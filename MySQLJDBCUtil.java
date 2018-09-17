import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public static boolean databaseExists(String databaseName) throws Exception {
        String sql = "SHOW DATABASES LIKE '" + databaseName + "'";
        Connection conn = MySQLJDBCUtil.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        rs.last();
        int nrOfRows = rs.getRow();
        rs.beforeFirst();

        rs.close();
        stmt.close();
        conn.close();

        return nrOfRows != 0;
    }

    public static boolean recordExists(String database, String table, String column, String value) throws SQLException {
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(Finals.SET_QUOTES_SQL);
        s.executeUpdate("use \"" + database + "\";");

        String sqlQuery = "SELECT " + column + " from " + table + " where " + column + " = '" + value + "';";

        System.out.println(sqlQuery);

        ResultSet rs = s.executeQuery(sqlQuery);

        rs.last();
        int nrOfRows = rs.getRow();
        rs.beforeFirst();

        rs.close();
        s.close();
        c.close();

        return nrOfRows != 0;
    }

    public static boolean setUpSocietateDB(String selectedSocietate) throws Exception {
        if (MySQLJDBCUtil.databaseExists(selectedSocietate.trim())) {
            return true;
        }

        if (Alerts.confirmationAlert(Finals.SOCIETATE_DATABASE_NOT_SET_UP_TITLE_TEXT, selectedSocietate, Finals.SOCIETATE_DATABASE_NOT_SET_UP_CONTENT_TEXT)) {
            Connection c = MySQLJDBCUtil.getConnection();
            Statement s = c.createStatement();
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("create database \"" + selectedSocietate.trim() + "\";");
            s.executeUpdate("use \"" + selectedSocietate.trim() + "\";");
            SQLExecuter.executeFile(Finals.SQL_QUERIES + "setUpSocietateDB.sql", c);

            s.close();
            //ps.close();
            c.close();

            return true;
        } else {
            return false;
        }
    }

    public static void updateMifix(String nrInvOld, String nrInv, String mifixSiCharTech, String clasificare, String inceputulAmortizarii, int durataAmortizarii, String regimDeAmortizare, LocalDate termenDeGarantie, String contDebitor, String contCreditor) throws SQLException {
        Connection c = MySQLJDBCUtil.getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(Finals.SET_QUOTES_SQL);
        s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

        String updSql = "update mijlocFix set " +
                " nrInventar = '" + nrInv + "'," +
                "mifixSiCaracteristiceTechnice = '" + mifixSiCharTech + "', " +
                "clasificare = '" + clasificare + "', " +
                "inceputulAmortizarii = '" + inceputulAmortizarii + "', " +
                "durataAmortizarii = " + durataAmortizarii + ", " +
                "regimDeAmortizare = '" + regimDeAmortizare + "', ";

        if (termenDeGarantie == null) {
            updSql += "termenDeGarantie = null, ";
        } else {
            updSql += "termenDeGarantie = '" + termenDeGarantie.toString() + "', ";
        }

        if (contDebitor == null || contDebitor.equals("")) {
            updSql += "contDebitor = null, ";
        } else {
            updSql += "contDebitor = '" + contDebitor + "', ";
        }

        if (contCreditor == null || contCreditor.equals("")) {
            updSql += "contCreditor = null ";
        } else {
            updSql += "contCreditor = '" + contCreditor + "'";
        }

        updSql += "where nrInventar = '" + nrInvOld + "';";

        System.out.println(updSql);
        s.executeUpdate(updSql);

        s.close();
        c.close();
    }

    public static ArrayList<String> getfeluriOperati() throws SQLException {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement

        ResultSet rs = st.executeQuery(Finals.SELECT_FELURI_DE_OPERATII_SQL);

        ArrayList<String> result = new ArrayList<>();

        while (rs.next()) {
            result.add(rs.getString("denumire"));
        }
        return result;
    }

    public static Float valueOfMifixAtADate(String nrInv, LocalDate date, Connection c) throws SQLException   // Operations on the day date dont count
    // Returns null if mifix is alredy sold or casat, or doesent exists yet
    {
        Float value = 0f;
        try (PreparedStatement getOpsPstm = c.prepareStatement(Finals.GET_ALL_OPERATIE_OF_MIFIX_SQL);
             Statement s = c.createStatement()) {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            getOpsPstm.setString(1, nrInv);
            ResultSet operations = getOpsPstm.executeQuery();

            while (operations.next()) {
                LocalDate dateOfOp = LocalDate.parse(operations.getString("dataOperatiei"));
                if (dateOfOp.isAfter(date) || dateOfOp.equals(date)) {
                    if (value.equals(0f))
                        return null;
                    return value;
                }

                if (operations.getString("felOperatieidenumire").equals(Finals.VANZARE_OP) ||
                        operations.getString("felOperatieidenumire").equals(Finals.CASARE_OP)) {
                    return null;
                } else if (operations.getString("felOperatieidenumire").equals(Finals.REEVALUARE_OP)) {
                    PreparedStatement reevValuePstm = c.prepareStatement(Finals.REEVALUARE_VALUE_SQL);
                    reevValuePstm.setInt(1, operations.getInt("opID"));
                    ResultSet reevValue = reevValuePstm.executeQuery();

                    reevValue.next();
                    value = reevValue.getFloat("newValue");
                } else {
                    value += operations.getFloat("valoareFaraTVASum");
                }
            }

        }
        return value;
    }

    public static Float valueAmortizata(String nrInv, LocalDate date, Connection c) throws SQLException   // Operations on the day date dont count
    // Returns null if mifix is alredy sold or casat, or doesent exists yet
    {
        Float res;
        try (PreparedStatement getOpsPstm = c.prepareStatement(Finals.AMORTIZAT_VALUE_UNTIL_SQL);
             Statement s = c.createStatement()) {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            getOpsPstm.setString(1, nrInv);
            getOpsPstm.setString(2, date.toString());
            ResultSet values = getOpsPstm.executeQuery();

            values.next();

            res = values.getFloat("diffSum") + values.getFloat("calcSum");
        }
        return res;
    }

    public static boolean isSuspended(String nrInv, LocalDate dateOfAmort, Connection c) throws SQLException {
        try (PreparedStatement pstm = c.prepareStatement(Finals.IS_SUSPENDED_SQL);
             Statement s = c.createStatement()) {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            pstm.setString(1, nrInv);
            pstm.setString(2, dateOfAmort.toString());

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        }

    }

    public static boolean isAmortizat(String nrInv, LocalDate dateOfAmort, Connection c) throws SQLException {

        try (PreparedStatement pstm = c.prepareStatement(Finals.IS_AMORTIZAT_SQL);
             Statement s = c.createStatement()) {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            pstm.setString(1, nrInv);
            pstm.setString(2, dateOfAmort.toString());
            pstm.setString(3, dateOfAmort.toString());

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        }

    }

    public static String getAmortizationStartingDate(String nrInv) throws SQLException
    {
        try(Connection c = getConnection();
            Statement s = c.createStatement();
            PreparedStatement pstm = c.prepareStatement(Finals.GET_AMORTIZATION_START_DATE_SQL))
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            pstm.setString(1, nrInv);

            ResultSet rs = pstm.executeQuery();

            rs.next();

            return rs.getString("inceputulAmortizarii");

        }
    }
}