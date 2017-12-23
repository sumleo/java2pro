package Crawler.Utils;

import Crawler.Model.Earthquake;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Crawler {
    private ArrayList<Earthquake> earthquakes;
    private String url;
    private long min;
    private long max;

    public Crawler(String url) {
        this.url = url;
    }

    /**
     * Crawler of data and generate earthquakes objects
     */
    public void doCrawling() {
        try {
            Document doc = Jsoup.connect(url).execute().parse();
            Elements elements = doc.getElementsByClass("ligne1");
            elements.addAll(doc.getElementsByClass("ligne2"));
            int reg;
            earthquakes = null;
            earthquakes = new ArrayList<Earthquake>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
            min = new Date().getTime();
            for (Element element : elements) {
                Earthquake earthquake = new Earthquake();
                //id
                earthquake.setId(Integer.parseInt(element.getElementsByAttribute("id").get(0).attr("id")));
                //UTC Date
                earthquake.setUtcDate(element.getElementsByClass("tabev6").get(0).text().replace("earthquake", "").substring(0, 21));
                //latitude
                earthquake.setLatitude(element.getElementsByClass("tabev2").get(0).text().equals("N") ? Float.parseFloat(element.getElementsByClass("tabev1").get(0).text().trim()) : -1 * Float.parseFloat(element.getElementsByClass("tabev1").get(0).text().trim()));
                //longitude
                earthquake.setLongitude(element.getElementsByClass("tabev2").get(1).text().equals("E") ? Float.parseFloat(element.getElementsByClass("tabev1").get(1).text().trim()) : -1 * Float.parseFloat(element.getElementsByClass("tabev1").get(1).text().trim()));
                //Depth
                earthquake.setDepth(Integer.parseInt(element.getElementsByClass("tabev3").get(0).text()));
                //Mag
                earthquake.setMagnitude(Float.parseFloat(element.getElementsByClass("tabev2").get(2).text().trim()));
                //Region
                earthquake.setRegion(element.getElementsByClass("tb_region").get(0).text());
                //Region Id
                earthquake.setArea_id(Integer.parseInt(element.getElementsByAttributeValueStarting("id", "reg").attr("id").replace("reg", "")));
                try {
                    earthquake.setDate(simpleDateFormat.parse(earthquake.getUtcDate()).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (min > earthquake.getDate()) min = earthquake.getDate();
                if (max < earthquake.getDate()) max = earthquake.getDate();
                earthquakes.add(earthquake);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Network error");
        }
    }

    public ArrayList<Earthquake> getEarthquakes() {
        return earthquakes;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }
}
