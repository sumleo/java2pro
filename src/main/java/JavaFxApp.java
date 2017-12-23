

import Crawler.CralwerInitial;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxApp extends Application {
    /**
     * Initial the stage
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            CralwerInitial cralwerInitial = new CralwerInitial();
            cralwerInitial.start();
            Parent root = FXMLLoader.load(getClass().getResource("MainGUI.fxml"));
            primaryStage.setTitle("Earquakes");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * main function
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
