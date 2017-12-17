package Utils;

import Configure.DatabaseConfig;
import com.sun.corba.se.spi.orbutil.fsm.Guard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLhandler {
    public void doQuery() {
        try {
            Class.forName(DatabaseConfig.DRIVER);
            Connection connection = DriverManager.getConnection(DatabaseConfig.DATABASE_PATH, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            Statement statement = connection.createStatement();
            String sql = "select * from country";
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("countryname"));
            }
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
