import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import static javafx.application.Platform.exit;

public class Alerts {
    static public void setUpCommonDataAlert() throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Finals.COMMON_DATABASE_NOT_SET_UP_TITLE_TEXT);
        alert.setHeaderText(Finals.COMMON_DATABASE_NOT_SET_UP_HEADER_TEXT);
        alert.setContentText(Finals.COMMON_DATABASE_NOT_SET_UP_CONTENT_TEXT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("Setting up commonDataDB");
            SQLExecuter.executeFile("setUpCommonDataDB.sql");
            System.out.println("commonDataDB set up finished");
        } else {
            exit();
        }
    }

    static public void errorAlert(String title, String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    static public boolean confirmationAlert(String title, String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    static public boolean inapoiButtonAlert() throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Finals.INAPOI_BUTTON_TITLE_TEXT);
        alert.setHeaderText(Finals.INAPOI_BUTTON_HEADER_TEXT);
        alert.setContentText(Finals.INAPOI_BUTTON_CONTENT_TEXT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    static public void informationAlert(String title, String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

}
