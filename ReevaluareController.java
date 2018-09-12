import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ReevaluareController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    @FXML
    TextField valoareNouTextField;

    private ActionsController actionsController;

    @FXML
    public void valoareNouTextFieldAction()
    {

        if (valoareNouTextField.getText() == null || valoareNouTextField.getText() .equals(""))
            return;

        try
        {
            valoareNouTextField.setText("" + Util.round(Float.parseFloat(valoareNouTextField.getText()), 2));
        }
        catch (NumberFormatException e)
        {
            valoareNouTextField.setText("");
        }
    }

    @FXML
    public void executeButtonAction()
    {
        if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
        {

            try
            {
                if (getOperationBaseController().validateBaseInputForAdaugare() && validateReevaluareInput())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    adaugareBaseInDatabase(c);
                    adaugareReevaluareInDB(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
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

        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
        {


            try
            {
                if (validateInputForStergere())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    modificareBaseInDatabase(c);
                    modificareReevaluareInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
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


        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))
        {
            try
            {
                if (getOperationBaseController().validateBaseInputForAdaugare() && validateReevaluareInput())
                {
                    Connection c = MySQLJDBCUtil.getConnection();
                    Statement s = c.createStatement();
                    s.execute(Finals.START_TRANSACTION);

                    stergereBaseInDatabase(c);
                    stergereReevaluareInDatabase(c);

                    s.execute(Finals.COMMIT_TRANSACTION);
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
        }
        try
        {

            OperatiuniTableInitializer.reload(getOperationBaseController().nrInventarTextField.getText());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean validateReevaluareInput()
    {
        if (valoareNouTextField.getText() == null || valoareNouTextField.getText().equals("")) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, "Valoare nu poate ramane gol!", Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }
        return true;
    }

    public void adaugareReevaluareInDB(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.INSERT_IN_REEVALUARE_SQL))
        {
            pstmt.setString(1, valoareNouTextField.getText());

            pstmt.executeUpdate();
        }
    }

    public void modificareReevaluareInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.MODIFICARE_REEVALUARE_SQL))
        {
            pstmt.setString(1, valoareNouTextField.getText());
            pstmt.setInt(2, actionsController.getSelectedOperatieData().getOperatieID());

            pstmt.executeUpdate();
        }
    }

    public void stergereReevaluareInDatabase(Connection c) throws SQLException
    {
        try (PreparedStatement pstmt = c.prepareStatement(Finals.DELETE_REEVALUARE_SQL))
        {
            pstmt.setInt(1, actionsController.getSelectedOperatieData().getOperatieID());

            pstmt.executeUpdate();
        }
    }

    ReevaluareController(ActionsController actionsController)
    {
        this.actionsController = actionsController;
        setFelOperatiei("reevaluare");
        setActionsController(actionsController);
    }

    public void initialize ()
    {
        initializeBase(sublayerHBox);
        getOperationBaseController().baseOperationLayerHBox.getChildren().remove(getOperationBaseController().getValueInputVBox());
    }

}
