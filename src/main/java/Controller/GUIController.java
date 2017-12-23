package Controller;

import Utils.Eckert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import Configure.ConfigLoader;
import DAO.DataBuffer;
import Model.Earchquake;
import Utils.CSVinitial;
import Utils.DataReader;
import Utils.Mercator;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class GUIController implements Initializable {
    @FXML
    ChoiceBox region;
    @FXML
    DatePicker from;
    @FXML
    DatePicker to;
    @FXML
    Slider mag;
    @FXML
    TableView<Earchquake> data;
    @FXML
    Text output;
    @FXML
    BarChart chartByMag;
    @FXML
    CategoryAxis cateMag;
    @FXML
    BarChart chartByDate;
    @FXML
    HBox mercatorH;
    @FXML
    Canvas mercator;
    @FXML
    HBox eckertH;
    @FXML
    Canvas eckert;
    @FXML
    Text magText;
    @FXML
    GridPane queryGrid;

    /**
     * Get magnitude value
     *
     * @param mouseEvent
     */
    public void changeMagText(MouseEvent mouseEvent) {
        double magitude = (double) ((int) (10 * mag.getValue())) / 10;
        magText.setText(String.valueOf(magitude));
    }

    /**
     * CralwerInitial handler funciton of search action
     *
     * @param mouseEvent
     */
    public void search(MouseEvent mouseEvent) {
        if (from.getValue() == null && to.getValue() == null) {
            error();
            return;
        }
        DataBuffer.clear();
        String regionData = String.format((String) region.getValue());
        LocalDate fromDate = from.getValue();
        LocalDate toDate = to.getValue();
        double magData = mag.getValue();
        startQuery();
//        if (fromDate != null && toDate != null) {
//            if (fromDate.compareTo(toDate) > 0) {
//                error();
//                return;
//            }
//            queryByCsv(regionData,fromDate,toDate,magData);
//        }else if (fromDate==null){
//            queryByCsv(regionData,toDate,toDate,magData);
//        }else {
//            queryByCsv(regionData,fromDate,LocalDate.now(),magData);
//        }
        if (fromDate != null && toDate != null) {
            if (fromDate.compareTo(toDate) > 0) {
                error();
                return;
            }
            DataReader.GetData(fromDate, toDate, magData, regionData);
        } else if (fromDate == null) {
            DataReader.GetData(toDate, LocalDate.now(), magData, regionData);
        } else if (toDate == null) {
            DataReader.GetData(fromDate, LocalDate.now(), magData, regionData);
        }
        clearAll();
        setTableData();
        createChartByMag(magData);
        createChartByDate();
        Mercator.Draw(mercator);
        Eckert.Draw(eckert);
        endQuery();
    }

    /**
     * Some query notes
     */
    private void startQuery() {
        output.setText("Querying...");
    }

    private void endQuery() {
        output.setText(DataBuffer.getData().size() + " items have been found.");
    }

    private void error() {
        output.setText("The date might be error.");
    }

    /**
     * Query data by csv
     */
    private void queryByCsv(String region, LocalDate from, LocalDate to, double mag) {
        CSVinitial csVinitial = new CSVinitial();
        csVinitial.initial(from, to, mag, region);
    }

    /**
     * Set data table
     */
    private void setTableData() {
        ArrayList<Earchquake> earchquakes = DataBuffer.getData();
        for (Earchquake earchquake : earchquakes) data.getItems().add(earchquake);
    }

    private void clearTableData() {
        data.getItems().clear();
    }

    /**
     * Create Chart by magnitude
     *
     * @param mag
     */
    private void createChartByMag(double mag) {
        int m3 = 0, m4 = 0, m5 = 0, m6 = 0, m7 = 0;
        ArrayList<Earchquake> earchquakes = DataBuffer.getData();
        double m;
        for (Earchquake earchquake : earchquakes) {
            m = earchquake.getMagnitude();
            if (m < 3) m3++;
            else if (m < 4) m4++;
            else if (m < 5) m5++;
            else if (m < 6) m6++;
            else m7++;
        }
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        series.getData().add(new XYChart.Data<String, Integer>("Under 3.0", m3));
        series.getData().add(new XYChart.Data<String, Integer>("3.0 to 4.0", m4));
        series.getData().add(new XYChart.Data<String, Integer>("4.0 to 5.0", m5));
        series.getData().add(new XYChart.Data<String, Integer>("5.0 to 6.0", m6));
        series.getData().add(new XYChart.Data<String, Integer>("6.0 and over", m7));
        series.setName("Numbers");
        chartByMag.getData().add(series);
    }

    /**
     * Create chart order by increasing date
     */
    private void createChartByDate() {
        Map<String, Integer> hashMap = new TreeMap<String, Integer>();
        ArrayList<Earchquake> earchquakes = DataBuffer.getData();
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int has;
        for (Earchquake earchquake : earchquakes) {
            if (hashMap.containsKey(earchquake.getDateToKey())) {
                has = hashMap.get(earchquake.getDateToKey());
                hashMap.put(earchquake.getDateToKey(), ++has);
            } else {
                hashMap.put(earchquake.getDateToKey(), 1);
            }
        }
        //Sort the data by increasing date
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(hashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for (HashMap.Entry<String, Integer> kv : list) {
            series.getData().add(new XYChart.Data<String, Integer>(kv.getKey(), kv.getValue()));
        }
        //series.setName("Numbers");
        chartByDate.setLegendVisible(false);
        chartByDate.getData().add(series);
    }

    /**
     * Clear all former data
     */
    private void clearAll() {
        clearMercator();
        clearChartByDate();
        clearTableData();
        clearChartByMag();
        clearEckert();
    }

    /**
     * Clear chart By Mag
     */
    private void clearChartByMag() {
        chartByMag.getData().clear();
    }

    /**
     * Clear chart by date
     */
    private void clearChartByDate() {
        chartByDate.getData().clear();
    }

    /**
     * Clear mercator data
     */
    private void clearMercator() {
        mercator.getGraphicsContext2D().clearRect(0, 0, mercator.getWidth(), mercator.getHeight());
    }

    /**
     * Clear eckert data
     */
    private void clearEckert() {
        eckert.getGraphicsContext2D().clearRect(0, 0, eckert.getWidth(), eckert.getHeight());
    }

    /**
     * Initial region data and set images
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConfigLoader.initial();
        //Set up the image
        BackgroundImage backgroundImage = new BackgroundImage(new Image("Mercator.jpg", mercator.getWidth(), mercator.getHeight(), false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        mercatorH.setBackground(new Background(backgroundImage));
        BackgroundImage eckertImg = new BackgroundImage(new Image("Eckert_IV.png", eckert.getWidth(), eckert.getHeight(), false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        eckertH.setBackground(new Background(eckertImg));
        BackgroundImage queryImg = new BackgroundImage(new Image("307.png", 960, 300, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        queryGrid.setBackground(new Background(queryImg));
        //Initial region
//        region.getItems().add("World Wide");
//        CSVinitial csVinitial=new CSVinitial();
//        for (String reg:csVinitial.getRegion()){
//            region.getItems().add(reg);
//        }
//        csVinitial=null;
        DataReader.ReadRegion();
        region.getItems().add("World Wide");
        for (String reg : DataReader.hashMap.keySet()) {
            region.getItems().add(reg);
        }
    }
}
