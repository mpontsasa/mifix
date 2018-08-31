import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
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


public class ClasificariTableInitializer {

    private static TableDisplayer<ClasificariData> td = null;
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

        td.getLabel().setText("Clasificari de mijloc fix");

        TableColumn cod = new TableColumn("Cod de clasificare");
        cod.setMinWidth(100);
        cod.setCellValueFactory(
                new PropertyValueFactory<ClasificariData, String>("cod"));

        TableColumn desc = new TableColumn("Clasificare");
        desc.setMinWidth(1100);
        desc.setCellValueFactory(
                new PropertyValueFactory<ClasificariData, String>("desc"));


        //............setting up tooltips
        desc.setCellFactory(new Callback<TableColumn<ClasificariData, Object>, TableCell<ClasificariData, Object>>() {
            @Override
            public TableCell<ClasificariData, Object> call(TableColumn<ClasificariData, Object> p) {
                return new TableCell<ClasificariData, Object>() {
                    @Override
                    public void updateItem(Object t, boolean empty) {
                        super.updateItem(t, empty);
                        if (t == null) {
                            setTooltip(null);
                            setText(null);
                        } else {
                            Tooltip tooltip = new Tooltip();
                            ClasificariData myModel = getTableView().getItems().get(getTableRow().getIndex());
                            tooltip.setText(Util.breackInLine(myModel.getDesc(), 300));
                            setTooltip(tooltip);
                            setText(t.toString());
                        }
                    }
                };
            }
        });

        TableColumn minDur = new TableColumn("min. dur.");
        minDur.setMinWidth(50);
        minDur.setCellValueFactory(
                new PropertyValueFactory<ClasificariData, Integer>("minDur"));

        TableColumn maxDur = new TableColumn("max. dur.");
        maxDur.setMinWidth(50);
        maxDur.setCellValueFactory(
                new PropertyValueFactory<ClasificariData, Integer>("maxDur"));

        td.getTable().getColumns().addAll(cod, desc, minDur, maxDur);

        setUpSearchField();
    }

    public static void setUpSearchField()
    {
        td.setUpSearchField();

        for (int i = 0; i < td.getSearchTextFields().size(); i++)
        {
            td.getSearchTextFields().get(i).textProperty().addListener((observable, oldValue, newValue) -> {
                td.getFilteredData().setPredicate(mifix -> {

                    if (newValue == null || newValue.isEmpty()) {       // nem tudom miert, de enelkul nem megy
                        //return true;
                        //System.out.println("mindegy");
                    }
                    System.out.println("b");
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

    public static void setData() throws SQLException
    {
        Connection c = MySQLJDBCUtil.getConnection(Finals.COMMON_DATABASE_NAME);    //get the connection
        Statement st = c.createStatement();                                         //make a statement
        ResultSet rs = st.executeQuery(Finals.SELECT_FROM_CLASIFICARI_SQL);         //get the clasificari

        td.getData().clear();
        while(rs.next())
        {
            td.getData().add(new ClasificariData(rs.getString("cod"), rs.getString("description"), rs.getInt("minDur"), rs.getInt("maxDur")));
        }

        rs.close();
        st.close();
        c.close();
    }

    public static class ClasificariData {
        private final SimpleStringProperty cod;
        private final SimpleStringProperty desc;
        private final SimpleIntegerProperty minDur;
        private final SimpleIntegerProperty maxDur;

        private ClasificariData(String fName, String lName, Integer minDur, Integer maxDur) {
            this.cod = new SimpleStringProperty(fName);
            this.desc = new SimpleStringProperty(lName);
            this.minDur = new SimpleIntegerProperty(minDur);
            this.maxDur = new SimpleIntegerProperty(maxDur);
        }

        public String getProperty(int index)
        {
            switch(index)
            {
                case 0:
                    return getCod();
                case 1:
                    return getDesc();
                case 2:
                    return "" + getMinDur();
                case 3:
                    return "" + getMaxDur();
                }

            return null;
        }


        public String getCod() {
            return cod.get();
        }

        public void setCod(String cd) {
            cod.set(cd);
        }

        public String getDesc() {
            return desc.get();
        }

        public void setDesc(String dsc) {
            desc.set(dsc);
        }

        public Integer getMinDur() {
            return minDur.get();
        }

        public void setMinDur(Integer mDur) {
            minDur.set(mDur);
        }

        public Integer getMaxDur() {
            return maxDur.get();
        }

        public void setMaxDur(Integer mDur) {
            maxDur.set(mDur);
        }
    }

    public static TableDisplayer<ClasificariData> getTd() {
        return td;
    }

    public static boolean show() throws SQLException    //shows the table. If it had to make the initialization(first time) return true
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

}
