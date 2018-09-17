import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.sql.*;
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

                    if (!pentruToateCheckBox.isSelected())
                    {
                        calculateInDatabaseForOne(nrInventarTextField.getText(), c);
                    }
                    else
                    {
                        calculareInDatabase(c);
                    }

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

        if (endYearTextField.getText() == null || endYearTextField.getText().isEmpty() ||
                endMonthTextField.getText() == null || endMonthTextField.getText().isEmpty())
        {
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

    public void calculateInDatabaseForOne(String nrInv, Connection c) throws SQLException
    {

        LocalDate endDate = LocalDate.parse(endYearTextField.getText() + "-" + ((endMonthTextField.getText().length() < 2) ? "0" : "") + endMonthTextField.getText() + "-" +  "01");

        try (PreparedStatement insertPstm = c.prepareStatement(Finals.INSERT_INTO_AMORTIZARE_SQL);
             PreparedStatement getNrOfAmortizatMonthsPstm = c.prepareStatement(Finals.NR_OF_AMORTIZAT_MONTHS_SQL);
             PreparedStatement lastAmortizatMonthPstm = c.prepareStatement(Finals.LAST_AMORTIZATION_DATE_SQL);
             PreparedStatement amortizareStartingdatePstm = c.prepareStatement(Finals.GET_AMORTIZATION_START_DATE_SQL);
             Statement s = c.createStatement())
        {

            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            LocalDate lastAmortizatDate;

            lastAmortizatMonthPstm.setString(1,nrInv);
            ResultSet lastAmDateRS = lastAmortizatMonthPstm.executeQuery();
            if (lastAmDateRS.next() && lastAmDateRS.getString("lastMonth") != null)    //if it was amortizat before
            {
                lastAmortizatDate = LocalDate.parse(lastAmDateRS.getString("lastMonth"));     // I get the last mont that was amortizat
                lastAmortizatDate = lastAmortizatDate.plusMonths(1);
            }
            else    // if it wasnt amortizat yet, we set the inceputul amortizarii to start from
            {
                amortizareStartingdatePstm.setString(1,nrInv);
                ResultSet rs = amortizareStartingdatePstm.executeQuery();

                rs.next();
                lastAmortizatDate = LocalDate.parse(rs.getString("inceputulAmortizarii"));
            }

            for (LocalDate dateOfAmort = LocalDate.parse(lastAmortizatDate.toString());!dateOfAmort.isAfter(endDate); dateOfAmort = dateOfAmort.plusMonths(1))   //loop ower the months to amirtiza with dateOfAmort
            {
                if (!MySQLJDBCUtil.isSuspended(nrInv, dateOfAmort, c)) //if not suspended
                {
                    Float valueOfMifix = MySQLJDBCUtil.valueOfMifixAtADate(nrInv, dateOfAmort, c);

                    if (valueOfMifix == null || valueOfMifix.equals(0))   // has no operations yet or alredy sold/casat
                    {
                        return;
                    }

                    getNrOfAmortizatMonthsPstm.setString(1, nrInv);

                    ResultSet rs = getNrOfAmortizatMonthsPstm.executeQuery();
                    rs.next();
                    int monthsAmortizat = rs.getInt("monthCount");
                    int monthsLeft = rs.getInt("durataAmortizarii") * 12 - monthsAmortizat;

                    Float amortizareValue = (valueOfMifix - MySQLJDBCUtil.valueAmortizata(nrInv, dateOfAmort, c))/monthsLeft;

                    insertPstm.setString(1, nrInv);
                    insertPstm.setString(2, dateOfAmort.toString());
                    insertPstm.setFloat(3, amortizareValue);
                    insertPstm.setFloat(4, 0f);

                    System.out.println(insertPstm.toString());

                    insertPstm.executeUpdate();
                }
            }
        }
    }

    public void calculareInDatabase(Connection c) throws SQLException {

        try (PreparedStatement getMifixsPstm = c.prepareStatement(Finals.GET_ALL_OPERATIE_OF_MIFIX_SQL);
             Statement s = c.createStatement()) {

            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            ResultSet rs = s.executeQuery(Finals.SELECT_FROM_MIJLOC_FIX_SQL);

            while (rs.next())
            {
                calculateInDatabaseForOne(rs.getString("nrInventar"), c);
            }
        }
    }

    public void recalculareInDatabase(Connection c) throws SQLException {

    }

    public void stergereInDatabase(Connection c) throws SQLException {

    }

    public void initialize()
    {
        endYearTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(4));
        endMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(2));
    }
}
