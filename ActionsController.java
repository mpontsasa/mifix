import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class ActionsController {

    Main main;

    private MijlocFixController mijlocFixController = null; //controller of active mifix view
    private OperationController operationController = null; //controller of active operatie

    @FXML
    Label societateActivaLabel;

    @FXML
    Button schimbareSocietateButton;

    @FXML
    CheckBox vizualiareTabelClsificariCheckBox;

    @FXML
    CheckBox vizualizareMijlocFixCheckBox;

    @FXML
    ComboBox selectareOperatieComboBox;

    @FXML
    VBox optionContentVBox;

    @FXML
    ScrollPane baseScrollPane;

    @FXML
    ComboBox selectareActionComboBox;

    @FXML
    TextField selectedNrInventarTextBox;

    @FXML
    DatePicker vizualizareOperatiiStartDatePicker;

    @FXML
    DatePicker vizualizareOperatiiEndDatePicker;

    @FXML
    public void schimbareSocietateButtonClicked()
    {
        main.changeToSelectSocietateView();
    }

    @FXML
    public void selectareOperatieComboBoxAction()
    {
        switch (selectareOperatieComboBox.getValue().toString())
        {
            case Finals.MIFIX_OP:
                mifixOptionSelected();
                break;
            case Finals.VANZARE_OP:
                generalOperatieOptionSelected("vanzare");
                break;
            case Finals.ACHIZATIE_OP:
                generalOperatieOptionSelected("achizitie");
                break;
            case Finals.CASARE_OP:
                generalOperatieOptionSelected("casare");
                break;
            case Finals.REEVALUARE_OP:
                generalOperatieOptionSelected("reevaluare");
                break;
            case Finals.COMPLETARE_OP:
                generalOperatieOptionSelected("completare");
                break;
            case Finals.AMENAJARE_OP:
                generalOperatieOptionSelected("amenajare");
                break;
            case Finals.TRANSPORT_OP:
                generalOperatieOptionSelected("transport");
                break;
        }
        placeAndSizeAllTables();
    }

    @FXML
    public void selectareActionComboBoxAction()
    {

    }

    @FXML
    public void vizualiareTabelClsificariCheckBoxAction()
    {
        if (vizualiareTabelClsificariCheckBox.isSelected())
        {
            try
            {
                if (ClasificariTableInitializer.show())
                {
                    ClasificariTableInitializer.getTd().getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            clasificareSelected();
                        }
                    });
                }
            }
            catch (SQLException e) {
                //SQL ERRRORRRRR
                e.printStackTrace();
            }
        }
        else
        {
            ClasificariTableInitializer.hide();
        }
    }

    @FXML
    public void vizualizareMijlocFixCheckBoxAction()
    {
        if (vizualizareMijlocFixCheckBox.isSelected())
        {
            try
            {
                if (MijlocFixTableInitializer.show()) {
                    MijlocFixTableInitializer.getTd().getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            mifixSelected();
                        }
                    });
                }

            }
            catch (SQLException e) {
                //SQL ERRRORRRRR
                e.printStackTrace();
            }
        }
        else
        {
            MijlocFixTableInitializer.hide();
        }
    }

    public void vizualizareOperatiuniButtonAction()
    {

    }

    public void initialize(Main main) {
        this.main = main;

        societateActivaLabel.setText(Main.getSocietateActuala());

        //............set up the stage

        main.getGlobalPrimaryStage().setX(0);
        main.getGlobalPrimaryStage().setY(0);

        Main.getGlobalPrimaryStage().heightProperty().addListener(e ->{
            placeAndSizeAllTables();
        });

        // ..............................................................................set up clasificare table view

        ClasificariTableInitializer.setLc(new ListenerCallbeck() {
            @Override
            public void action() {
                vizualiareTabelClsificariCheckBox.setSelected(false);
            }
        });

        if (ClasificariTableInitializer.getTd() != null && ClasificariTableInitializer.getTd().getStage().isShowing())
        {
            vizualiareTabelClsificariCheckBox.setSelected(true);
        }
        // ..............................................................................set up clasificare table view

        MijlocFixTableInitializer.setLc(new ListenerCallbeck() {
            @Override
            public void action() {
                vizualizareMijlocFixCheckBox.setSelected(false);
            }
        });

        if (MijlocFixTableInitializer.getTd() != null && MijlocFixTableInitializer.getTd().getStage().isShowing())
        {
            vizualizareMijlocFixCheckBox.setSelected(true);
        }

        //................................................................................set up operatiuni combo box

         selectareOperatieComboBox.getItems().addAll(
                 Finals.MIFIX_OP,
                 Finals.ACHIZATIE_OP,
                 Finals.COMPLETARE_OP,
                 Finals.REEVALUARE_OP,
                 Finals.AMENAJARE_OP,
                 Finals.TRANSPORT_OP,
                 Finals.CASARE_OP,
                 Finals.VANZARE_OP
         );
        //................................................................................set up actions combo box

        selectareActionComboBox.getItems().addAll(
                Finals.ADAUGARE_OP,
                Finals.MODIFICARE_OP,
                Finals.STERGERE_OP
        );

        selectareActionComboBox.setValue(selectareActionComboBox.getItems().get(0));

        //
    }

    public void mifixOptionSelected()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "creareMijlocFixView.fxml"));
            loader.setControllerFactory(c -> {
                return new MijlocFixController(this);
            });
            ScrollPane creareMifixPanel = loader.load();

            mijlocFixController = (MijlocFixController) loader.getController();

            if (optionContentVBox.getChildren().size() > 1)
                optionContentVBox.getChildren().remove(1);

            optionContentVBox.getChildren().add(creareMifixPanel);

            main.getGlobalPrimaryStage().setMinWidth(1000);
            main.getGlobalPrimaryStage().setMinHeight(380);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void generalOperatieOptionSelected(String felOperatiei)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "generalOperatieView.fxml"));

            loader.setControllerFactory(c -> {
                return new GeneralOperatieController(felOperatiei, selectareActionComboBox);
            });
            HBox sublayerHBox = loader.load();

            operationController = loader.getController();

            if (optionContentVBox.getChildren().size() > 1)     //remoove last operatie from the screen if necesarry
                optionContentVBox.getChildren().remove(1);

            optionContentVBox.getChildren().add(sublayerHBox);

            main.getGlobalPrimaryStage().setMinWidth(1000);
            main.getGlobalPrimaryStage().setMinHeight(380);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void placeAndSizeAllTables()
    {

        if (vizualiareTabelClsificariCheckBox.isSelected())
        {
            ClasificariTableInitializer.getTd().placeAndSize();
        }

        if (vizualizareMijlocFixCheckBox.isSelected())
        {
            MijlocFixTableInitializer.getTd().placeAndSize();
        }
    }

    public void mifixSelected() //mifix selected from table view
    {
        //set up selected nrInventar

        selectedNrInventarTextBox.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getNrInventar());

        //...............................................fill input with selected
        if (selectareOperatieComboBox.getValue().toString().equals(Finals.MIFIX_OP) &&
                !selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))    //mifix option selected but not adaugare action
        {
            mijlocFixController.mijlocFixSelectedInTable();
        }
        else if(!selectareOperatieComboBox.getValue().toString().equals(Finals.MIFIX_OP) &&
                selectareActionComboBox.getValue().toString().equals(Finals.ADAUGARE_OP))    //operatie option selected and adaugare action
        {
            operationController.mijlocFixSelectedInTable();
        }
    }

    public void clasificareSelected() //clasificare selected from table view
    {
        if (selectareOperatieComboBox.getValue().toString().equals(Finals.MIFIX_OP) &&
                !selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))    //mifix option selected but not stergere action
        {
            mijlocFixController.clasificareSelectedInTable();
        }
    }

    /*    public void vanzareOptionSelected()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "vanzareView.fxml"));
            HBox sublayerHBox = loader.load();

            operationController = (VanzareController) loader.getController();
            if (optionContentVBox.getChildren().size() > 1)     //remoove last operatie from the screen if necesarry
                optionContentVBox.getChildren().remove(1);

            optionContentVBox.getChildren().add(sublayerHBox);

            main.getGlobalPrimaryStage().setMinWidth(1000);
            main.getGlobalPrimaryStage().setMinHeight(380);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void acizitieOptionSelected()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "achizitieView.fxml"));
            HBox sublayerHBox = loader.load();

            operationController = (AchizitieController) loader.getController();
            ((AchizitieController)operationController).setMain(main);

            if (optionContentVBox.getChildren().size() > 1)     //remoove last operatie from the screen if necesarry
                optionContentVBox.getChildren().remove(1);

            optionContentVBox.getChildren().add(sublayerHBox);

            main.getGlobalPrimaryStage().setMinWidth(1000);
            main.getGlobalPrimaryStage().setMinHeight(380);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
}
