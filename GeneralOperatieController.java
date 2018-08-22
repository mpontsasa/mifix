import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class GeneralOperatieController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    ComboBox actiuneComboBox;

    @FXML
    public void executeButtonAction()
    {
        if (actiuneComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))
        {
            adaugareInDatabase();
        }
        else if (actiuneComboBox.getValue().toString().equals(Finals.MODIFICARE_OP))
        {
            modificareInDatabase();
        }
        else if (actiuneComboBox.getValue().toString().equals(Finals.STERGERE_OP))
        {
            stergereInDatabase();
        }
    }

    GeneralOperatieController(String felOperatioei, ComboBox actiuneComboBox)
    {
        this.actiuneComboBox = actiuneComboBox;
        setFelOperatiei(felOperatioei);
    }

    public void initialize()
    {
        initializeBase(sublayerHBox);
    }
}
