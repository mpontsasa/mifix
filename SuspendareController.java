import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;


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

        if(MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", nrInventarTextField.getText()))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
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
}
