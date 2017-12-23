package Crawler.Utils;

import Crawler.Model.Earthquake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class UpdateData {
    private Connection connection;
    private Statement statement;
    private long date;

    public UpdateData() {
        initial();
    }

    /**
     * Initial the database connection
     */
    private void initial() {
        try {
            Class.forName(ConfigLoader.dbDriver);
            connection = DriverManager.getConnection(ConfigLoader.dbPath);
            statement = connection.createStatement();
            newestDate();
        } catch (Exception e) {
            System.out.println(">>>Initial failed!");
        }
    }

    /**
     * Update data main function
     */
    public void update() {
        System.out.println(new Date() + " I'm start working");
        int page = 1;
        ArrayList<Earthquake> earthquakes;
        Crawler crawler = new Crawler(ConfigLoader.baseUrl + page);
        crawler.doCrawling();
        if (crawler.getMax() < date) {
            System.out.println("The data is already newest!");
            return;
        }
        long tempMin = new Date().getTime();
        int count = 0;
        while (true) {
            if (tempMin < date) {
                break;
            }
            earthquakes = crawler.getEarthquakes();
            for (Earthquake earthquake : earthquakes) {
                if (earthquake.getDate() > date && tempMin > earthquake.getDate()) {
                    if (insert(earthquake)) {
                        count++;
                    }
                }
            }
            System.out.println("I'm updating the page " + ConfigLoader.baseUrl + page);
            page++;
            tempMin = crawler.getMin();
            crawler = null;
            crawler = new Crawler(ConfigLoader.baseUrl + page);
            crawler.doCrawling();
        }
        System.out.println(">>> " + count + " items updated.");
        System.out.println(new Date() + " Everything is new");
        clear();
    }

    /**
     * Insert earthquake object to database
     *
     * @param earthquake
     * @return
     */
    private boolean insert(Earthquake earthquake) {
        try {
            String sql = "insert into quakes values(" + earthquake.getId() + ",'" + earthquake.getUtcDate() + "'," + earthquake.getLatitude() + "," + earthquake.getLongitude()
                    + "," + earthquake.getDepth() + "," + earthquake.getMagnitude() + ",'" + earthquake.getRegion() + "'," + earthquake.getArea_id() + ");";
            statement.execute(sql);
            return true;
        } catch (Exception e) {
            System.out.println(">>> The item already existed!");
        }
        return false;
    }

    /**
     * Get the newest Date information
     */
    private void newestDate() {
        try {
            date = statement.executeQuery("SELECT * FROM quakes ORDER BY utc_date DESC ").getTimestamp("utc_date").getTime();
        } catch (Exception e) {
            System.out.println(">>> Can't find the newestDate");
        }
    }

    /**
     * Clear the variables to save memory space
     */
    private void clear() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            statement = null;
            connection = null;
        } catch (Exception e) {
            System.out.println(">>>Clear all failed!");
        }

    }
}
