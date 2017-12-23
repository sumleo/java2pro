package Crawler.Utils;

import java.util.Properties;

public class ConfigLoader {
    public static String dbPath;
    public static String dbDriver;
    private static String file = "data.cnf";
    public static String baseUrl;

    /**
     * Initial the config data
     */
    public static void initial() {
        Properties properties = new Properties();
        try {
            properties.load(ConfigLoader.class.getClassLoader().getResourceAsStream(file));
            dbPath = properties.getProperty("dataSource");
            dbDriver = properties.getProperty("dbDriver");
            baseUrl = properties.getProperty("url");
        } catch (Exception e) {
            System.out.println("Can't find data.cnf");
        }
    }
}
