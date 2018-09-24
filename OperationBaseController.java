import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OperationBaseController {

    private boolean dirtyFlag = false;
    private ArrayList<ValueBarController> valueBarControllers = new ArrayList<>();
    private ArrayList<String> procentTVAs = new ArrayList<>();

    @FXML
    TextField nrInventarTextField;

    @FXML
    TextField nrReceptieTextField;

    @FXML
    TextField felDocumentTextField;

    @FXML
    TextField nrDocumentTextField;

    @FXML
    DatePicker dataOperatieiDatePicker;

    @FXML
    VBox valueInputVBox;

    @FXML
    Button addNewValueBarButton;

    @FXML
    HBox baseOperationLayerHBox;

    @FXML
    public void addNewValueBarButtonAction() {
        addEmptyValueBar();
    }

    public void initialize() {

        //.......................initialize data operatiei
        dataOperatieiDatePicker.setValue(LocalDate.now());
        //...........................................set up procentTVAs

        ArrayList<Integer> numericTVAs = DbfAccess.getTVAProcent();

        for (Integer i : numericTVAs)
            procentTVAs.add(i.toString());

        procentTVAs.add(Finals.SCUTIT_CU_DREPT);
        procentTVAs.add(Finals.SCUTIT_FARA_DREPT);
        procentTVAs.add(Finals.NEIMPOZABIL);

        //......................add first value bar
        addEmptyValueBar();
    }

    public boolean validateBaseInputForAdaugare() throws SQLException {

        //.......................validate nrInventar

        if (nrInventarTextField.getText() == null || nrInventarTextField.getText().equals("")) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        if (!MySQLJDBCUtil.recordExists(Main.getSocietateActuala(), "mijlocFix", "nrInventar", nrInventarTextField.getText())) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_INVENTAR_NU_EXISTS_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //..............validate nr receptie
        if (nrReceptieTextField.getText() == null || nrReceptieTextField.getText().equals("")) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_RECEPTIE_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //..............validate fel document
        if (felDocumentTextField.getText() == null || felDocumentTextField.getText().equals("")) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.FEL_DOCUMNENT_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //..............validate nr document
        if (nrDocumentTextField.getText() == null || nrDocumentTextField.getText().equals("")) {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.NR_DOCUMENT_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        //..............................validate values

        for (ValueBarController vbc : valueBarControllers) {
            if (!vbc.validateInput())
                return false;
        }

        //..............passed
        return true;
    }
    

    public boolean isDirty() {
        return dirtyFlag;
    }

    public boolean addEmptyValueBar() {
        if (valueBarControllers.size() == 6) // cant have more than 6
        {
            return false;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Finals.VIEWS_PATH + "operationValoriView.fxml"));
            GridPane valueInputGridPane = loader.load();

            ValueBarController vbc = (ValueBarController) loader.getController();
            valueBarControllers.add(vbc);
            valueInputVBox.getChildren().add(valueInputGridPane);

            vbc.removeButton.setOnAction((event) -> {
                // Button was clicked, do something...
                valueBarControllers.remove(vbc);
                valueInputVBox.getChildren().remove(valueInputGridPane);
            });

            vbc.initializeProcentTVAComboBox(procentTVAs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean addValueBar(float valoareFaraTVA, int TVAIndex, int TVA, float diferentaTVA)
    {
        boolean ret = addEmptyValueBar();
        ValueBarController vbc = valueBarControllers.get(valueBarControllers.size() - 1);

        vbc.getValoareFaraTVATextField().setText("" + valoareFaraTVA);

        if (TVAIndex >= 4)
            vbc.getProcentTVAComboBox().setValue(TVAIndex);
        else
            vbc.getProcentTVAComboBox().setValue("" + TVA);

        vbc.getDiferentaTVATextField().setText("" + diferentaTVA);

        vbc.getValoareTVATextField().setText("" + Util.round(valoareFaraTVA * TVA / 100, 2));

        return ret;
    }

    public void removeAllValueBars()
    {
        for (int i = 1; i < valueInputVBox.getChildren().size(); i++)   // We must not remoove the + button at index 0
            valueInputVBox.getChildren().remove(i);
    }

    public VBox getValueInputVBox() {
        return valueInputVBox;
    }

    public ArrayList<ValueBarController> getValueBarControllers() {
        return valueBarControllers;
    }

}
