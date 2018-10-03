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
    private static ArrayList<OperatiuniTableDisplayer<OperatieData>> tds = new ArrayList<>();

    public static TableDisplayer initializeTable(String nrInv, LocalDate start, LocalDate end) throws SQLException
    {

        OperatiuniTableDisplayer<OperatieData> td = new OperatiuniTableDisplayer<>(nrInv, start, end);
        tds.add(td);

        setData(td, nrInv, start, end);

        td.getStage().setWidth(700);
        td.getStage().setHeight(700);
        td.getTable().setPrefHeight(600);
        td.getTable().setPrefWidth(650);

        String title = "Operatiuni ";

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
                    new PropertyValueFactory<OperatieData, String>("nrInventar"));

            td.getTable().getColumns().add(nrInventar);
        }



        TableColumn felOperatie = new TableColumn("Fel operatie");
        felOperatie.setMinWidth(32);
        felOperatie.setCellValueFactory(
                new PropertyValueFactory<OperatieData, String>("felOperatiei"));


        TableColumn nrReceptie = new TableColumn("Nr. receptie");
        nrReceptie.setMinWidth(32);
        nrReceptie.setCellValueFactory(
                new PropertyValueFactory<OperatieData, String>("nrReceptie"));

        TableColumn felDocument = new TableColumn("Fel document");
        felDocument.setMinWidth(10);
        felDocument.setCellValueFactory(
                new PropertyValueFactory<OperatieData, String>("felDocument"));


        TableColumn nrDocument = new TableColumn("Nr. document");
        nrDocument.setMinWidth(32);
        nrDocument.setCellValueFactory(
                new PropertyValueFactory<OperatieData, Integer>("nrDocument"));


        TableColumn dataOperatiei = new TableColumn("Regim de amortizare");
        dataOperatiei.setMinWidth(70);
        dataOperatiei.setCellValueFactory(
                new PropertyValueFactory<OperatieData, String>("dataOperatiei"));

        TableColumn valoareFaraTVA = new TableColumn("Valoare fara TVA");
        valoareFaraTVA.setMinWidth(15);
        valoareFaraTVA.setCellValueFactory(
                new PropertyValueFactory<OperatieData, String>("valoareFaraTVA"));

        td.getTable().getColumns().addAll(felOperatie, nrReceptie, felDocument, nrDocument, dataOperatiei, valoareFaraTVA);

        td.show();

        setUpSearchField(td);

        return td;
    }

    public static void setUpSearchField(OperatiuniTableDisplayer<OperatieData> td)
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

        String sqlQuery = "select operatiebase.operatieID as opID, nrInventar, commonDataDB.feluriOperatiei.denumire as felOperatieidenumire , nrReceptie, felDocument, nrDocument, dataOperatiei, sum(valoareFaraTVA) as valoareFaraTVASum " +
                "from mijlocFix " +
                "join operatiebase on mijlocFix.mifixID = operatiebase.mifixID " +
                "Left Join operatievalori on operatiebase.operatieID = operatieValori.operatieID " +
                "Join commonDataDB.feluriOperatiei on commonDataDB.feluriOperatiei.felOperatieiID = operatiebase.felOperatieiID ";

        if (nrInventar != null && !nrInventar.isEmpty())
        {
            sqlQuery += "where nrInventar = '" + nrInventar + "' ";

        }

        if (start != null)
        {
            sqlQuery += "and dataOperatiei >= '" + start.toString() + "' ";
        }

        if (end != null)
        {
            sqlQuery += "and dataOperatiei <= '" + end.toString() + "' ";
        }

        sqlQuery += "group by operatiebase.operatieID " +
                "order by dataOperatiei;";

        System.out.println(sqlQuery);
        ResultSet rs = st.executeQuery(sqlQuery);

        while(rs.next())
        {
            td.getData().add(new OperatieData(
                    rs.getInt("opID"),
                    rs.getString("nrInventar"),
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

    public static class OperatieData {
        private int operatieID;
        private final SimpleStringProperty nrInventar;
        private final SimpleStringProperty felOperatiei;
        private final SimpleStringProperty nrReceptie;
        private final SimpleStringProperty felDocument;
        private final SimpleStringProperty nrDocument;
        private final SimpleStringProperty dataOperatiei;
        private final SimpleFloatProperty valoareFaraTVA;

        OperatieData(int operatieID, String nrInventar, String felOperatiei, String nrReceptie, String felDocument, String nrDocument, String dataOperatiei, Float valoareFaraTVA)
        {
            this.operatieID = operatieID;
            this.nrInventar = new SimpleStringProperty(nrInventar);
            this.felOperatiei = new SimpleStringProperty(felOperatiei);
            this.nrReceptie = new SimpleStringProperty(nrReceptie);
            this.felDocument = new SimpleStringProperty(felDocument);
            this.nrDocument = new SimpleStringProperty(nrDocument);
            this.dataOperatiei = new SimpleStringProperty(dataOperatiei);
            this.valoareFaraTVA = new SimpleFloatProperty(valoareFaraTVA);
        }

        public String getProperty(int index) {
            switch(index)
            {
                case 0:
                    return getFelOperatiei();
                case 1:
                    return getNrReceptie();
                case 2:
                    return getFelDocument();
                case 3:
                    return "" + getNrDocument();
                case 4:
                    return getDataOperatiei();
                case 5:
                    return "" + getValoareFaraTVA();
                }

            return null;
        }

        public int getOperatieID() {
            return operatieID;
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

    public static ArrayList<OperatiuniTableDisplayer<OperatieData>> getTds() {
        return tds;
    }

    public static void reload(String nrInv) throws SQLException
    {
        for (OperatiuniTableDisplayer<OperatieData> td : tds)
        {
            if (td.getNrInventar().equals(nrInv)  || td.getNrInventar() == null || td.getNrInventar().isEmpty())
            {
                td.getData().clear();
                setData(td, nrInv, td.getStart(), td.getEnd());
            }
        }
    }

}
