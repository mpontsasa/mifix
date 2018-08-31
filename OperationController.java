import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.LocalDate;

public class OperationController {

    private OperationBaseController operationBaseController;
    private String felOperatiei;

    public void initializeBase(HBox sublayer, String felOperatiei)
    {
        this.felOperatiei = felOperatiei;
        initializeBase(sublayer);
    }

    public void initializeBase(HBox sublayer)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "operationBase.fxml"));
            HBox baseOperationVBox = loader.load();

            operationBaseController = (OperationBaseController) loader.getController();

            //sublayer.getChildren().add(baseOperationVBox);
            sublayer.getChildren().add(0, baseOperationVBox);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public OperationBaseController getOperationBaseController() {
        return operationBaseController;
    }

    public void mijlocFixSelectedInTable()
    {
        operationBaseController.nrInventarTextField.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getNrInventar());
    }

    public void operatieSelectedInTable(OperatiuniTableDisplayer<OperatiuniTableInitializer.OperatieData> otd)
    {
        operationBaseController.nrInventarTextField.setText(otd.getNrInventar());
        operationBaseController.nrReceptieTextField.setText(otd.getTable().getSelectionModel().getSelectedItem().getNrReceptie());
        operationBaseController.felDocumentTextField.setText(otd.getTable().getSelectionModel().getSelectedItem().getFelDocument());
        operationBaseController.nrDocumentTextField.setText(otd.getTable().getSelectionModel().getSelectedItem().getNrDocument());

        if (otd.getTable().getSelectionModel().getSelectedItem().getDataOperatiei() != null)
            operationBaseController.dataOperatieiDatePicker.setValue(LocalDate.parse(otd.getTable().getSelectionModel().getSelectedItem().getDataOperatiei()));

        //..................................loading values

        operationBaseController.removeAllValueBars();

        try (Connection conn = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());
             PreparedStatement pstmt = conn.prepareStatement(Finals.SELECT_VALORI_FOR_OPERATION_SQL))
        {

            pstmt.setInt(1, otd.getTable().getSelectionModel().getSelectedItem().getOperatieID());

            ResultSet rs = pstmt.executeQuery();

            while(rs.next())
            {
                operationBaseController.addValueBar(rs.getFloat("valoareFaraTVA"),
                                                    rs.getInt("procentTVAID"),
                                                    rs.getInt("procentTVA"),
                                                    rs.getFloat("diferentaTVA"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void adaugareInDatabase()
    {
        try
        {
            if (getOperationBaseController().validateInputForAdaugare())
            {
                Connection c = MySQLJDBCUtil.getConnection();
                Statement s = c.createStatement();
                s.executeUpdate(Finals.SET_QUOTES_SQL);
                s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");

                //........inserting into operatiebase

                String insSql = "insert into \"Freeform 2005\".operatiebase (mifixID, nrReceptie, felDocument, nrDocument, dataOperatiei, felOperatieiID) VALUES \n" +
                        "((Select mifixID from mijlocFix where nrInventar='"+ getOperationBaseController().nrInventarTextField.getText() +"')"+ ", '" +
                        getOperationBaseController().nrReceptieTextField.getText() + "', '"+
                        getOperationBaseController().felDocumentTextField.getText() +"', '"+
                        getOperationBaseController().nrDocumentTextField.getText() +"', '"+
                        getOperationBaseController().dataOperatieiDatePicker.getValue().toString() +"', \n" +
                        "(Select commonDataDB.felurioperatiei.felOperatieiID from commondatadb.felurioperatiei where denumire='"+felOperatiei+"'));";

                System.out.println(insSql);
                s.executeUpdate(insSql);

                ///.................... insert values

                for(ValueBarController vbc : getOperationBaseController().getValueBarControllers())
                {
                    if (vbc.getValoareFaraTVATextField().getText() != null && !vbc.getValoareFaraTVATextField().getText().equals(""))   //if its not an empty valoare
                    {
                        insSql = "insert into \"Freeform 2005\".operatieValori (operatieID, procentTVAID, procentTVA, valoareFaraTVA, diferentaTVA)\n" +
                                "Values ((Select MAX(\"Freeform 2005\".operatiebase.operatieID) FROM \"Freeform 2005\".operatiebase), \n" +
                                (vbc.getProcentTVAComboBox().getItems().indexOf(vbc.getProcentTVAComboBox().getValue()) + 1) + ", ";

                        if (vbc.getProcentTVAComboBox().getItems().indexOf(vbc.getProcentTVAComboBox().getValue()) <= 2)
                            insSql += vbc.getProcentTVAComboBox().getValue().toString();
                        else
                            insSql += "0";

                        insSql += ", " +
                                vbc.getValoareFaraTVATextField().getText() + ", ";

                        if (vbc.getDiferentaTVATextField().getText() == null || vbc.getDiferentaTVATextField().getText().equals(""))
                            insSql += "0";
                        else
                            insSql += vbc.getDiferentaTVATextField().getText();

                        insSql += ");";

                        System.out.println(insSql);
                        s.executeUpdate(insSql);
                    }
                }

                //..................
                s.close();
                c.close();

                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);

            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            //....
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void modificareInDatabase(OperatiuniTableInitializer.OperatieData operatieData)
    {
        stergereInDatabase(operatieData);
        adaugareInDatabase();
    }

    public void stergereInDatabase(OperatiuniTableInitializer.OperatieData operatieData)
    {
        try (Connection conn = MySQLJDBCUtil.getConnection(Main.getSocietateActuala());
             PreparedStatement pstmt = conn.prepareStatement(Finals.DELETE_OPERATION_SQL))
        {

            pstmt.setInt(1, operatieData.getOperatieID());

            pstmt.executeUpdate();



        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getFelOperatiei() {
        return felOperatiei;
    }

    public void setFelOperatiei(String felOperatiei) {
        this.felOperatiei = felOperatiei;
    }
}
