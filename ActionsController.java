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
    private OperatiuniTableInitializer.OperatieData selectedOperatieData;

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
    ComboBox vizualizareOptionsComboBox;

    @FXML
    public void schimbareSocietateButtonClicked()
    {
        main.changeToSelectSocietateView();
    }

    @FXML
    public void selectareOperatieComboBoxAction()
    {

        switch (selectareOperatieComboBox.getValue().toString()) {
            case Finals.MIFIX_OP:
                mifixOptionSelected();
                break;
            case Finals.VANZARE_OP:
                generalOperatieOptionSelected(Finals.VANZARE_OP);
                break;
            case Finals.CASARE_OP:
                generalOperatieOptionSelected(Finals.CASARE_OP);
                break;
            case Finals.REEVALUARE_OP:
                reevaluareOperatieOptionSelected();
                break;
            case Finals.COMPLETARE_OP:
                generalOperatieOptionSelected(Finals.COMPLETARE_OP);
                break;
            case Finals.AMENAJARE_OP:
                generalOperatieOptionSelected(Finals.AMENAJARE_OP);
                break;
            case Finals.TRANSPORT_OP:
                generalOperatieOptionSelected(Finals.TRANSPORT_OP);
                break;
            case Finals.ACHIZATIE_OP:
                generalOperatieOptionSelected(Finals.ACHIZATIE_OP);
                break;
            case Finals.SUSPENDARE_OP:
                suspendareOperatieOptionSelected();
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
                            clasificareSelectedFromTable();
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
                            mifixSelectedFromTable();
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

    @FXML
    public void vizualizareOperatiuniButtonAction()
    {
        if (vizualizareOptionsComboBox.getValue() == Finals.OPERATIUNI_VIZ_OP)
        {
            try
            {
                /*if(!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", selectedNrInventarTextBox.getText()))
                {
                    Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
                    return;
                }
                else*/
                {
                    TableDisplayer td = OperatiuniTableInitializer.initializeTable(selectedNrInventarTextBox.getText(), vizualizareOperatiiStartDatePicker.getValue(), vizualizareOperatiiEndDatePicker.getValue());
                    td.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            for (OperatiuniTableDisplayer<OperatiuniTableInitializer.OperatieData> otd : OperatiuniTableInitializer.getTds()) //we are looking for  the TableDisplayer
                            {
                                if (otd.getTable().getSelectionModel().getSelectedItem() == newSelection)   //If we found it
                                {
                                    operatieSelectedFromTable(otd); //we use it to call the operatieselected function
                                    return; // thats all we need
                                }
                            }
                        }
                    });
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        else if (vizualizareOptionsComboBox.getValue() == Finals.SUSPENDARI_VIZ_OP)
        {

        }
    }

    public void initialize(Main main) {

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

        selectareOperatieComboBox.getItems().add(Finals.MIFIX_OP);
        selectareOperatieComboBox.getItems().add(Finals.SUSPENDARE_OP);

        try
        {

            selectareOperatieComboBox.getItems().addAll(MySQLJDBCUtil.getfeluriOperati());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


         /*selectareOperatieComboBox.getItems().addAll(
                 Finals.MIFIX_OP,
                 Finals.ACHIZATIE_OP,
                 Finals.COMPLETARE_OP,
                 Finals.REEVALUARE_OP,
                 Finals.AMENAJARE_OP,
                 Finals.TRANSPORT_OP,
                 Finals.CASARE_OP,
                 Finals.VANZARE_OP
         );*/
        //................................................................................set up actions combo box

        selectareActionComboBox.getItems().addAll(
                Finals.ADAUGARE_OP,
                Finals.MODIFICARE_OP,
                Finals.STERGERE_OP
        );

        selectareActionComboBox.setValue(selectareActionComboBox.getItems().get(0));

        //..............................set up vizualizareOptions combo box

        vizualizareOptionsComboBox.getItems().addAll(
                Finals.OPERATIUNI_VIZ_OP,
                Finals.SUSPENDARI_VIZ_OP
        );

        vizualizareOptionsComboBox.setValue(Finals.OPERATIUNI_VIZ_OP);

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
                return new GeneralOperatieController(felOperatiei, this);
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

    public void reevaluareOperatieOptionSelected()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "reevaluareView.fxml"));

            loader.setControllerFactory(c -> {
                return new ReevaluareController(this);
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

    public void suspendareOperatieOptionSelected()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "suspendareView.fxml"));

            loader.setControllerFactory(c -> {
                return new SuspendareController(this);
            });
            HBox sublayerHBox = loader.load();

            operationController = null;

            if (optionContentVBox.getChildren().size() > 1)     //remoove last operatie from the screen if necesarry
                optionContentVBox.getChildren().remove(1);

            optionContentVBox.getChildren().add(sublayerHBox);

            Main.getGlobalPrimaryStage().setMinWidth(1000);
            Main.getGlobalPrimaryStage().setMinHeight(380);

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

    public void mifixSelectedFromTable() //mifix selected from table view
    {
        //set up selected nrInventar

        selectedNrInventarTextBox.setText(MijlocFixTableInitializer.getTd().getTable().getSelectionModel().getSelectedItem().getNrInventar());

        //...............................................fill input with selected
        if (selectareOperatieComboBox.getValue() != null)
        {
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
    }

    public void clasificareSelectedFromTable() //clasificare selected from table view
    {
        if (selectareOperatieComboBox.getValue() != null)
        {
            if (selectareOperatieComboBox.getValue().toString().equals(Finals.MIFIX_OP) &&
                    !selectareActionComboBox.getValue().toString().equals(Finals.STERGERE_OP))    //mifix option selected but not stergere action
            {
                mijlocFixController.clasificareSelectedInTable();
            }
        }
    }

    public void operatieSelectedFromTable(OperatiuniTableDisplayer<OperatiuniTableInitializer.OperatieData> otd) //operatie selected from table view
    {
        selectedOperatieData = otd.getTable().getSelectionModel().getSelectedItem();
        selectareOperatieComboBox.setValue(otd.getTable().getSelectionModel().getSelectedItem().getFelOperatiei());
        operationController.operatieSelectedInTable(otd);
    }

    public OperatiuniTableInitializer.OperatieData getSelectedOperatieData() {
        return selectedOperatieData;
    }

    /*public void vanzareOptionSelected()
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
*/
    /*public void acizitieOptionSelected()
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
    }
*/
}
