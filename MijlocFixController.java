import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


public class MijlocFixController {

    Main main;

    ArrayList<ClasificariRecord> clasificari;
    ClasificariRecord selectedClasificare = null;
    boolean dirtiFlag = false;

    @FXML
    TextField numarInventarTextField;

    @FXML
    ComboBox codDeClasificareComboBox;

    @FXML
    TextArea descriptieClasificareTextArea;

    @FXML
    DatePicker termenDeGarantieDatePicker;

    @FXML
    Label durataHelper;

    @FXML
    TextField durataAmortizariiTextField;

    @FXML
    ComboBox regimDeAmortizareComboBox;

    @FXML
    Button adaugareButton;

    @FXML
    TextArea mijlocFixSiCaracteristiciTextArea;

    @FXML
    TextField contDebitorTextField;

    @FXML
    TextField contCreditorTextField;

    ActionsController actionsController;

    @FXML
    public void codDeClasificareComboBoxAction()
    {
        dirtiFlag = true;
        if (codDeClasificareComboBox.getValue() == null || codDeClasificareComboBox.getValue().equals(""))
        {
            descriptieClasificareTextArea.setText("");
            durataHelper.setText("-");
            selectedClasificare = null;
            return;
        }

        for (ClasificariRecord cr : clasificari)
        {
            if (cr.getCod().equals(codDeClasificareComboBox.getValue()))
            {
                descriptieClasificareTextArea.setStyle("-fx-text-fill: black;");
                descriptieClasificareTextArea.setText(cr.getDescription());
                selectedClasificare = cr;
                setUpDurataHelperLabel(cr.getMinDur(), cr.getMaxDur());
                durataAmortizoriiTextFieldAction();

                return;
            }
        }

        descriptieClasificareTextArea.setStyle("-fx-text-fill: red;");
        descriptieClasificareTextArea.setText("Cod de clasificare incorect!");
        durataHelper.setText("-");
        selectedClasificare = null;
    }

    @FXML
    public void codDeClasificareComboBoxKeyTyped() throws Exception
    {
        codDeClasificareComboBox.getItems().clear();
        for (ClasificariRecord cr : clasificari)
        {
            if (codDeClasificareComboBox.getEditor().getText() == null || cr.getCod().startsWith(codDeClasificareComboBox.getEditor().getText()))
            {
                codDeClasificareComboBox.getItems().add(cr.getCod());
            }
        }
    }

    @FXML
    public void durataAmortizoriiTextFieldAction()
    {
        dirtiFlag = true;
        int nr;
        try
        {
            nr = Integer.parseInt(durataAmortizariiTextField.getText());
        }
        catch (NumberFormatException nfe)
        {
            durataAmortizariiTextField.setText("");     //if its not a number
            return;
        }

        if (selectedClasificare != null)
        {

            if (nr < selectedClasificare.getMinDur())
            {
                durataAmortizariiTextField.setText("");
                //durataAmortizariiTextField.setText("" + selectedClasificare.getMinDur());
            }
            else if (nr > selectedClasificare.getMaxDur())
            {
                durataAmortizariiTextField.setText("");
                //durataAmortizariiTextField.setText("" + selectedClasificare.getMaxDur());
            }
        }
    }

    @FXML
    public void nrInventarTextFieldAtion()
    {
        dirtiFlag = true;
    }

    @FXML
    public void contDebitorTextFieldAtion()
    {
        dirtiFlag = true;
    }

    @FXML
    public void contCreditorTextFieldAtion()
    {
        dirtiFlag = true;
    }

    @FXML
    public void regimDeAmortizareComboBoxAction()
    {
        dirtiFlag = true;
    }

    @FXML
    public void mijlocFixSiCaracteristiciTextAreaAction()
    {
        dirtiFlag = true;
    }

    @FXML
    public void setTermenDeGarantieDatePickerAction()
    {
        dirtiFlag = true;
    }

    @FXML
    public void executareButtonAction()
    {
        if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
        {
            adaugareInDatabase();
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
        {
            modificareInDatabase();
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))
        {
            stergereInDatabase();
        }
    }

    @FXML
    public void inapoiButtonAction() throws Exception
    {
        if (!dirtiFlag || Alerts.inapoiButtonAlert())    //if no changes where made or user wants to back anyway
        {
            main.changeToActionsView();
        }
    }

