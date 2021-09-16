package appointmentmaker.utility;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class that handles access to the Database and the distribution of DataSource connections to the Database
 */
public final class DBUtility {

    private static final String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    private static DataSource dataSource=null;

    /**
     * Creates DataSource object to connect to the Database
     */
    public static void dbConnect(){
            if(dataSource==null) {
                try{
                    Class.forName(JDBC_DRIVER);
                    dataSource = MyDataSourceFactory.getMySQLDataSource();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * Nullifies the DataSource object to disconnect
     */
    public static void dbDisconnect(){
        if(dataSource!=null)
            dataSource=null;
    }

    /**
     * Gets a connection from the connection pool
     * @return Connection object
     */
    public static Connection dbGetConnection(){

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbGetConnection();
    }

    /**
     * Closes ResultSet object after Database access if applicable
     * @param rs ResultSet object to potentially close
     */
    public static void dbCloseRsQuietly(ResultSet rs){
        if(rs != null){
            try{
                if(!rs.isClosed())
                    rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Inner class that creates the MySQL version of DataSource
     */
    static class MyDataSourceFactory{

        private static DataSource getMySQLDataSource(){
            Properties prop = new Properties();
            FileInputStream fis;
            MysqlDataSource mysqlDS = null;
            try{
                fis = new FileInputStream("src/appointmentmaker/utility/Logs/db.properties");
                //fis = new FileInputStream("src/appointmentmaker/utility/Logs/db1.properties");// for local use
                prop.load(fis);
                mysqlDS = new MysqlDataSource();
                mysqlDS.setURL(prop.getProperty("MYSQL_DB_URL"));
                mysqlDS.setUser(prop.getProperty("MYSQL_DB_USERNAME"));
                mysqlDS.setPassword(prop.getProperty("MYSQL_DB_PASSWORD"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mysqlDS;
        }
    }
}
