import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;

public class ControllerItem {
    
    /* Estas labels son las del view0, se las pasaremos al crear el item 
     * de esta forma cuando le demos a un item, se ejecuta el metodo displayInfo() 
     * se cargara la informacion que tiene en la person a la view0
     * con solo pulsar un item 
    */
    @FXML
    private Label number, name, phone;

    @FXML
    private Polygon coloredShape;

    private JSONObject person;

    private Label idField, nameField, surnameField, phoneField, mailField;

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

    /* Para pasarle las labels del view0 que modificara cuando lo cliquemos */
    public void setlinkedLabels(Label idField, Label nameField, Label surnameField, Label phoneField, Label mailField) {
        this.idField = idField;
        this.nameField = nameField;
        this.surnameField = surnameField;
        this.phoneField = phoneField;
        this.mailField = mailField;
    }

    @FXML
    /* Funcion para que el Item cargue la información,
    en esas labels de view0 */
    public void displayInfo() {
        idField.setText(number.getText());
        nameField.setText(String.valueOf(person.get("userName")));
        surnameField.setText(String.valueOf(person.get("userSurname")));
        phoneField.setText(phone.getText());
        mailField.setText(String.valueOf(person.get("userMail")));
    }

}
