import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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

        if (Integer.parseInt(startYearTextField.getText()) < Integer.parseInt(endYearTextField.getText()) ||
                (Integer.parseInt(startYearTextField.getText()) == Integer.parseInt(endYearTextField.getText()) &&
                        Integer.parseInt(startMonthTextField.getText()) < Integer.parseInt(endMonthTextField.getText())))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.START_DATE_AFTER_END_HEADER, Finals.INVALID_INPUT_CONTENT_TEXT);
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

    public void calculateInDatabaseForOne(String nrInv, Connection c) throws SQLException
    {
        LocalDate dateOfAmort = LocalDate.parse(startYearTextField.getText() + "-" + startMonthTextField.getText() + "-" +  "01");
        LocalDate endDate = LocalDate.parse(endYearTextField.getText() + "-" + endMonthTextField.getText() + "-" +  "01");


        try (PreparedStatement pstm = c.prepareStatement(Finals.INSERT_INTO_AMORTIZARE_SQL);
             Statement s = c.createStatement())
        {
            for (;!dateOfAmort.isAfter(endDate); dateOfAmort = dateOfAmort.plusMonths(1))
            {
                if (!MySQLJDBCUtil.isSuspended(nrInv, dateOfAmort, c) && !MySQLJDBCUtil.isAmortizat(nrInv, dateOfAmort, c))
                {
                    Float valueOfMifix = MySQLJDBCUtil.valueOfMifixAtADate(nrInv, dateOfAmort, c);

                    if (valueOfMifix == null)   // has no operations yet or alredy sold/casat
                    {
                        return;
                    }

                    Float amortizareValue = valueOfMifix - MySQLJDBCUtil.valueAmortizata(nrInv, dateOfAmort, c);

                    pstm.setString(1, nrInv);
                    pstm.setString(2, dateOfAmort.toString());
                    pstm.setFloat(3, amortizareValue);
                    pstm.setFloat(4, 0f);

                    pstm.executeUpdate();

                }
            }
        }
    }

    public void calculareInDatabase(Connection c) throws SQLException {

        try (PreparedStatement getMifixsPstm = c.prepareStatement(Finals.GET_ALL_OPERATIE_OF_MIFIX_SQL);
             Statement s = c.createStatement()) {

            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");




        }
    }
    public void recalculareInDatabase(Connection c) throws SQLException {

    }

    public void stergereInDatabase(Connection c) throws SQLException {

    }

    public void initialize()
    {
        startYearTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(4));
        startMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(2));
        endYearTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(4));
        endMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(2));
    }
}
