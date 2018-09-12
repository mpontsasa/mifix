import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AmortizareController {

    ActionsController actionsController;

    @FXML
    CheckBox pentruToateCheckBox;

    @FXML
    Label nrInventarLabel;

    @FXML
    TextField nrInventarTextField;

    @FXML
    ComboBox startYearTextField;

    @FXML
    ComboBox startMonthTextField;

    @FXML
    ComboBox endYearTextField;

    @FXML
    ComboBox endMonthTextField;

    @FXML
    Button executareButton;

    @FXML
    public void pentruToateCheckBoxAction()
    {
        if (pentruToateCheckBox.isSelected())
        {
            nrInventarLabel.setVisible(false);
            nrInventarTextField.setVisible(false);
        }
        else
        {
            nrInventarLabel.setVisible(true);
            nrInventarTextField.setVisible(true);
        }
    }

    @FXML
    public void nrInventarTextFieldAction()
    {

    }

    @FXML
    public void startYearTextFieldAction()
    {}

    @FXML
    public void endYearTextFieldAction()
    {}

    @FXML
    public void startMonthTextFieldAction()
    {}

    @FXML
    public void endMonthTextFieldAction()
    {}

    @FXML
    public void executareButtonAction()
    {}


    public AmortizareController(ActionsController actionsController) {
        this.actionsController = actionsController;
    }


}
