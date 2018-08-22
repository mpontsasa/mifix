import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AchizitieController extends OperationController {

    @FXML
    HBox sublayerHBox;

    @FXML
    Button executeButton;

    @FXML
    public void executeButtonAction()
    {
        adaugareInDatabase();
    }

    public void initialize()
    {
        initializeBase(sublayerHBox, "achizitie");
    }
}