    public void adaugareInDatabase()
    {
        try
        {
            if (validateInputForAdaugare())
            {
                Connection c = MySQLJDBCUtil.getConnection();
                Statement s = c.createStatement();
                s.executeUpdate(Finals.SET_QUOTES_SQL);
                s.executeUpdate("use \"" + main.getSocietateActuala() +"\";");

                String insSql = "insert into mijlocFix"+
                        "(nrInventar,mifixSiCaracteristiceTechnice, clasificare, durataAmortizarii, regimDeAmortizare, termenDeGarantie, contDebitor, contCreditor ) VALUES ('" +
                        numarInventarTextField.getText() + "', '"+
                        mijlocFixSiCaracteristiciTextArea.getText() + "', '"+
                        codDeClasificareComboBox.getValue().toString() + "', '"+
                        durataAmortizariiTextField.getText() + "', '" +
                        regimDeAmortizareComboBox.getValue().toString() + "', ";

                if (termenDeGarantieDatePicker.getValue() == null)  //termen de garantie might be null
                    insSql += "null, ";
                else
                    insSql += "'"+
                            termenDeGarantieDatePicker.getValue().toString() + "', ";


                if (contDebitorTextField.getText() != null && !contDebitorTextField.getText().equals(""))
                {
                    insSql += "'" + contDebitorTextField.getText() + "', ";
                }
                else
                {
                    insSql += "null, ";
                }

                if (contCreditorTextField.getText() != null && !contCreditorTextField.getText().equals(""))
                {
                    insSql += "'" + contCreditorTextField.getText() + "');";
                }
                else
                {
                    insSql += "null);";
                }

                System.out.println(insSql);
                s.executeUpdate(insSql);

                s.close();
                c.close();
                dirtiFlag = false;
                MijlocFixTableInitializer.reload();
                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            //System.out.println("SQL EXEPTION adaugareAction.");
            // make an error alert
        }
    }

    public void modificareInDatabase()
    {
        try
        {
            //if nr inventar exists
            if(MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", numarInventarTextField.getText()))
            {
                if (validateInputForModificare() &&
                        Alerts.confirmationAlert(Finals.MODIFICARE_HEADER_TEXT, Finals.MODIFICARE_MIJLOC_FIX_TITLE_TEXT, "Sunteti sigur ca modificati mijloc fix cu nr inventar: \n" + numarInventarTextField.getText() + "  ?"))
                {
                    MySQLJDBCUtil.updateMifix(numarInventarTextField.getText(),
                            numarInventarTextField.getText(),
                            mijlocFixSiCaracteristiciTextArea.getText(),
                            codDeClasificareComboBox.getValue().toString(),
                            Integer.parseInt(durataAmortizariiTextField.getText()),
                            regimDeAmortizareComboBox.getValue().toString(),
                            termenDeGarantieDatePicker.getValue(),
                            contDebitorTextField.getText(),
                            contCreditorTextField.getText());

                    MijlocFixTableInitializer.reload();
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);

                }

            }// if nrInventar doesent exist
            else
            {
                // if mijloc fix isnt selected
                if (actionsController.selectedNrInventarTextBox.getText() == null || actionsController.selectedNrInventarTextBox.getText().equals(""))
                {
                    Alerts.informationAlert(Finals.MIFIX_NOt_SELECtED_TEXT, Finals.MIFIX_NOt_SELECtED_TEXT, "");
                }   // if mifix is selected but it doesent exists
                else if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", actionsController.selectedNrInventarTextBox.getText())) {
                    Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.SELECTED_NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT, "Nr. inventar selectat: " + actionsController.selectedNrInventarTextBox.getText());

                }//if selected nr inventar is all right
                else if (validateInputForModificare() &&
                        Alerts.confirmationAlert(Finals.MODIFICARE_HEADER_TEXT, Finals.MODIFICARE_MIJLOC_FIX_TITLE_TEXT, "Sunteti sigur ca modificati numar inventar " +actionsController.selectedNrInventarTextBox.getText()+ " la " + numarInventarTextField.getText() + "  ?"))
                {
                    MySQLJDBCUtil.updateMifix(actionsController.selectedNrInventarTextBox.getText(),
                            numarInventarTextField.getText(),
                            mijlocFixSiCaracteristiciTextArea.getText(),
                            codDeClasificareComboBox.getValue().toString(),
                            Integer.parseInt(durataAmortizariiTextField.getText()),
                            regimDeAmortizareComboBox.getValue().toString(),
                            termenDeGarantieDatePicker.getValue(),
                            contDebitorTextField.getText(),
                            contCreditorTextField.getText());

                    MijlocFixTableInitializer.reload();
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);

                }


            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void stergereInDatabase()
    {
        try {
            //checking if nrinventar empty
            if (numarInventarTextField.getText() == null || numarInventarTextField.getText().equals("")) {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return;
            }
            //check if nrInventar exists
            if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", numarInventarTextField.getText())) {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return;
            }   // if has no operatiuni or we can delete the operatiuni
            else if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "operatieBase", "mifixID", numarInventarTextField.getText()) ||
                    Alerts.confirmationAlert(Finals.MIFIX_TO_DELETE_HAS_OPERATIONS_TITLE_TEXT, Finals.MIFIX_TO_DELETE_HAS_OPERATIONS_HEADER_TEXT, Finals.MIFIX_TO_DELETE_HAS_OPERATIONS_CONTENT_TEXT))
            {
                Connection c = MySQLJDBCUtil.getConnection();
                Statement s = c.createStatement();
                s.executeUpdate(Finals.SET_QUOTES_SQL);
                s.executeUpdate("use \"" + main.getSocietateActuala() + "\";");

                String insSql = "delete from mijlocFix where nrInventar = '" + numarInventarTextField.getText() + "'";

                System.out.println(insSql);
                s.executeUpdate(insSql);

                s.close();
                c.close();
                dirtiFlag = false;
                MijlocFixTableInitializer.reload();
                Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public MijlocFixController(ActionsController actionsController) {
        this.actionsController = actionsController;
    }

    public void initialize() throws Exception
    {

        dirtiFlag = false;  //initialize dirty flag
        //............................................................................set up clasificari
        Connection c = MySQLJDBCUtil.getConnection(Finals.COMMON_DATABASE_NAME);    //get the connection
        Statement st = c.createStatement();                                         //make a statement
        ResultSet rs = st.executeQuery(Finals.SELECT_FROM_CLASIFICARI_SQL);         //get the clasificari

        clasificari = new ArrayList<>();                                            //put the classificari into araylist

        while(rs.next())
        {
            clasificari.add(new ClasificariRecord(rs.getString("cod"), rs.getString("description"), rs.getInt("minDur"), rs.getInt("maxDur")));
        }

        rs.close();
        st.close();

        codDeClasificareComboBoxKeyTyped(); // this will fill the combobox

        //...........................................................................set up durata amortizarii

        durataAmortizariiTextField.focusedProperty().addListener(new ChangeListener<Boolean>()  //adding change focus listener
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    //System.out.println("Textfield on focus");
                }
                else
                {
                    //System.out.println("Textfield out focus");
                    durataAmortizoriiTextFieldAction();
                }
            }
        });

        //...........................................................................set upregim de amortizare

        //c = MySQLJDBCUtil.getConnection(Finals.COMMON_DATABASE_NAME);    //get the connection (alredy done)
        st = c.createStatement();                                         //make a statement
        rs = st.executeQuery(Finals.SELECT_FROM_REGIMI_DE_AMORTIZARE_SQL);         //get the regimi

        while (rs.next())
        {
            regimDeAmortizareComboBox.getItems().add(rs.getString("denumire"));
        }

        rs.close();
        st.close();


        //..............................v.............................................set up regim de amortizare

        regimDeAmortizareComboBox.setValue(regimDeAmortizareComboBox.getItems().get(0));


        // ......
        c.close();
    }

    public boolean validateInputForAdaugare() throws SQLException
    {
        //validation of nrInventar

        if (numarInventarTextField.getText() == null || numarInventarTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if(MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", numarInventarTextField.getText()))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //validare cod de clasificare
        if (codDeClasificareComboBox.getValue() == null || codDeClasificareComboBox.getValue().toString().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.COD_DE_CLASIFICARE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (!codDeClasificareComboBox.getItems().contains(codDeClasificareComboBox.getValue()))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.COD_DE_CLASIFICARE_INCORECT_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //validation of durata amortizarii

        if (durataAmortizariiTextField.getText()== null || durataAmortizariiTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DURATA_AMORTIZARII_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        // validation of mifix si caracteristice technice
        if (mijlocFixSiCaracteristiciTextArea.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.MI_FIX_SI_CAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (mijlocFixSiCaracteristiciTextArea.getText().length() > 500)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.MI_FIX_SI_CAR_TOO_LONG_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        // validation of cont creditor

        /*if (contCreditorTextField.getText()== null || contCreditorTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.CONT_CREDITOR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }*/

        if (contCreditorTextField.getText()!= null && !contCreditorTextField.getText().equals(""))
        {

            String contDBVersion = DbfAccess.contExists(contCreditorTextField.getText());   // get the database version of the string if exists
            if (contDBVersion == null)  //if it doesent exists
            {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.INEXISTENT_CONT_CREDITOR_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }
            else
            {
                contCreditorTextField.setText(contDBVersion);   //lets use the database version
            }
        }

        // validation of cont debitor

        /*if (contDebitorTextField.getText()== null || contDebitorTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.CONT_DEBITOR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }*/

        if (contDebitorTextField.getText()!= null &&! contDebitorTextField.getText().equals(""))
        {

            String contDBVersion = DbfAccess.contExists(contDebitorTextField.getText());   // get the database version of the string if exists
            if (contDBVersion == null)  //if it doesent exists
            {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.INEXISTENT_CONT_DEBITOR_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }
            else
            {
                contDebitorTextField.setText(contDBVersion);   //lets use the database version
            }

        }
        //..............

        return true;
    }

    public boolean validateInputForModificare() throws SQLException
    {
        //validation of nrInventar

        if (numarInventarTextField.getText() == null || numarInventarTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //validare cod de clasificare
        if (codDeClasificareComboBox.getValue() == null || codDeClasificareComboBox.getValue().toString().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.COD_DE_CLASIFICARE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (!codDeClasificareComboBox.getItems().contains(codDeClasificareComboBox.getValue()))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.COD_DE_CLASIFICARE_INCORECT_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //validation of durata amortizarii

        if (durataAmortizariiTextField.getText()== null || durataAmortizariiTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DURATA_AMORTIZARII_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        // validation of mifix si caracteristice technice
        if (mijlocFixSiCaracteristiciTextArea.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.MI_FIX_SI_CAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (mijlocFixSiCaracteristiciTextArea.getText().length() > 500)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.MI_FIX_SI_CAR_TOO_LONG_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        // validation of cont creditor

        /*if (contCreditorTextField.getText()== null || contCreditorTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.CONT_CREDITOR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }*/

        if (contCreditorTextField.getText()!= null && !contCreditorTextField.getText().equals(""))
        {

            String contDBVersion = DbfAccess.contExists(contCreditorTextField.getText());   // get the database version of the string if exists
            if (contDBVersion == null)  //if it doesent exists
            {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.INEXISTENT_CONT_CREDITOR_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }
            else
            {
                contCreditorTextField.setText(contDBVersion);   //lets use the database version
            }
        }

        // validation of cont debitor

        /*if (contDebitorTextField.getText()== null || contDebitorTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.CONT_DEBITOR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }*/

        if (contDebitorTextField.getText()!= null &&! contDebitorTextField.getText().equals(""))
        {

            String contDBVersion = DbfAccess.contExists(contDebitorTextField.getText());   // get the database version of the string if exists
            if (contDBVersion == null)  //if it doesent exists
            {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.INEXISTENT_CONT_DEBITOR_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }
            else
            {
                contDebitorTextField.setText(contDBVersion);   //lets use the database version
            }

        }
        //..............

        return true;
    }

    public void setUpDurataHelperLabel(int minDur, int maxDur)
    {
        durataHelper.setText("" + minDur + " - " + maxDur + " ani");
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void mijlocFixSelectedInTable()
    {
        numarInventarTextField.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getNrInventar());
        durataAmortizariiTextField.setText("" + MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getDurataAmortizarii());
        contDebitorTextField.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getContDebitor());
        contCreditorTextField.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getContCreditor());
        mijlocFixSiCaracteristiciTextArea.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getMifixSiCar());
        codDeClasificareComboBox.setValue("" + MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getClasificare().toString());
        regimDeAmortizareComboBox.setValue("" + MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getRegimDeAmortizare().toString());

        if (MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getTermenDeGarantie() != null)
            termenDeGarantieDatePicker.setValue(LocalDate.parse(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getTermenDeGarantie()));
    }

    public void clasificareSelectedInTable()
    {
        codDeClasificareComboBox.setValue(ClasificariTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getCod());
    }
}
