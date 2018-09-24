import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static java.sql.Types.FLOAT;
import static java.sql.Types.INTEGER;

public class DatabasePorter {

    public static void exportDatabase(String dbName) throws SQLException, IOException
    {


        String content = "";

        try(Connection c = MySQLJDBCUtil.getConnection();
            Statement s = c.createStatement();
            Statement s2 = c.createStatement())
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + dbName + "\";");

            ResultSet tablesRS = s.executeQuery(Finals.ShOW_TABLES_SQL);

            while(tablesRS.next())  //every table
            {
                //pstm.setString(1,tablesRS.getString(1));

                ResultSet rows = s2.executeQuery("select * from " + tablesRS.getString(1));


                content += "----\n";
                content += "insert into " + tablesRS.getString(1) + " VALUES \n";

                if (!rows.next())
                {
                    content += "();\n";
                }
                else
                {

                    while (true) //every row
                    {
                        content += "(";
                        for (int i = 1; i <= rows.getMetaData().getColumnCount(); i++) //every column
                        {
                            if (i > 1)
                                content += ",";

                            if (rows.getString(i) == null)
                                content += null;
                            else if (rows.getMetaData().getColumnType(i) == INTEGER || rows.getMetaData().getColumnType(i) == FLOAT)
                            {
                                content += rows.getString(i);
                            }
                            else
                            {
                                content += "'" + rows.getString(i) + "'";
                            }
                        }

                        if (rows.next())
                        {
                            content += "),";
                        }
                        else
                        {
                            content += ");";
                            break;
                        }

                        content += "\n";
                    }

                    content += "\n";
                }


                content += "\n";
            }

            //..............print to file

            File file = new File( Finals.EXPORT_PATH + dbName + "_" + LocalDate.now().toString() + ".sql");
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(Finals.EXPORT_PATH + dbName+ "_" + LocalDate.now().toString() + ".sql");

            fw.write(content);

            fw.close();
        }
    }
}
