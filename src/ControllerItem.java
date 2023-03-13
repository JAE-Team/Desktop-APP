import org.json.JSONArray;
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
    en la view0, a partir del controlador
    tambien hace llamda a la API para obtener las transacciones*/
    public void displayInfo() {
        Controller0 controllerView0 = (Controller0) UtilsViews.getController("View0");
        controllerView0.setId(number.getText());
        controllerView0.setName(String.valueOf(person.get("userName")));
        controllerView0.setSurname(String.valueOf(person.get("userSurname")));
        controllerView0.setMail(String.valueOf(person.get("userEmail")));
        controllerView0.setPhone(phone.getText());
        controllerView0.setAmmount(String.valueOf(person.get("userBalance")));
        chargeTransactions(number.getText());
    }

    /* Pide el id de usuario */
    public void chargeTransactions(String userId){
        Controller0 controllerView0 = (Controller0) UtilsViews.getController("View0");
        JSONObject obJSON = new JSONObject("{}");
        obJSON.put("userId", phone.getText());
        controllerView0.clearTransactions();
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host +":" + Main.port + "/api/get_transactions", obJSON.toString(), (response) -> {
            JSONObject objResponse = new JSONObject(response);
            Object message = objResponse.get("message");
        
            // System.out.println("Recibido del server: "+objResponse.toString());
            if (message instanceof String) {
                UtilsAlerts.alertError("Error", "No se ha podido obtener del servidor la información de las transferencias de "+name.getText());
            } else if (message instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) message;
                if(jsonArray.length() ==0){
                    UtilsAlerts.alertInformation("Sin transferencias", "No se han encontrado transferencias asociadas a este usuario en la base de datos");
                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objTransaction = jsonArray.getJSONObject(i);
                        controllerView0.addTransaction(objTransaction);
                    }
                }

            }
        });

        
 
    }

}
