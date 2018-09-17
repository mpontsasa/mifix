import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TableDisplayer<T> {

    private TableView<T> table = new TableView<>();

    private Stage stage = new Stage();
    private Label label = new Label("");
    private HBox  searchFields = new HBox();
    private ArrayList<TextField> searchTextFields = new ArrayList<>();


    private ObservableList<T> data = FXCollections.observableArrayList();
    private FilteredList<T> filteredData;
    private SortedList<T> sortedData;

    TableDisplayer() {

        filteredData = new FilteredList<>(data, p -> true);

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<T> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);


        placeAndSize();

        Scene scene = new Scene(new Group());

        label.setFont(new Font("Arial", 20));
        stage.setWidth(450);
        stage.setHeight(500);

        table.setEditable(false);

        //table.setItems(data);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, searchFields, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
    }

    public void setUpSearchField()
    {
        TextField textField;

        for (int i = 0; i < table.getColumns().size(); i++)
        {
            textField = new TextField();

            textField.prefWidthProperty().bind(table.getColumns().get(i).widthProperty());
            textField.minWidthProperty().bind(table.getColumns().get(i).widthProperty());
            textField.maxWidthProperty().bind(table.getColumns().get(i).widthProperty());

            searchFields.getChildren().add(textField);
            searchTextFields.add(textField);
        }
    }

    public void hide()
    {
        stage.hide();
    }

    public void close()
    {
        stage.close();
    }

    public void show()
    {
        stage.show();
    }

    public void placeAndSize()
    {
        stage.setY(Main.getGlobalPrimaryStage().getHeight());
        stage.setX(0);

        stage.setHeight(Main.getPrimaryScreenBounds().getHeight() - Main.getGlobalPrimaryStage().getHeight() - 200);
    }

    public TableView<T> getTable() {
        return table;
    }

    public Stage getStage() {
        return stage;
    }

    public Label getLabel() {
        return label;
    }

    public ObservableList<T> getData() {
        return data;
    }

    public HBox getSearchFields() {
        return searchFields;
    }

    public FilteredList<T> getFilteredData() {
        return filteredData;
    }

    public SortedList<T> getSortedData() {
        return sortedData;
    }

    public ArrayList<TextField> getSearchTextFields() {
        return searchTextFields;
    }
}