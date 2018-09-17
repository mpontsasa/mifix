import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class SQLExecuter {

    public static Connection getConnection() throws SQLException {
        return MySQLJDBCUtil.getConnection();
    }


    public static void executeFile(String pathName) throws SQLException
    {
        Connection c = getConnection();
        executeFile(pathName, c);
    }

    public static void executeFile(String pathName, Connection c) throws SQLException {
        String s;
        StringBuffer sb = new StringBuffer();

        try {
            FileReader fr = new FileReader(new File(pathName));
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character

            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append("\n" + s);
            }
            br.close();

            execute(sb.toString(), c);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void execute(String sqlCommands) throws SQLException
    {
        Connection c = SQLExecuter.getConnection();
        execute(sqlCommands, c);
    }

    public static void execute(String sqlCommands, Connection c) throws SQLException
    {
        try
        {

            String[] inst = sqlCommands.toString().split(Finals.SQL_COMMAND_DELIMITER);

            Statement st = c.createStatement();

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not executeFile empty statements
                if (!inst[i].trim().equals("")) {
                    //System.out.println(">>" + inst[i]);
                    st.executeUpdate(inst[i]);
                    System.out.println(">>" + inst[i]);
                }
            }

        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sqlCommands.toString());
        }
    }
}