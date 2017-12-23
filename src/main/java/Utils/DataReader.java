package Utils;

import Configure.ConfigLoader;
import DAO.DataBuffer;
import Model.Earchquake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DataReader {
    public static HashMap<String, String> hashMap;

    /**
     * Read region
     */
    public static void ReadRegion() {
        try {
            Class.forName(ConfigLoader.dbDriver);
            Connection connection = DriverManager.getConnection(ConfigLoader.dbPath);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM plates");
            hashMap = new HashMap<String, String>();
            while (rs.next()) {
                if (!hashMap.containsKey(rs.getString("name"))) {
                    hashMap.put(rs.getString("name"), rs.getString("code"));
                }
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(">>>Read region error!");
            e.printStackTrace();
        }
    }

    /**
     * Get data
     *
     * @param from
     * @param to
     * @param mag
     * @param region
     */
    public static void GetData(LocalDate from, LocalDate to, double mag, String region) {
        try {
            Class.forName(ConfigLoader.dbDriver);
            Connection connection = DriverManager.getConnection(ConfigLoader.dbPath);
            Statement statement = connection.createStatement();
            String reg;
            if (!region.equals("World Wide")) {
                reg = getRegion(hashMap.get(region), statement);
            } else {
                reg = "";
            }
            ResultSet rs = statement.executeQuery("SELECT * FROM quakes WHERE utc_date >='" + from.toString() + "' AND utc_date <='" + to.plusDays(1).toString() + "' and magnitude >= " + mag + " " + reg + ";");
            DataBuffer.clear();
            DataBuffer.setAllData();
            ArrayList<Earchquake> earchquakes = DataBuffer.getData();
            while (rs.next()) {
                Earchquake earchquake = new Earchquake();
                earchquake.setDis_Date(rs.getString("utc_date"));
                earchquake.setId(rs.getInt("id"));
                earchquake.setRegion(rs.getString("region"));
                earchquake.setMagnitude(rs.getDouble("magnitude"));
                earchquake.setDepth(rs.getInt("depth"));
                earchquake.setLatitude(rs.getDouble("latitude"));
                earchquake.setLongitude(rs.getDouble("longitude"));
                earchquakes.add(earchquake);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(">>>Get Data error!");
        }
    }

    /**
     * Get region by string
     *
     * @param reg
     * @param statement
     * @return
     */
    private static String getRegion(String reg, Statement statement) {
        try {
            String result = " and ( ";
            ResultSet resultSet = statement.executeQuery("SELECT * FROM plate_areas WHERE plate1='" + reg + "' or plate2='" + reg + "';");
            int count = 0;
            while (resultSet.next()) {
                if (count == 0) {
                    result += " ( latitude >= " + resultSet.getDouble("minlat") + " and latitude <= " + resultSet.getDouble("maxlat")
                            + " and longitude >= " + resultSet.getDouble("minlon") + " and longitude <= " + resultSet.getDouble("maxlon") + " )";
                } else {
                    result += " or ( latitude >= " + resultSet.getDouble("minlat") + " and latitude <= " + resultSet.getDouble("maxlat")
                            + " and longitude >= " + resultSet.getDouble("minlon") + " and longitude <= " + resultSet.getDouble("maxlon") + " )";
                }
                count++;
            }
            result += " )";
            return result;
        } catch (Exception e) {
            System.out.println("Get Region failed!");
            return "";
        }
    }
}
