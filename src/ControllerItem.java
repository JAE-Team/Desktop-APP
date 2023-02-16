import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;

public class ControllerItem {
    
    @FXML
    private Label number, name;

    @FXML
    private Polygon coloredShape;

    private JSONObject person;

    @FXML
    private void handleMenuAction() {
        Controller2.getInstance().updateView(person);
        System.out.println(name.getText());
        setView2();
    }
    @FXML
    private void setView2() {
        UtilsViews.setViewAnimating("View2");
    }

    public void setNumber(String title) {
        this.number.setText(title);
    }

    public void setName(String subtitle) {
        this.name.setText(subtitle);
    }

    public void setPerson(JSONObject person){
        this.person=person;
    }

    public void setColor(String color) {
        coloredShape.setStyle("-fx-fill: " + color);
    }
}
