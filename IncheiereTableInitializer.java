import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class IncheiereTableInitializer {
    private static ArrayList<OperatiuniTableDisplayer<IncheiereData>> tds = new ArrayList<>();

    public static TableDisplayer initializeTable(String nrInv, LocalDate start, LocalDate end) throws SQLException
    {

        OperatiuniTableDisplayer<IncheiereData> td = new OperatiuniTableDisplayer<>(nrInv, start, end);
        tds.add(td);

        setData(td, start, end);

        td.getStage().setWidth(700);
        td.getStage().setHeight(700);
        td.getTable().setPrefHeight(600);
        td.getTable().setPrefWidth(650);

        String title = "Inceiere ";


        if (start!= null && end != null)
        {
            td.getLabel().setText(title + "intre " + start.toString() + "  -  " + end.toString());
        }
        else if (start != null)
        {
            td.getLabel().setText(title + "dupa " + start.toString());
        }
        else if (end != null)
        {
            td.getLabel().setText(title + "inainte de " + end.toString());
        }
        else
        {
            td.getLabel().setText(title);
        }

        td.getStage().setOnCloseRequest(event -> {
            tds.remove(td);
        });


        TableColumn date = new TableColumn("Data");
        date.setMinWidth(32);
        date.setCellValueFactory(
                new PropertyValueFactory<IncheiereData, String>("date"));

        td.getTable().getColumns().addAll(date);

        td.show();

        setUpSearchField(td);

        return td;
    }

    public static void setUpSearchField(OperatiuniTableDisplayer<IncheiereData> td)
    {
        td.setUpSearchField();

        for (int i = 0; i < td.getSearchTextFields().size(); i++)
        {
            td.getSearchTextFields().get(i).textProperty().addListener((observable, oldValue, newValue) -> {
                //System.out.println("text changed");
                td.getFilteredData().setPredicate(mifix -> {

                    if (newValue == null || newValue.isEmpty()) {}       // nem tudom miert, de enelkul nem megy

                    for (int j = 0; j < td.getTable().getColumns().size(); j++)
                    {
                        if (td.getSearchTextFields().get(j).getText() != null && !td.getSearchTextFields().get(j).getText().isEmpty() &&
                                !mifix.getProperty(j).toLowerCase().contains(td.getSearchTextFields().get(j).getText().toLowerCase()))
                        {
                            return false;
                        }
                    }
                    return true;
                });
            });
        }
    }

    public static void setData(TableDisplayer td, LocalDate start, LocalDate end) throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement

        String sqlQuery = "select monthIncheiat from incheiereMonth " +
                "where true ";

        if (start != null)
        {
            sqlQuery += "and monthIncheiat >= '" + start.toString() + "' ";
        }

        if (end != null)
        {
            sqlQuery += "and monthIncheiat <= '" + end.toString() + "' ";
        }

        sqlQuery += "group by monthIncheiat;";

        System.out.println(sqlQuery);
        ResultSet rs = st.executeQuery(sqlQuery);

        while(rs.next())
        {
            td.getData().add(new IncheiereData(
                    rs.getString("monthIncheiat"))
            );
        }

        rs.close();
        st.close();
        c.close();
    }

    public static class IncheiereData {

        private final SimpleStringProperty date;

        public String getProperty(int index) {
            switch(index)
            {
                case 0:
                    return getDate();
            }

            return null;
        }


        public IncheiereData(String  date) {
            this.date = new SimpleStringProperty(date);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }
    }

    public static ArrayList<OperatiuniTableDisplayer<IncheiereData>> getTds() {
        return tds;
    }

    public static void reload() throws SQLException
    {
        for (OperatiuniTableDisplayer<IncheiereData> td : tds)
        {
                td.getData().clear();
                setData(td, td.getStart(), td.getEnd());
        }
    }
}
