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

public class OperatiuniTableInitializer {
    private static ArrayList<OperatiuniTableDisplayer<operatieData>> tds = new ArrayList<>();

    public static TableDisplayer initializeTable(String nrInv, LocalDate start, LocalDate end) throws SQLException
    {

        OperatiuniTableDisplayer<operatieData> td = new OperatiuniTableDisplayer<>(nrInv);
        tds.add(td);

        setData(td, nrInv, start, end);

        td.getStage().setWidth(700);
        td.getStage().setHeight(700);
        td.getTable().setPrefHeight(600);
        td.getTable().setPrefWidth(650);

        if (start!= null && end != null)
        {
            td.getLabel().setText("Operatiuni " + nrInv + " intre " + start.toString() + "  -  " + end.toString());
        }
        else if (start != null)
        {
            td.getLabel().setText("Operatiuni " + nrInv + " dupa " + start.toString());
        }
        else if (end != null)
        {
            td.getLabel().setText("Operatiuni " + nrInv + " inainte de " + end.toString());
        }
        else
        {
            td.getLabel().setText("Operatiuni " + nrInv);
        }

        td.getStage().setOnCloseRequest(event -> {
            tds.remove(td);
        });

        /*TableColumn felOperatiei = new TableColumn("Nr. Inventar");
        felOperatiei.setMinWidth(100);
        felOperatiei.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("felOperatiei"));*/

        TableColumn felOperatie = new TableColumn("Fel operatie");
        felOperatie.setMinWidth(32);
        felOperatie.setCellValueFactory(
                new PropertyValueFactory<operatieData, String>("felOperatiei"));


        TableColumn nrReceptie = new TableColumn("Nr. receptie");
        nrReceptie.setMinWidth(32);
        nrReceptie.setCellValueFactory(
                new PropertyValueFactory<operatieData, String>("nrReceptie"));

        TableColumn felDocument = new TableColumn("Fel document");
        felDocument.setMinWidth(10);
        felDocument.setCellValueFactory(
                new PropertyValueFactory<operatieData, String>("felDocument"));


        TableColumn nrDocument = new TableColumn("Nr. document");
        nrDocument.setMinWidth(32);
        nrDocument.setCellValueFactory(
                new PropertyValueFactory<operatieData, Integer>("nrDocument"));


        TableColumn dataOperatiei = new TableColumn("Regim de amortizare");
        dataOperatiei.setMinWidth(70);
        dataOperatiei.setCellValueFactory(
                new PropertyValueFactory<operatieData, String>("dataOperatiei"));

        TableColumn valoareFaraTVA = new TableColumn("Valoare fara TVA");
        valoareFaraTVA.setMinWidth(15);
        valoareFaraTVA.setCellValueFactory(
                new PropertyValueFactory<operatieData, String>("valoareFaraTVA"));

        td.getTable().getColumns().addAll(felOperatie, nrReceptie, felDocument, nrDocument, dataOperatiei, valoareFaraTVA);

        td.show();

        return td;
    }

    public static void setData(TableDisplayer td, String nrInventar, LocalDate start, LocalDate end) throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement

        String sqlQuery = "select operatiebase.operatieID as opID, commonDataDB.feluriOperatiei.denumire as felOperatieidenumire , nrReceptie, felDocument, nrDocument, dataOperatiei, sum(valoareFaraTVA) as valoareFaraTVASum " +
                "from mijlocFix " +
                "join operatiebase on mijlocFix.mifixID = operatiebase.mifixID " +
                "Left Join operatievalori on operatiebase.operatieID = operatieValori.operatieID " +
                "Join commonDataDB.feluriOperatiei on commonDataDB.feluriOperatiei.felOperatieiID = operatiebase.felOperatieiID " +
                "where nrInventar = '" + nrInventar + "' ";
        if (start != null)
        {
            sqlQuery += "and dataOperatiei >= '" + start.toString() + "' ";
        }

        if (end != null)
        {
            sqlQuery += "and dataOperatiei <= '" + end.toString() + "' ";
        }

        sqlQuery += "group by operatiebase.operatieID;";

        System.out.println(sqlQuery);
        ResultSet rs = st.executeQuery(sqlQuery);

        while(rs.next())
        {
            td.getData().add(new OperatiuniTableInitializer.operatieData(
                    rs.getInt("opID"),
                    rs.getString("felOperatieidenumire"),
                    rs.getString("nrReceptie"),
                    rs.getString("felDocument"),
                    rs.getString("nrDocument"),
                    rs.getString("dataOperatiei"),
                    rs.getFloat("valoareFaraTVASum"))
            );
        }

        rs.close();
        st.close();
        c.close();
    }

    public static class operatieData {
        private int operatieID;
        private final SimpleStringProperty felOperatiei;
        private final SimpleStringProperty nrReceptie;
        private final SimpleStringProperty felDocument;
        private final SimpleStringProperty nrDocument;
        private final SimpleStringProperty dataOperatiei;
        private final SimpleFloatProperty valoareFaraTVA;

        operatieData(int operatieID, String felOperatiei, String nrReceptie, String felDocument, String nrDocument, String dataOperatiei, Float valoareFaraTVA)
        {
            this.operatieID = operatieID;
            this.felOperatiei = new SimpleStringProperty(felOperatiei);
            this.nrReceptie = new SimpleStringProperty(nrReceptie);
            this.felDocument = new SimpleStringProperty(felDocument);
            this.nrDocument = new SimpleStringProperty(nrDocument);
            this.dataOperatiei = new SimpleStringProperty(dataOperatiei);
            this.valoareFaraTVA = new SimpleFloatProperty(valoareFaraTVA);
        }

        public int getOperatieID() {
            return operatieID;
        }

        public String getFelOperatiei() {
            return felOperatiei.get();
        }

        public SimpleStringProperty felOperatieiProperty() {
            return felOperatiei;
        }

        public void setFelOperatiei(String felOperatiei) {
            this.felOperatiei.set(felOperatiei);
        }

        public String getNrReceptie() {
            return nrReceptie.get();
        }

        public SimpleStringProperty nrReceptieProperty() {
            return nrReceptie;
        }

        public void setNrReceptie(String nrReceptie) {
            this.nrReceptie.set(nrReceptie);
        }

        public String getFelDocument() {
            return felDocument.get();
        }

        public SimpleStringProperty felDocumentProperty() {
            return felDocument;
        }

        public void setFelDocument(String felDocument) {
            this.felDocument.set(felDocument);
        }

        public String getNrDocument() {
            return nrDocument.get();
        }

        public SimpleStringProperty nrDocumentProperty() {
            return nrDocument;
        }

        public void setNrDocument(String nrDocument) {
            this.nrDocument.set(nrDocument);
        }

        public String getDataOperatiei() {
            return dataOperatiei.get();
        }

        public SimpleStringProperty dataOperatieiProperty() {
            return dataOperatiei;
        }

        public void setDataOperatiei(String dataOperatiei) {
            this.dataOperatiei.set(dataOperatiei);
        }

        public float getValoareFaraTVA() {
            return valoareFaraTVA.get();
        }

        public SimpleFloatProperty valoareFaraTVAProperty() {
            return valoareFaraTVA;
        }

        public void setValoareFaraTVA(int valoareFaraTVA) {
            this.valoareFaraTVA.set(valoareFaraTVA);
        }
    }

    public static void reload() throws SQLException
    {
        ///coming soon
    }

    public static ArrayList<OperatiuniTableDisplayer<operatieData>> getTds() {
        return tds;
    }
}
