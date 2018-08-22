import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author mysqltutorial.org
 */
public class MySQLJDBCUtil {

    /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException
     */

    static String dbPath = "";

    public static Connection getConnection(String dbName) throws SQLException   // connect to a database
    {
        dbPath = "/" + dbName;
        Connection conn = getConnection();
        dbPath = "";
        return conn;
    }

    public static Connection getConnection() throws SQLException {  //connect to the server
        Connection conn = null;

        try (FileInputStream f = new FileInputStream("db.properties")) {

            // load the properties file
            Properties pros = new Properties();
            pros.load(f);

            // assign db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");

            // create a connection to the database
            conn = DriverManager.getConnection(url + dbPath, user, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static boolean databaseExists(String databaseName) throws Exception
    {
        String sql = "SHOW DATABASES LIKE '" + databaseName +"'";
        Connection conn = MySQLJDBCUtil.getConnection();
        Statement stmt  = conn.createStatement();

        ResultSet rs    = stmt.executeQuery(sql);

        rs.last();
        int nrOfRows = rs.getRow();
        rs.beforeFirst();

        rs.close();
        stmt.close();
        conn.close();

        return nrOfRows != 0;
    }

    public static boolean recordExists(String database, String table, String column, String value) throws SQLException
    {
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(Finals.SET_QUOTES_SQL);
        s.executeUpdate("use \"" + database + "\";");

        String sqlQuery = "SELECT "+column+" from "+ table +" where " +column+" = '"+ value +"';";

        ResultSet rs = s.executeQuery(sqlQuery);

        rs.last();
        int nrOfRows = rs.getRow();
        rs.beforeFirst();

        rs.close();
        s.close();
        c.close();

        return nrOfRows != 0;
    }

    public static boolean setUpSocietateDB(String selectedSocietate) throws Exception
    {
        if (MySQLJDBCUtil.databaseExists(selectedSocietate.trim()))
        {
            return true;
        }

        if(Alerts.confirmationAlert(Finals.SOCIETATE_DATABASE_NOT_SET_UP_TITLE_TEXT, selectedSocietate, Finals.SOCIETATE_DATABASE_NOT_SET_UP_CONTENT_TEXT))
        {
            Connection c = MySQLJDBCUtil.getConnection();
            Statement s = c.createStatement();
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("create database \"" + selectedSocietate.trim() + "\";");
            s.executeUpdate("use \"" + selectedSocietate.trim() + "\";");

            SQLExecuter.executeFile("setUpSocietateDB.sql", c);

            s.close();
            //ps.close();
            c.close();

            return true;
        }
        else
        {
            return false;
        }
    }

    public static void deleteValuesOfOperatie(int operatieID) throws SQLException
    {
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(Finals.SET_QUOTES_SQL);
        s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

        String sqlQuery = "DELETE from \"" + Main.getSocietateActuala() + "\".operatieValori where operatieID=" + operatieID + ";";

        s.executeUpdate(sqlQuery);

        s.close();
        c.close();

    }

    public static void deleteOperatie(int operatieId)
    {

    }
}