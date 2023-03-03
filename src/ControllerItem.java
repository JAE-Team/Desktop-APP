import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;

public class ControllerItem {
    @FXML
    private Label number, name, phone;

    @FXML
    private Polygon coloredShape;

    private JSONObject person;

    private Controller0 controllerView0;

    @FXML
    private void handleMenuAction() {
        System.out.println(name.getText());
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
    public Label getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone.setText(phone);
    }
    public void setColor(String color) {
        coloredShape.setStyle("-fx-fill: " + color);
    }

    @FXML
    /* Funcion para que el Item cargue la información,
    en la view0, a partir del controlador*/
    public void displayInfo() {
        controllerView0.setId(number.getText());
        controllerView0.setName(String.valueOf(person.get("userName")));
        controllerView0.setSurname(String.valueOf(person.get("userSurname")));
        controllerView0.setMail(String.valueOf(person.get("userMail")));
        controllerView0.setPhone(phone.getText());
    }

    public void chargeTransactions(){
        // Codigo para obtener un JSONObject de transacciones
        controllerView0.addTransaction(new JSONObject());
    }

}
