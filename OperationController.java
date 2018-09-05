import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.LocalDate;

public class OperationController {

    private OperationBaseController operationBaseController;
    private String felOperatiei;
    protected ActionsController actionsController;


    public void initializeBase(HBox sublayer, String felOperatiei)
    {
        this.actionsController = actionsController;
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

    public boolean adaugareBaseInDatabase()
    {
        try
        {
            if (getOperationBaseController().validateBaseInputForAdaugare())
            {
                Connection c = MySQLJDBCUtil.getConnection();
                Statement s = c.createStatement();
                s.execute(Finals.START_TRANSACTION);

                adaugareBaseInDatabase(c);

                s.execute(Finals.COMMIT_TRANSACTION);
                s.close();
                c.close();

                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            //....
            return false;
        }
    }

    public boolean modificareBaseInDatabase()
    {
        try (Connection conn = MySQLJDBCUtil.getConnection(Main.getSocietateActuala()))
        {
            if (validateInputForModificare())
            {

                Statement s = conn.createStatement();
                s.execute(Finals.START_TRANSACTION);

                modificareBaseInDatabase(conn);

                s.execute(Finals.COMMIT_TRANSACTION);
                s.close();
                conn.close();

                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                return true;
            }
            else
            {
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean stergereBaseInDatabase()
    {
        try (Connection conn = MySQLJDBCUtil.getConnection(Main.getSocietateActuala()))
        {
            if (validateInputForStergere())
            {

                Statement s = conn.createStatement();
                s.execute(Finals.START_TRANSACTION);

                stergereBaseInDatabase(conn);

                s.execute(Finals.COMMIT_TRANSACTION);
                s.close();
                conn.close();



                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                return true;

            }
            else
                return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void adaugareBaseInDatabase(Connection c) throws SQLException
    {
        Statement s = c.createStatement();
        s.executeUpdate(Finals.SET_QUOTES_SQL);
        s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");

        //........inserting into operatiebase

        String insSql = "insert into operatiebase (mifixID, nrReceptie, felDocument, nrDocument, dataOperatiei, felOperatieiID) VALUES \n" +
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
                insSql = "insert into operatieValori (operatieID, procentTVAID, procentTVA, valoareFaraTVA, diferentaTVA)\n" +
                        "Values ((Select MAX(operatiebase.operatieID) FROM operatiebase), \n" +
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

    }

    public void modificareBaseInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmtUpdate = c.prepareStatement(Finals.UPDATE_OPERATIE_BASE_SQL);
            PreparedStatement pstmtDeleteValues = c.prepareStatement(Finals.DELETE_VALORI_SQL);
             Statement s = c.createStatement();)
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");

            pstmtUpdate.setString(1, operationBaseController.nrInventarTextField.getText());
            pstmtUpdate.setString(2, operationBaseController.nrReceptieTextField.getText());
            pstmtUpdate.setString(3, operationBaseController.felDocumentTextField.getText());
            pstmtUpdate.setString(4, operationBaseController.nrDocumentTextField.getText());
            pstmtUpdate.setString(5, operationBaseController.dataOperatieiDatePicker.getValue().toString());
            pstmtUpdate.setString(6, felOperatiei);
            pstmtUpdate.setInt(7, actionsController.getSelectedOperatieData().getOperatieID());

            System.out.println(pstmtUpdate.toString());
            pstmtUpdate.executeUpdate();

            pstmtDeleteValues.setInt(1, actionsController.getSelectedOperatieData().getOperatieID());
            pstmtDeleteValues.executeUpdate();
            System.out.println(pstmtUpdate.toString());

            ///.................... insert values

            String updSql;

            for(ValueBarController vbc : getOperationBaseController().getValueBarControllers())
            {
                if (vbc.getValoareFaraTVATextField().getText() != null && !vbc.getValoareFaraTVATextField().getText().equals(""))   //if its not an empty valoare
                {
                    updSql = "insert into operatieValori (operatieID, procentTVAID, procentTVA, valoareFaraTVA, diferentaTVA)\n" +
                            "Values ("+ actionsController.getSelectedOperatieData().getOperatieID() +", \n" +
                            (vbc.getProcentTVAComboBox().getItems().indexOf(vbc.getProcentTVAComboBox().getValue()) + 1) + ", ";

                    if (vbc.getProcentTVAComboBox().getItems().indexOf(vbc.getProcentTVAComboBox().getValue()) <= 2)
                        updSql += vbc.getProcentTVAComboBox().getValue().toString();
                    else
                        updSql += "0";

                    updSql += ", " +
                            vbc.getValoareFaraTVATextField().getText() + ", ";

                    if (vbc.getDiferentaTVATextField().getText() == null || vbc.getDiferentaTVATextField().getText().equals(""))
                        updSql += "0";
                    else
                        updSql += vbc.getDiferentaTVATextField().getText();

                    updSql += ");";

                    System.out.println(updSql);
                    s.executeUpdate(updSql);
                }
            }
        }

        actionsController.setSelectedOperatieData(null);
    }

    public void stergereBaseInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.DELETE_OPERATION_SQL))
        {
            pstmt.setInt(1, actionsController.getSelectedOperatieData().getOperatieID());

            pstmt.executeUpdate();


        }
    }

    public boolean validateInputForModificare() throws SQLException
    {
        if (actionsController.getSelectedOperatieData() == null)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.OPERATIE_NOT_SELECTED_HEADER, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        return operationBaseController.validateBaseInputForAdaugare();
    }

    public boolean validateInputForStergere()
    {
        if (actionsController.getSelectedOperatieData() == null)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.OPERATIE_NOT_SELECTED_HEADER, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }
        return true;
    }

    public String getFelOperatiei() {
        return felOperatiei;
    }

    public void setFelOperatiei(String felOperatiei) {
        this.felOperatiei = felOperatiei;
    }

    public void setActionsController(ActionsController actionsController) {
        this.actionsController = actionsController;
    }
}
