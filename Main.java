
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;

public class Main extends Application {

    private static Stage globalPrimaryStage;
    private static String societateActuala = null;
    private static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage primaryStage) throws Exception{

        globalPrimaryStage = primaryStage;
        globalPrimaryStage.setTitle(Finals.MAIN_STAGE_CAPTION);

        try (FileInputStream f = new FileInputStream("db.properties")) {

            if (!MySQLJDBCUtil.databaseExists(Finals.COMMON_DATABASE_NAME)) {
                Alerts.setUpCommonDataAlert();
            }

            // load the properties file
            Properties pros = new Properties();
            pros.load(f);

            // assign db parameters
            String activeDB = pros.getProperty("activeDB");

            if (activeDB.equals("none"))
            {
                changeToSelectSocietateView();
            }
            else
            {
                if(MySQLJDBCUtil.setUpSocietateDB(activeDB))
                {
                    societateActuala = activeDB;
                    changeToActionsView();
                }
                else
                {
                    changeToSelectSocietateView();
                }
            }

            // create a connection to the database
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
            Alerts.exceptionAlert(e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Alerts.exceptionAlert(e);
        }


        globalPrimaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.exit(0);
                    }
                });
            }
        });

        globalPrimaryStage.show();

    }

    public void changeToActionsView()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "actionsView.fxml"));
            Parent root = (Parent)loader.load();

            ActionsController actionsController = (ActionsController)loader.getController();

            actionsController.initialize(this);

            globalPrimaryStage.setScene(new Scene(root));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeToSelectSocietateView()
    {

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "selectSocietteView.fxml"));
            Parent root = (Parent)loader.load();

            FirstPageController firstPageController = loader.getController();
            firstPageController.setMain(this);

            globalPrimaryStage.setScene(new Scene(root));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeToCreareMijlocFixView()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "creareMijlocFixView.fxml"));
            Parent root = (Parent)loader.load();

            MijlocFixController mijlocFixController = (MijlocFixController) loader.getController();
            mijlocFixController.setMain(this);

            globalPrimaryStage.setScene(new Scene(root));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getSocietateActuala() {
        return societateActuala;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setSocietateActuala(String societateActuala) {
        this.societateActuala = societateActuala;
    }

    public static Stage getGlobalPrimaryStage() {
        return globalPrimaryStage;
    }

    public static Rectangle2D getPrimaryScreenBounds() {
        return primaryScreenBounds;
    }
}
