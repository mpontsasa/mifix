import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.sql.*;
import java.time.LocalDate;

public class AmortizareController {

    private ActionsController actionsController;

    @FXML
    CheckBox pentruToateCheckBox;

    @FXML
    Label nrInventarLabel;

    @FXML
    TextField nrInventarTextField;

    @FXML
    Label monthsTitleLable;

    @FXML
    TextField endYearTextField;

    @FXML
    TextField endMonthTextField;

    @FXML
    TextField startYearTextField;

    @FXML
    TextField startMonthTextField;

    @FXML
    Label intervalumLabel;

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
            if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.CALCULARE_OP))
            {
                if (validateInputForCalculare()) {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    if (!pentruToateCheckBox.isSelected())
                    {
                        LocalDate endDate = LocalDate.parse(startYearTextField.getText() + "-" + ((startMonthTextField.getText().length() < 2) ? "0" : "") + startMonthTextField.getText() + "-" +  "01");
                        calculateInDatabaseForOne(nrInventarTextField.getText(), endDate, c);
                    }
                    else
                    {
                        LocalDate endDate = LocalDate.parse(startYearTextField.getText() + "-" + ((startMonthTextField.getText().length() < 2) ? "0" : "") + startMonthTextField.getText() + "-" +  "01");
                        calculareInDatabase(c, endDate);
                    }

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    //SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.RECALCULARE_OP))
            {
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
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.INCHEIERE_OP))
            {
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
            }
        }
        catch (SQLException e) {
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
                startMonthTextField.getText() == null || startMonthTextField.getText().isEmpty())
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DATE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (endYearTextField.getText() == null || endYearTextField.getText().isEmpty() ||
                endMonthTextField.getText() == null || endMonthTextField.getText().isEmpty())
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DATE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        return true;
    }

    public boolean validateInputForRecalculare() throws SQLException
    {
        return validateInputForCalculare();
        // NEEDS TO BE ASKED< IF ITS FOR SURE (confirmation alert)
    }

    public boolean validateInputForIncheiere() throws SQLException {

        if (startYearTextField.getText() == null || startYearTextField.getText().isEmpty() ||
                startMonthTextField.getText() == null || startMonthTextField.getText().isEmpty() ||
                endYearTextField.getText() == null || endYearTextField.getText().isEmpty() ||
                endMonthTextField.getText() == null || endMonthTextField.getText().isEmpty())
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DATE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }


        return true;
    }

    public void calculateInDatabaseForOne(String nrInv, LocalDate endDate, Connection c) throws SQLException
    {

        try (PreparedStatement insertPstm = c.prepareStatement(Finals.INSERT_INTO_AMORTIZARE_SQL);
             PreparedStatement getNrOfAmortizatMonthsPstm = c.prepareStatement(Finals.NR_OF_AMORTIZAT_MONTHS_SQL);
             PreparedStatement lastAmortizatMonthPstm = c.prepareStatement(Finals.LAST_AMORTIZATION_DATE_BY_NR_INV_SQL);
             PreparedStatement amortizareStartingDatePstm = c.prepareStatement(Finals.GET_AMORTIZATION_START_DATE_SQL);
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
                amortizareStartingDatePstm.setString(1,nrInv);
                ResultSet rs = amortizareStartingDatePstm.executeQuery();

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

    public void calculareInDatabase(Connection c, LocalDate endDate) throws SQLException {

        try (PreparedStatement getMifixsPstm = c.prepareStatement(Finals.GET_ALL_OPERATIE_OF_MIFIX_SQL);
             Statement s = c.createStatement()) {

            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

            ResultSet rs = s.executeQuery(Finals.SELECT_FROM_MIJLOC_FIX_SQL);

            while (rs.next())
            {
                calculateInDatabaseForOne(rs.getString("nrInventar"), endDate, c);
            }
        }
    }

    public void recalculareInDatabase(Connection c) throws SQLException
    {
        if (validateInputForCalculare())
        {
            try (Statement s = c.createStatement();
                 Statement s2 = c.createStatement();
                PreparedStatement setDiffsPstm = c.prepareStatement(Finals.SET_DIFF_BY_MFID_AND_MONTH_SQL))
            {
                s.executeUpdate(Finals.SET_QUOTES_SQL);
                s.executeUpdate("use \"" + Main.getSocietateActuala() + "\";");

                //.............................get the old differentes
                LocalDate startDate = LocalDate.parse(startYearTextField.getText() + "-" + ((startMonthTextField.getText().length() < 2) ? "0" : "") + startMonthTextField.getText() + "-" +  "01");

                String getOldDifSql = "Select mifixID, monthOfAmortizare, diferenta from amortizare " +
                        "where monthOfAmortizare >= DATE('"+ startDate.toString() +"') ";

                if (!pentruToateCheckBox.isSelected())
                {
                    getOldDifSql += "and mifixID = (select mifixID from mijlocFix where nrInventar = '" + nrInventarTextField.getText() + "')";
                }

                ResultSet oldDiffsRS = s2.executeQuery(getOldDifSql);

                //.......................................get the latest date

                ResultSet rs = s.executeQuery(Finals.LAST_AMORTIZATION_DATE_SQL);
                rs.next();

                LocalDate lastDate = LocalDate.parse(rs.getString("lastMonth"));

                //........................................delete the old values

                String deleteOldSql = "delete from amortizare " +
                        "where monthOfAmortizare >= DATE('"+ startDate.toString() +"') ";

                if (!pentruToateCheckBox.isSelected())
                {
                    getOldDifSql += "and mifixID = (select mifixID from mijlocFix where nrInventar = '" + nrInventarTextField.getText() + "')";
                }

                s.executeUpdate(deleteOldSql);

                //....................set up the old differentes

                while(oldDiffsRS.next())
                {
                    setDiffsPstm.setString(1, oldDiffsRS.getString("diferenta"));
                    setDiffsPstm.setString(2, oldDiffsRS.getString("mifixID"));
                    setDiffsPstm.setString(3, oldDiffsRS.getString("monthOfAmortizare"));

                    setDiffsPstm.executeUpdate();
                }

                oldDiffsRS.close();
                rs.close();

            }
        }
    }

    public void incheiereInDB1Month(LocalDate month, Connection c) throws SQLException
    {
        try (PreparedStatement pstm = c.prepareStatement(Finals.INCHEIERE_1_MONTH_SQL))
        {
            if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "incheiereMonth", "monthIncheiat", month.toString()))   //if alredyn incheiat, we dont bother
            {
                pstm.setString(1, month.toString());
            }
        }
    }

    public void incheiereInDB(LocalDate month, Connection c) throws SQLException
    {

        LocalDate startDate = LocalDate.parse(startYearTextField.getText() + "-" + ((startMonthTextField.getText().length() < 2) ? "0" : "") + startMonthTextField.getText() + "-" +  "01");
        LocalDate endDate = LocalDate.parse(endYearTextField.getText() + "-" + ((endMonthTextField.getText().length() < 2) ? "0" : "") + endMonthTextField.getText() + "-" +  "01");

        for (;!startDate.isAfter(endDate); startDate = startDate.plusMonths(1))   //loop ower the months to
        {
            incheiereInDB1Month(startDate, c);
        }
    }

    public void deschidereInDB(Connection c) throws SQLException
    {
        LocalDate startDate = LocalDate.parse(startYearTextField.getText() + "-" + ((startMonthTextField.getText().length() < 2) ? "0" : "") + startMonthTextField.getText() + "-" +  "01");
        LocalDate endDate = LocalDate.parse(endYearTextField.getText() + "-" + ((endMonthTextField.getText().length() < 2) ? "0" : "") + endMonthTextField.getText() + "-" +  "01");

        try (PreparedStatement pstm = c.prepareStatement(Finals.DESCHIDERE_SQL))
        {
            pstm.setString(1,startDate.toString());
            pstm.setString(2,endDate.toString());

            pstm.executeUpdate();
        }
    }

    public void initialize()
    {
        startYearTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(4));
        startMonthTextField.addEventFilter(KeyEvent.KEY_TYPED , Util.numeric_Validation(2));
    }

    public void actionSelected()
    {
        if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.CALCULARE_OP))
        {
            monthsTitleLable.setText(Finals.WHAT_MONTHS_MEAN_CALC);
            intervalumLabel.setVisible(false);
            //endMonthTextField.setVisible(false);
            //endYearTextField.setVisible(false);
            //pentruToateCheckBox.setSelected(false);
            pentruToateCheckBox.setVisible(false);
            nrInventarTextField.setVisible(false);
            nrInventarLabel.setVisible(false);
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.RECALCULARE_OP))
        {
            monthsTitleLable.setText(Finals.WHAT_MONTHS_MEAN_RECALC);
            intervalumLabel.setVisible(false);
            //endMonthTextField.setVisible(false);
            //endYearTextField.setVisible(false);
            nrInventarTextField.setVisible(false);
            nrInventarLabel.setVisible(false);
            pentruToateCheckBox.setSelected(true);
            pentruToateCheckBox.setVisible(true);
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.INCHEIERE_OP))
        {
            monthsTitleLable.setText(Finals.WHAT_MONTHS_MEAN_INCH);
            intervalumLabel.setVisible(true);
            endMonthTextField.setVisible(true);
            endYearTextField.setVisible(true);
            nrInventarLabel.setVisible(false);
            nrInventarTextField.setVisible(false);
            nrInventarLabel.setVisible(false);
            pentruToateCheckBox.setSelected(true);
            pentruToateCheckBox.setVisible(false);
        }
    }
}
