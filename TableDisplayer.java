import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TableDisplayer<T> {

    private TableView<T> table = new TableView<>();

    private Stage stage = new Stage();
    private Label label = new Label("");


    private ObservableList<T> data = FXCollections.observableArrayList();

    TableDisplayer() {

        placeAndSize();

        Scene scene = new Scene(new Group());

        label.setFont(new Font("Arial", 20));
        stage.setWidth(450);
        stage.setHeight(500);

        table.setEditable(false);

        table.setItems(data);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
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

    public void placeAndSize()
    {
        stage.setY(Main.getGlobalPrimaryStage().getHeight());
        stage.setX(0);

        stage.setHeight(Main.getPrimaryScreenBounds().getHeight() - Main.getGlobalPrimaryStage().getHeight());
    }


}