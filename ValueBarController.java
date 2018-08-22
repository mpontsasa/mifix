import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ValueBarController {

    int procent;

    @FXML
    Button removeButton;

    @FXML
    ComboBox procentTVAComboBox;

    @FXML
    TextField valoareTVATextField;

    @FXML
    TextField valoareFaraTVATextField;

    @FXML
    TextField diferentaTVATextField;

    @FXML
    public void procentTVAComboBoxAction()
    {
        if (procentTVAComboBox.getValue() == null)
            return;

        try
        {
            procent = Integer.parseInt(procentTVAComboBox.getValue().toString());
        }
        catch (NumberFormatException e)
        {
            if (procentTVAComboBox.getItems().contains(procentTVAComboBox.getValue().toString()))
            {
                procent = 0;
            }
            else
            {
                procentTVAComboBox.setValue(null);
                valoareTVATextField.setText("");
                return;
            }
        }

        try
        {
            if (valoareFaraTVATextField.getText() != null && !valoareFaraTVATextField.getText().equals(""))
            {
                Double valTva = Util.round(Float.parseFloat(valoareFaraTVATextField.getText()) * procent / 100, 2);
                valoareTVATextField.setText(valTva.toString());
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

    }

    @FXML
    public void valoareFaraTVATextFieldAction()
    {
        if (valoareTVATextField.getText() == null)
            return;

        try
        {
            Float.parseFloat(valoareFaraTVATextField.getText());
        }
        catch (NumberFormatException e)
        {
                valoareFaraTVATextField.setText("");
                valoareTVATextField.setText("");
                return;
        }

        try
        {
            if (procentTVAComboBox.getValue() != null)
            {
                Double valTva = Util.round(Float.parseFloat(valoareFaraTVATextField.getText()) * procent / 100, 2);
                valoareTVATextField.setText(valTva.toString());
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

    }

    @FXML
    public void diferentaTVATextFieldAction()
    {

        if (diferentaTVATextField.getText() == null || diferentaTVATextField.getText() .equals(""))
            return;

        try
        {
            diferentaTVATextField.setText("" + Util.round(Float.parseFloat(diferentaTVATextField.getText()), 2));
        }
        catch (NumberFormatException e)
        {
            diferentaTVATextField.setText("");
        }
    }

    public boolean validateInput()
    {
        //......................validating Valoare fara tva
        if ((valoareFaraTVATextField.getText() == null || valoareFaraTVATextField.getText().equals("")) &&
                (procentTVAComboBox.getValue() == null || procentTVAComboBox.getValue().toString().equals("")) &&
                (diferentaTVATextField.getText() == null || diferentaTVATextField.getText().equals("")))
        {
            return true;
        }

        //......................validating procent TVA

        if (procentTVAComboBox.getValue() == null || procentTVAComboBox.getValue().toString().equals(""))
        {
            Alerts.errorAlert(Finals.INVALID_INPUT_TITLE_TEXT, Finals.PROCENT_TVA_EMPTY_HEADER_TEXT, Finals.INVALID_INPUT_CONTENT_TEXT);
            return false;
        }

        return true;
    }

    public void initializeProcentTVAComboBox(ArrayList<String> items)
    {
        procentTVAComboBox.getItems().addAll(items);
    }

    public ComboBox getProcentTVAComboBox() {
        return procentTVAComboBox;
    }

    public TextField getValoareTVATextField() {
        return valoareTVATextField;
    }

    public TextField getValoareFaraTVATextField() {
        return valoareFaraTVATextField;
    }

    public TextField getDiferentaTVATextField() {
        return diferentaTVATextField;
    }
}
