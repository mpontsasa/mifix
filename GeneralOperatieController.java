import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

public class GeneralOperatieController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;


    @FXML
    public void executeButtonAction()
    {
        try
        {

            if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
            {
                adaugareBaseInDatabase();
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
            {
                modificareBaseInDatabase();
            }
            else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))
            {
                stergereBaseInDatabase();
            }

            OperatiuniTableInitializer.reload(getOperationBaseController().nrInventarTextField.getText());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    GeneralOperatieController(String felOperatioei, ActionsController actionsController)
    {
        this.actionsController = actionsController;
        setFelOperatiei(felOperatioei);
        setActionsController(actionsController);
    }

    public void initialize()
    {
        initializeBase(sublayerHBox);
    }
}
