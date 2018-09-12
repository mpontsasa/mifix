import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


public class SuspendareController {

    ActionsController actionsController;

    @FXML
    TextField nrInventarTextField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;

    @FXML
    Button executareButton;

    @FXML
    public void executareButtonAction()
    {
        try
        {
            if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
            {
                if (validateInputForAdaugare())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    adaugareInDatabase(c);


                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
            {
                if (validateInputForModificare())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    modificareInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))
            {
                if (validateInputForAdaugare())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    stergereInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
                    s.close();
                    c.close();

                    SuspendariTableInitializer.reload(nrInventarTextField.getText());
                    Alerts.informationAlert(Finals.SUCCESSFUL_OPERATION_TITLE_TEXT, Finals.SUCCESSFUL_OPERATION_HEADER_TEXT, Finals.SUCCESSFUL_OPERATION_CONTENT_TEXT);
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    SuspendareController(ActionsController actionsController)
    {
        this.actionsController = actionsController;
    }

    public boolean validateInputForAdaugare() throws SQLException
    {
        //...........................validate nrInventar
        if (nrInventarTextField.getText() == null || nrInventarTextField.getText().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if(!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", nrInventarTextField.getText()))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }
        //.............................validate dates

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.DATE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }
        return true;
    }

    public boolean validateInputForModificare() throws SQLException
    {
        if (actionsController.getSelectedSuspendareData() == null)
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.OPERATIE_NOT_SELECTED_HEADER, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }
        return true;
    }

    public void adaugareInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.INSERT_IN_SUSPENDARE_SQL);
             Statement s = c.createStatement();)
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");


            pstmt.setString(1, nrInventarTextField.getText());
            pstmt.setString(2, startDatePicker.getValue().toString());
            pstmt.setString(3, endDatePicker.getValue().toString());

            pstmt.executeUpdate();
        }
    }

    public void modificareInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.UPDATE_SUSPENDARE_SQL);
             Statement s = c.createStatement())
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");


            pstmt.setString(1, nrInventarTextField.getText());
            pstmt.setString(2, startDatePicker.getValue().toString());
            pstmt.setString(3, endDatePicker.getValue().toString());
            pstmt.setInt(4, actionsController.getSelectedSuspendareData().getOperatieID());

            System.out.println(pstmt.toString());

            pstmt.executeUpdate();

        }
    }

    public void stergereInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.DELETE_SUSPENDARE_SQL);
             Statement s = c.createStatement())
        {
            s.executeUpdate(Finals.SET_QUOTES_SQL);
            s.executeUpdate("use \"" + Main.getSocietateActuala() +"\";");

            pstmt.setInt(1, actionsController.getSelectedSuspendareData().getOperatieID());

            System.out.println(pstmt.toString());

            pstmt.executeUpdate();

        }
    }

    public void mijlocFixSelectedInTable()
    {
        nrInventarTextField.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getNrInventar());
    }

    public void suspendareSelectedInTable(OperatiuniTableDisplayer<SuspendariTableInitializer.SuspendareData> std)
    {

        nrInventarTextField.setText(std.getTable().getSelectionModel().getSelectedItem().getNrInventar());
        startDatePicker.setValue(LocalDate.parse(std.getTable().getSelectionModel().getSelectedItem().getStartDate()));
        endDatePicker.setValue(LocalDate.parse(std.getTable().getSelectionModel().getSelectedItem().getEndDate()));
    }
}
