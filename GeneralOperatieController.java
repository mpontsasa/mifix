import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class GeneralOperatieController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;


    @FXML
    public void executeButtonAction()
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
