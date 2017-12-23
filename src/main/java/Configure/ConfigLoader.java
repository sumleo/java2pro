package Configure;

import java.util.Properties;

public class ConfigLoader {
    public static String dbPath;
    public static String dbDriver;
    private static String file = "data.cnf";
    public static String csvLocation;

    /**
     * Loading the settings
     */
    public static void initial() {
        Properties properties = new Properties();
        try {
            properties.load(ConfigLoader.class.getClassLoader().getResourceAsStream(file));
            dbPath = properties.getProperty("dataSource");
            dbDriver = properties.getProperty("dbDriver");
            csvLocation = properties.getProperty("csvLocation");
        } catch (Exception e) {
            System.out.println(">>>Can't find data.cnf");
        }
    }
}
