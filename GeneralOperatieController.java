import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class GeneralOperatieController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    ActionsController actionsController;

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

    GeneralOperatieController(String felOperatioei, ActionsController actionsController)
    {
        this.actionsController = actionsController;
        setFelOperatiei(felOperatioei);
    }

    public void initialize()
    {
        initializeBase(sublayerHBox);
    }
}
