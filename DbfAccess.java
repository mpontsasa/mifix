import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jamel.dbf.DbfReader;
import org.jamel.dbf.processor.DbfProcessor;
import org.jamel.dbf.processor.DbfRowProcessor;
import org.jamel.dbf.structure.DbfRow;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;


public class DbfAccess {

    public ObservableList<String> getSocietatiNames()
    {
        /*File dbf = new File(Finals.SOCIETATI_PATH);
        SocietatiNamesCalculator calc = new SocietatiNamesCalculator();
        DbfProcessor.processDbf(dbf, calc);*/

        ObservableList<String> options = FXCollections.observableArrayList();

        try (DbfReader reader = new DbfReader(new File(Finals.SOCIETATI_PATH))) {

            DbfRow row;
            while ((row = reader.nextRow()) != null) {
                options.add(row.getString("SOCMENU"));
            }
        }

        return options;

        //return calc.getOptions();
    }

    public static String contExists (String input)  // returns the database version if exists(may differ in spacing), returns null if not
    {
        try (DbfReader reader = new DbfReader(new File(Finals.CONTDIC_PATH))) {

            DbfRow row;
            while ((row = reader.nextRow()) != null) {

                if (Util.noSpace(row.getString("CONT")).equals(Util.noSpace(input)))
                    return row.getString("CONT");
            }
        }
        return null;
    }

    public static ArrayList<Integer> getTVAProcent()
    {
        ArrayList<Integer> res = new ArrayList<>();
        try (DbfReader reader = new DbfReader(new File(Finals.TVA_PATH))) {

            Date maxDate;

            DbfRow row;
            row = reader.nextRow();
            maxDate = row.getDate("DATA");


            while ((row = reader.nextRow()) != null)
            {
                if(row.getDate("DATA").after(maxDate))
                {
                    maxDate = row.getDate("DATA");

                    Float PTVA1 = row.getFloat("PTVA1") * 100;
                    Float PTVA2 = row.getFloat("PTVA2") * 100;
                    Float PTVA3 = row.getFloat("PTVA3") * 100;

                    res.clear();
                    res.add(PTVA1.intValue());
                    res.add(PTVA2.intValue());
                    res.add(PTVA3.intValue());

                }
            }
        }
        return res;
    }

}
