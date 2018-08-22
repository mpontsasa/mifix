import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OperatiuniTableInitializer {
    private static ArrayList<TableDisplayer<operatieData>> tds = new ArrayList<>();
    private static ListenerCallbeck lc = null;

    public static void initializeTable() throws SQLException
    {

        TableDisplayer<operatieData> td = new TableDisplayer<>();
        tds.add(td);

        td.getStage().setOnCloseRequest(event -> {
            if (lc != null)
            {
                lc.action();
            }
        });

        setData();

        td.getStage().setWidth(1400);
        td.getStage().setHeight(700);
        td.getTable().setPrefHeight(600);
        td.getTable().setPrefWidth(1350);
        td.getLabel().setText("Operatiuni");

        TableColumn nrInventar = new TableColumn("Nr. Inventar");
        nrInventar.setMinWidth(100);
        nrInventar.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("nrInventar"));

        TableColumn nrReceptie = new TableColumn("Nr. receptie");
        nrReceptie.setMinWidth(500);
        nrReceptie.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("nrReceptie"));

        TableColumn felDocument = new TableColumn("Fel document");
        felDocument.setMinWidth(20);
        felDocument.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("felDocument"));


        TableColumn nrDocument = new TableColumn("Nr. document");
        nrDocument.setMinWidth(20);
        nrDocument.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, Integer>("nrDocument"));


        TableColumn dataOperatiei = new TableColumn("Regim de amortizare");
        dataOperatiei.setMinWidth(70);
        dataOperatiei.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("dataOperatiei"));

        TableColumn valoareFaraTVA = new TableColumn("Valoare fara TVA");
        valoareFaraTVA.setMinWidth(50);
        valoareFaraTVA.setCellValueFactory(
                new PropertyValueFactory<MijlocFixTableInitializer.MijlocFixData, String>("valoareFaraTVA"));

        td.getTable().getColumns().addAll(nrInventar, nrReceptie, felDocument, nrDocument, dataOperatiei, valoareFaraTVA);
    }

    public static void setData() throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement
        ResultSet rs = st.executeQuery(Finals.SELECT_FROM_MIJLOC_FIX_SQL);         //get the clasificari

        while(rs.next())
        {
            td.getData().add(new MijlocFixTableInitializer.MijlocFixData( rs.getString("nrInventar"),
                    rs.getString("mifixSiCaracteristiceTechnice"),
                    rs.getString("clasificare"),
                    rs.getInt("durataAmortizarii"),
                    rs.getString("regimDeAmortizare"),
                    rs.getString("termenDeGarantie"),
                    rs.getString("contDebitor"),
                    rs.getString("contCreditor")));
        }

        rs.close();
        st.close();
        c.close();
    }

    public static class operatieData {
        private final SimpleStringProperty nrInventar;
        private final SimpleStringProperty nrReceptie;
        private final SimpleStringProperty felDocument;
        private final SimpleStringProperty nrDocument;
        private final SimpleStringProperty dataOperatiei;
        private final SimpleIntegerProperty valoareFaraTVA;

        operatieData(String nrInventar, String nrReceptie, String felDocument, String nrDocument, String dataOperatiei, Integer valoareFaraTVA)
        {
            this.nrInventar = new SimpleStringProperty(nrInventar);
            this.nrReceptie = new SimpleStringProperty(nrReceptie);
            this.felDocument = new SimpleStringProperty(felDocument);
            this.nrDocument = new SimpleStringProperty(nrDocument);
            this.dataOperatiei = new SimpleStringProperty(dataOperatiei);
            this.valoareFaraTVA = new SimpleIntegerProperty(valoareFaraTVA);
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

        public int getValoareFaraTVA() {
            return valoareFaraTVA.get();
        }

        public SimpleIntegerProperty valoareFaraTVAProperty() {
            return valoareFaraTVA;
        }

        public void setValoareFaraTVA(int valoareFaraTVA) {
            this.valoareFaraTVA.set(valoareFaraTVA);
        }
    }

    public static TableDisplayer<OperatiuniTableInitializer.MijlocFixData> getTd() {
        return td;
    }

    public static ListenerCallbeck getLc() {
        return lc;
    }

    public static void setLc(ListenerCallbeck lc_) {
        lc = lc_;
    }

    public static void reload() throws SQLException
    {
        if (td != null)
        {
            td.getData().clear();
            setData();
        }
    }

}
