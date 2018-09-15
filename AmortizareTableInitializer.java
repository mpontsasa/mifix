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

public class AmortizareTableInitializer {
    private static ArrayList<OperatiuniTableDisplayer<AmortizareData>> tds = new ArrayList<>();

    public static TableDisplayer initializeTable(String nrInv, LocalDate start, LocalDate end) throws SQLException
    {

        OperatiuniTableDisplayer<AmortizareData> td = new OperatiuniTableDisplayer<>(nrInv, start, end);
        tds.add(td);

        setData(td, nrInv, start, end);

        td.getStage().setWidth(700);
        td.getStage().setHeight(700);
        td.getTable().setPrefHeight(600);
        td.getTable().setPrefWidth(650);

        String title = "Suspendari ";

        if (nrInv != null && !nrInv.isEmpty())
        {
            title += nrInv + " ";
        }

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


        if (nrInv == null || nrInv.isEmpty())
        {
            TableColumn nrInventar = new TableColumn("Nr. Inventar");
            nrInventar.setMinWidth(100);
            nrInventar.setCellValueFactory(
                    new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("nrInventar"));

            td.getTable().getColumns().add(nrInventar);
        }

        TableColumn startDate = new TableColumn("De la");
        startDate.setMinWidth(32);
        startDate.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("startDate"));


        TableColumn endDate = new TableColumn("pana");
        endDate.setMinWidth(32);
        endDate.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("endDate"));


        td.getTable().getColumns().addAll(startDate, endDate);

        td.show();

        setUpSearchField(td);

        return td;
    }

    public static void setUpSearchField(OperatiuniTableDisplayer<AmortizareData> td)
    {
        td.setUpSearchField();

        for (int i = 0; i < td.getSearchTextFields().size(); i++)
        {
            td.getSearchTextFields().get(i).textProperty().addListener((observable, oldValue, newValue) -> {
                //System.out.println("text changed");
                td.getFilteredData().setPredicate(mifix -> {

                    if (newValue == null || newValue.isEmpty()) {}       // nem tudom miert, de enelkul nem megy

                    int nrInvDif = 0;   //0 if I dont have nrInv column, 1 if I do

                    if (td.getNrInventar() == null || td.getNrInventar().isEmpty()) //if I have nr inventar column
                    {
                        if (td.getSearchTextFields().get(0).getText() != null && !td.getSearchTextFields().get(0).getText().isEmpty() &&
                                !mifix.getNrInventar().toLowerCase().contains(td.getSearchTextFields().get(0).getText().toLowerCase()))
                        {
                            return false;
                        }
                        nrInvDif = 1;
                    }

                    for (int j = 0; j < td.getTable().getColumns().size() - nrInvDif; j++)
                    {
                        if (td.getSearchTextFields().get(j + nrInvDif).getText() != null && !td.getSearchTextFields().get(j + nrInvDif).getText().isEmpty() &&
                                !mifix.getProperty(j).toLowerCase().contains(td.getSearchTextFields().get(j + nrInvDif).getText().toLowerCase()))
                        {
                            return false;
                        }
                    }
                    return true;
                });
            });
        }
    }

    public static void setData(TableDisplayer td, String nrInventar, LocalDate start, LocalDate end) throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement

        String sqlQuery = "select suspendareID, nrInventar, startDate, endDate from suspendari, mijlocFix " +
                "where mijlocFix.mifixID = suspendari.mifixID ";

        if (nrInventar != null && !nrInventar.isEmpty())
        {
            sqlQuery += " and nrInventar = '" + nrInventar + "' ";

        }

        if (start != null)
        {
            sqlQuery += "and endDate >= '" + start.toString() + "' ";
        }

        if (end != null)
        {
            sqlQuery += "and startDate <= '" + end.toString() + "' ";
        }

        sqlQuery += "group by startDate;";

        System.out.println(sqlQuery);
        ResultSet rs = st.executeQuery(sqlQuery);

        while(rs.next())
        {
            td.getData().add(new AmortizareData(
                    rs.getInt("suspendareID"),
                    rs.getString("nrInventar"),
                    rs.getString("startDate"),
                    rs.getString("endDate"))
            );
        }

        rs.close();
        st.close();
        c.close();
    }

    public static class AmortizareData {
        private int amortizareID;
        private final SimpleStringProperty nrInventar;
        private final SimpleStringProperty date;
        private final SimpleFloatProperty valoareAmortizari;
        private final SimpleFloatProperty diferenta;

        public String getProperty(int index) {
            switch(index)
            {
                case 0:
                    return getNrInventar();
                case 1:
                    return getDate();
                case 2:
                    return getValoareAmortizari();
                case 3:
                    return getDiferenta();
            }

            return null;
        }

        public AmortizareData(int amortizareID, SimpleStringProperty nrInventar, SimpleStringProperty date, SimpleFloatProperty valoareAmortizari, SimpleFloatProperty diferenta) {
            this.amortizareID = amortizareID;
            this.nrInventar = nrInventar;
            this.date = date;
            this.valoareAmortizari = valoareAmortizari;
            this.diferenta = diferenta;
        }

        public int getAmortizareID() {
            return amortizareID;
        }

        public void setAmortizareID(int amortizareID) {
            this.amortizareID = amortizareID;
        }

        public String getNrInventar() {
            return nrInventar.get();
        }

        public SimpleStringProperty nrInventarProperty() {
            return nrInventar;
        }

        public void setNrInventar(String nrInventar) {
            this.nrInventar.set(nrInventar);
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

        public float getValoareAmortizari() {
            return valoareAmortizari.get();
        }

        public SimpleFloatProperty valoareAmortizariProperty() {
            return valoareAmortizari;
        }

        public void setValoareAmortizari(float valoareAmortizari) {
            this.valoareAmortizari.set(valoareAmortizari);
        }

        public float getDiferenta() {
            return diferenta.get();
        }

        public SimpleFloatProperty diferentaProperty() {
            return diferenta;
        }

        public void setDiferenta(float diferenta) {
            this.diferenta.set(diferenta);
        }
    }

    public static ArrayList<OperatiuniTableDisplayer<AmortizareData>> getTds() {
        return tds;
    }

    public static void reload(String nrInv) throws SQLException
    {
        for (OperatiuniTableDisplayer<AmortizareData> td : tds)
        {
            if (td.getNrInventar().equals(nrInv) || td.getNrInventar() == null || td.getNrInventar().isEmpty())
            {
                td.getData().clear();
                setData(td, nrInv, td.getStart(), td.getEnd());
            }
        }
    }
}
