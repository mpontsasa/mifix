import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class VanzareController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    @FXML
    public void executeButtonAction()
    {
        adaugareBaseInDatabase();
    }

    public void initialize()
    {
        initializeBase(sublayerHBox, "vanzare");
    }
}
