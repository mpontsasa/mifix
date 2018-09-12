import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AmortizareController {

    ActionsController actionsController;

    @FXML
    CheckBox pentruToateCheckBox;

    @FXML
    Label nrInventarLabel;

    @FXML
    TextField nrInventarTextField;

    @FXML
    TextField startYearTextField;

    @FXML
    TextField startMonthTextField;

    @FXML
    TextField endYearTextField;

    @FXML
    TextField endMonthTextField;

    @FXML
    Button executareButton;

    @FXML
    public void pentruToateCheckBoxAction() {
        if (pentruToateCheckBox.isSelected()) {
            nrInventarLabel.setVisible(false);
            nrInventarTextField.setVisible(false);
        } else {
            nrInventarLabel.setVisible(true);
            nrInventarTextField.setVisible(true);
        }
    }

    @FXML
    public void nrInventarTextFieldAction() {

    }

    @FXML
    public void startYearTextFieldAction() {
    }

    @FXML
    public void endYearTextFieldAction() {
    }

    @FXML
    public void startMonthTextFieldAction() {
    }

    @FXML
    public void endMonthTextFieldAction() {
    }

    @FXML
    public void executareButtonAction() {
        try {
            if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.CALCULARE_OP)) {
                if (validateInputForCalculare()) {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    calculareInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    //SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            } else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP)) {
                if (validateInputForRecalculare()) {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    recalculareInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    //SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            } else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP)) {
                if (validateInputForStergere()) {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    stergereInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    //SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public AmortizareController(ActionsController actionsController) {
        this.actionsController = actionsController;
    }

    public boolean validateInputForCalculare() throws SQLException {
        if (!pentruToateCheckBox.isSelected()) {
            if (nrInventarTextField.getText() == null || nrInventarTextField.getText().equals("")) {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }

            if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", nrInventarTextField.getText())) {
                Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                return false;
            }
        }

        if (startYearTextField.getText() == null || startYearTextField.getText().isEmpty() ||
                endYearTextField.getText() == null || endYearTextField.getText().isEmpty() ||
                startMonthTextField.getText() == null || startMonthTextField.getText().isEmpty() ||
                endMonthTextField.getText() == null || endMonthTextField.getText().isEmpty()) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DATE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        return true;
    }

    public boolean validateInputForStergere() throws SQLException {
        //return validateInputForCalculare();
        return false;
        // check if is selected...
    }

    public boolean validateInputForRecalculare() throws SQLException
    {
        return validateInputForCalculare();
        // NEEDS TO BE ASKED< IF ITS FOR SURE (confirmation alert)
    }

    public void calculareInDatabase(Connection c) throws SQLException {

    }

    public void recalculareInDatabase(Connection c) throws SQLException {

    }

    public void stergereInDatabase(Connection c) throws SQLException {

    }

    public void initialize()
    {
        startYearTextField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(4));
        startMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(2));
        endYearTextField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(4));
        endMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(2));
    }

    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Lengh) {
                    e.consume();
                }
                if(e.getCharacter().matches("[0-9]")){
                    /*if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                        e.consume();
                    }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                        e.consume();
                    }*/
                }else{
                    e.consume();
                }

                if(max_Lengh == 2 && !(txt_TextField.getText() + e.getCharacter()).isEmpty())  //month
                {
                    try
                    {

                        if (Integer.parseInt(txt_TextField.getText() + e.getCharacter()) > 12 || Integer.parseInt(txt_TextField.getText() + e.getCharacter()) < 1)
                            e.consume();
                    }
                    catch(NumberFormatException numEx)
                    {
                        //hehhe, do nothing(this try-catch is for backspace)
                    }
                }

            }
        };
    }   // copy paste, I didnt make this (dont blame me for naming convention)

}
