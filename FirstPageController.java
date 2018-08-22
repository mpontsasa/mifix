
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.module.Configuration;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Properties;

public class FirstPageController{

    private DbfAccess dbfAccess = new DbfAccess();

    Main main;

    @FXML
    private ComboBox selectareSocietateComboBox;

    @FXML
    private Label selectareSocietateLable;

    @FXML
    public void selectareSocietateComboBoxOnShowing()
    {
        ObservableList<String> societati = dbfAccess.getSocietatiNames();

        selectareSocietateComboBox.getItems().clear();
        selectareSocietateComboBox.getItems().addAll(societati);
    }

    @FXML
    public void selecteazaButtonAction() throws Exception
    {
        if (selectareSocietateComboBox.getSelectionModel().getSelectedItem() != null && !selectareSocietateComboBox.getSelectionModel().getSelectedItem().toString().equals(""))
        {
            String selected = selectareSocietateComboBox.getSelectionModel().getSelectedItem().toString();
            if (selectareSocietateComboBox.getItems().contains(selected))
            {
                if (MySQLJDBCUtil.setUpSocietateDB(selectareSocietateComboBox.getValue().toString()))
                {
                    main.setSocietateActuala(selected.trim());

                    WriteProperty.createProperties().setProperty("activeDB", Main.getSocietateActuala());
                    WriteProperty.saveProperties();

                    main.changeToActionsView();
                }
            }
            else
            {
                selectareSocietateLable.setText(Finals.SECIETATE_NU_EXISTA);
                selectareSocietateLable.setTextFill(Color.web("#FF0000"));
            }
        }
        else
        {
            selectareSocietateLable.setText(Finals.SECIETATE_NU_A_FOST_SELECTATA);
            selectareSocietateLable.setTextFill(Color.web("#FF0000"));
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
