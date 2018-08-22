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

public class MijlocFixTableInitializer {

    private static TableDisplayer<MijlocFixData> td = null;
    private static ListenerCallbeck lc = null;

    public static void initializeTable() throws SQLException
    {

        td = new TableDisplayer<>();

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
        td.getLabel().setText("Mijloc fix");

        TableColumn nrInventar = new TableColumn("Nr. Inventar");
        nrInventar.setMinWidth(100);
        nrInventar.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("nrInventar"));

        TableColumn mifixSiCar = new TableColumn("Descriptie");
        mifixSiCar.setMinWidth(500);
        mifixSiCar.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("mifixSiCar"));


        //............setting up tooltips
        mifixSiCar.setCellFactory(new Callback<TableColumn<MijlocFixData, Object>, TableCell<MijlocFixData, Object>>() {
            @Override
            public TableCell<MijlocFixData, Object> call(TableColumn<MijlocFixData, Object> p) {
                return new TableCell<MijlocFixData, Object>() {
                    @Override
                    public void updateItem(Object t, boolean empty) {
                        super.updateItem(t, empty);
                        if (t == null) {
                            setTooltip(null);
                            setText(null);
                        } else {
                            Tooltip tooltip = new Tooltip();
                            MijlocFixData myModel = getTableView().getItems().get(getTableRow().getIndex());
                            tooltip.setText(Util.breackInLine(myModel.getMifixSiCar(), 300));
                            setTooltip(tooltip);
                            setText(t.toString());
                        }
                    }
                };
            }
        });

        TableColumn clasificare = new TableColumn("Clasificare");
        clasificare.setMinWidth(20);
        clasificare.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("clasificare"));


        TableColumn durataAmortizarii = new TableColumn("Durata");
        durataAmortizarii.setMinWidth(20);
        durataAmortizarii.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, Integer>("durataAmortizarii"));


        TableColumn regimDeAmortizare = new TableColumn("Regim de amortizare");
        regimDeAmortizare.setMinWidth(70);
        regimDeAmortizare.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("regimDeAmortizare"));


        TableColumn termenDeGarantie = new TableColumn("termen de garantie");
        termenDeGarantie.setMinWidth(50);
        termenDeGarantie.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("termenDeGarantie"));


        TableColumn contDebitor = new TableColumn("cont debitor");
        contDebitor.setMinWidth(10);
        contDebitor.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("contDebitor"));

        TableColumn contCreditor = new TableColumn("cont creditor");
        contCreditor.setMinWidth(10);
        contCreditor.setCellValueFactory(
                new PropertyValueFactory<MijlocFixData, String>("contCreditor"));

        td.getTable().getColumns().addAll(nrInventar, mifixSiCar, clasificare, durataAmortizarii, regimDeAmortizare, termenDeGarantie, contDebitor, contCreditor);
    }

    public static void setData() throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());    //get the connection
        Statement st = c.createStatement();                                         //make a statement
        ResultSet rs = st.executeQuery(Finals.SELECT_FROM_MIJLOC_FIX_SQL);         //get the clasificari

        while(rs.next())
        {
            td.getData().add(new MijlocFixData( rs.getString("nrInventar"),
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

    public static class MijlocFixData {
        private final SimpleStringProperty nrInventar;
        private final SimpleStringProperty mifixSiCar;
        private final SimpleStringProperty clasificare;
        private final SimpleIntegerProperty durataAmortizarii;
        private final SimpleStringProperty regimDeAmortizare;
        private final SimpleStringProperty termenDeGarantie;
        private final SimpleStringProperty contDebitor;
        private final SimpleStringProperty contCreditor;

        public MijlocFixData(String nrInventar, String mifixSiCar, String clasificare, Integer durataAmortizarii, String regimDeAmortizare, String termenDeGarantie, String contDebitor, String contCreditor) {
            this.nrInventar = new SimpleStringProperty(nrInventar);
            this.mifixSiCar = new SimpleStringProperty(mifixSiCar);
            this.clasificare = new SimpleStringProperty(clasificare);
            this.durataAmortizarii = new SimpleIntegerProperty(durataAmortizarii);
            this.regimDeAmortizare = new SimpleStringProperty(regimDeAmortizare);
            this.termenDeGarantie = new SimpleStringProperty(termenDeGarantie);
            this.contDebitor = new SimpleStringProperty(contDebitor);
            this.contCreditor = new SimpleStringProperty(contCreditor);
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

        public String getMifixSiCar() {
            return mifixSiCar.get();
        }

        public SimpleStringProperty mifixSiCarProperty() {
            return mifixSiCar;
        }

        public void setMifixSiCar(String mifixSiCar) {
            this.mifixSiCar.set(mifixSiCar);
        }

        public String getClasificare() {
            return clasificare.get();
        }

        public SimpleStringProperty clasificareProperty() {
            return clasificare;
        }

        public void setClasificare(String clasificare) {
            this.clasificare.set(clasificare);
        }

        public int getDurataAmortizarii() {
            return durataAmortizarii.get();
        }

        public SimpleIntegerProperty durataAmortizariiProperty() {
            return durataAmortizarii;
        }

        public void setDurataAmortizarii(int durataAmortizarii) {
            this.durataAmortizarii.set(durataAmortizarii);
        }

        public String getRegimDeAmortizare() {
            return regimDeAmortizare.get();
        }

        public SimpleStringProperty regimDeAmortizareProperty() {
            return regimDeAmortizare;
        }

        public void setRegimDeAmortizare(String regimDeAmortizare) {
            this.regimDeAmortizare.set(regimDeAmortizare);
        }

        public String getTermenDeGarantie() {
            return termenDeGarantie.get();
        }

        public SimpleStringProperty termenDeGarantieProperty() {
            return termenDeGarantie;
        }

        public void setTermenDeGarantie(String termenDeGarantie) {
            this.termenDeGarantie.set(termenDeGarantie);
        }

        public String getContCreditor() {
            return contCreditor.get();
        }

        public SimpleStringProperty contCreditorProperty() {
            return contCreditor;
        }

        public void setContCreditor(String contCreditor) {
            this.contCreditor.set(contCreditor);
        }

        public String getContDebitor() {
            return contDebitor.get();
        }

        public SimpleStringProperty contDebitorProperty() {
            return contDebitor;
        }

        public void setContDebitor(String contDebitor) {
            this.contDebitor.set(contDebitor);
        }
    }

    public static TableDisplayer<MijlocFixData> getTd() {
        return td;
    }

    public static boolean show() throws SQLException
    {
        if (td == null)
        {
            initializeTable();
            td.show();
            return true;
        }
        else
        {

            td.show();
            return false;
        }

    }

    public static void hide()
    {
        if (td != null)
            td.hide();
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
