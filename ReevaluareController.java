import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ReevaluareController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    @FXML
    TextField valoareNouTextField;

    private ActionsController actionsController;

    @FXML
    public void executeButtonAction()
    {
        if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
        {
            adaugareInDatabase();
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
        {
            modificareInDatabase(actionsController.getSelectedOperatieData());
        }
        else if (actionsController.selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))
        {

            stergereInDatabase(actionsController.getSelectedOperatieData());
        }
    }

    ReevaluareController(ActionsController actionsController)
    {
        this.actionsController = actionsController;
    }

    public void initialize()
    {
        initializeBase(sublayerHBox);
        getOperationBaseController().baseOperationLayerHBox.getChildren().remove(getOperationBaseController().getValueInputVBox());
    }
}
