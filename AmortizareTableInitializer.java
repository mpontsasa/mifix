import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;

import java.sql.*;
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
        td.placeAndSize();



        String title = "Amortizari ";

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

        td.getTable().setEditable(true);

        td.getStage().setOnCloseRequest(event -> tds.remove(td));


        if (nrInv == null || nrInv.isEmpty())
        {
            TableColumn nrInventar = new TableColumn("Nr. Inventar");
            nrInventar.setMinWidth(100);
            nrInventar.setCellValueFactory(
                    new PropertyValueFactory<AmortizareData, String>("nrInventar"));

            td.getTable().getColumns().add(nrInventar);
        }

        TableColumn date = new TableColumn("Data");
        date.setMinWidth(32);
        date.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("date"));


        TableColumn valoareaCalculata = new TableColumn("Valoarea calculata");
        valoareaCalculata.setMinWidth(150);
        valoareaCalculata.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("valoareCalculata"));

        TableColumn diferenta = new TableColumn("Diferență");
        diferenta.setMinWidth(32);
        diferenta.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("diferenta"));

        diferenta.setCellFactory(TextFieldTableCell.forTableColumn(new MyFloatStringConverter()));

        diferenta.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<AmortizareData, Float>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<AmortizareData, Float> t) {

                        ((AmortizareData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDiferenta(t.getNewValue());

                        try(Connection c = MySQLJDBCUtil.getConnection();
                            PreparedStatement checkIfIncheiat = c.prepareStatement(Finals.CHECK_IF_INCHEIAT);
                            PreparedStatement updatePstm = c.prepareStatement("update amortizare set diferenta = ? where amortizareID = ?;");
                            Statement s = c.createStatement())
                        {
                            s.executeUpdate(Finals.SET_QUOTES_SQL);
                            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

                            checkIfIncheiat.setString(1, ((AmortizareData) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDate());
                            ResultSet rs =  checkIfIncheiat.executeQuery();

                            if (rs.next())
                            {
                                updatePstm.setFloat(1, t.getNewValue());
                                updatePstm.setFloat(2, ((AmortizareData) t.getTableView().getItems().get(t.getTablePosition().getRow())).getAmortizareID());

                                updatePstm.executeUpdate();
                            }
                            else
                            {
                                Alerts.errorAlert(Finals.MONTH_INCHEIAT_TITLE, Finals.MONTH_INCHEIAT_HEADER, Finals.MONTH_INCHEIAT_CONTENT);
                            }
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }
        );

        /*diferenta.setEditable(true);
        diferenta.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<AmortizareData, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<AmortizareData, String> t) {
                        ((AmortizareData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDiferenta(Float.parseFloat(t.getNewValue()));

                        System.out.println("Yeeee");

                    }
                }
        );*/

        TableColumn valoareAmortizare= new TableColumn("Valoare amortizare");
        valoareAmortizare.setMinWidth(150);
        valoareAmortizare.setCellValueFactory(
                new PropertyValueFactory<AmortizareData, String>("valoareAmortizare"));

        td.getTable().getColumns().addAll(date, valoareaCalculata, diferenta, valoareAmortizare);

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

        String sqlQuery = "select amortizareID, nrInventar, monthOfAmortizare, calculatedValue, diferenta from amortizare, mijlocFix " +
                "where mijlocFix.mifixID = amortizare.mifixID ";

        if (nrInventar != null && !nrInventar.isEmpty())
        {
            sqlQuery += " and nrInventar = '" + nrInventar + "' ";

        }


        if (start != null)
        {
            sqlQuery += "and monthOfAmortizare >= '" + start.toString() + "' ";
        }

        if (end != null)
        {
            sqlQuery += "and monthOfAmortizare <= '" + end.toString() + "' ";
        }

        sqlQuery += " order by nrInventar, monthOfAmortizare;";

        System.out.println(sqlQuery);
        ResultSet rs = st.executeQuery(sqlQuery);

        while(rs.next())
        {
            td.getData().add(new AmortizareData(
                    rs.getInt("amortizareID"),
                    rs.getString("nrInventar"),
                    rs.getString("monthOfAmortizare"),
                    rs.getFloat("calculatedValue"),
                    rs.getFloat("diferenta"),
                    rs.getFloat("calculatedValue") + rs.getFloat("diferenta")
                    )
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
        private final SimpleFloatProperty valoareCalculata;
        private final SimpleFloatProperty diferenta;
        private final SimpleFloatProperty valoareAmortizare;

        public String getProperty(int index) {
            switch(index)
            {
                case 0:
                    return getNrInventar();
                case 1:
                    return getDate();
                case 2:
                    return "" + getValoareCalculata();
                case 3:
                    return "" + getDiferenta();
                case 4:
                    return  "" + getValoareAmortizare();
            }

            return null;
        }

        public AmortizareData(int amortizareID, String nrInventar, String date, Float valoareCalculata, Float diferenta, Float valoareAmortizare) {
            this.amortizareID = amortizareID;
            this.nrInventar = new SimpleStringProperty(nrInventar);
            this.date = new SimpleStringProperty(date);
            this.valoareCalculata = new SimpleFloatProperty(valoareCalculata);
            this.diferenta = new SimpleFloatProperty(diferenta);
            this.valoareAmortizare = new SimpleFloatProperty(valoareAmortizare);
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

        public float getValoareCalculata() {
            return valoareCalculata.get();
        }

        public SimpleFloatProperty valoareCalculataProperty() {
            return valoareCalculata;
        }

        public void setValoareCalculata(float valoareCalculata) {
            this.valoareCalculata.set(valoareCalculata);
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

        public float getValoareAmortizare() {
            return valoareAmortizare.get();
        }

        public SimpleFloatProperty valoareAmortizareProperty() {
            return valoareAmortizare;
        }

        public void setValoareAmortizare(float valoareAmortizare) {
            this.valoareAmortizare.set(valoareAmortizare);
        }
    }

    public static ArrayList<OperatiuniTableDisplayer<AmortizareData>> getTds() {
        return tds;
    }

    public static void reload(String nrInv) throws SQLException
    {
        for (OperatiuniTableDisplayer<AmortizareData> td : tds)
        {
            if (nrInv == null || nrInv.isEmpty() || td.getNrInventar().equals(nrInv) || td.getNrInventar() == null || td.getNrInventar().isEmpty())
            {
                td.getData().clear();
                setData(td, nrInv, td.getStart(), td.getEnd());
            }
        }
    }
}
