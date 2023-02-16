import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller2 implements Initializable{

    @FXML
    private TextField id,name,surname,mail,phone,street,city;
    @FXML
    private Label infoLabel;

    private static Controller2 instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Start choiceBox setting onaction event
        instance=this;
    }

    @FXML
    private void setView1() {
        Controller1.getInstance().getPersonas();
        UtilsViews.setViewAnimating("View1");
    }
    @FXML
    private void setView3() {
        Controller1.getInstance().getPersonas();
        UtilsViews.setViewAnimating("View3");
    }

    public void updateView(JSONObject person){
        id.setText(String.valueOf(person.get("id")));
        name.setText(String.valueOf(person.get("name")));
        surname.setText(String.valueOf(person.get("surname")));
        mail.setText(String.valueOf(person.get("mail")));
        phone.setText(String.valueOf(person.get("phone")));
        street.setText(String.valueOf(person.get("street")));
        city.setText(String.valueOf(person.get("city")));
    }
    @FXML
    private void updatePerson(){
        System.out.println("Actualizo");
        System.out.println(getAllValues().toString());
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host + ":" + Main.port + "/updatePerson", getAllValues().toString(), (response) -> {
            JSONObject objResponse = new JSONObject(response);
            if(objResponse.get("status").equals("KO")){
                System.out.println("Res:"+objResponse.get("status")+" message:"+objResponse.get("result"));
                infoLabel.setText("Error: "+objResponse.get("result"));
            }else{
                infoLabel.setText("");
            }
            
        });
    }

    public JSONObject getAllValues(){
        JSONObject person = new JSONObject();
        person.put("id", id.getText());
        person.put("name", name.getText());
        person.put("surname", surname.getText());
        person.put("mail", mail.getText());
        person.put("phone", phone.getText());
        person.put("street", street.getText());
        person.put("city", city.getText());
        return person;
    }
    
    public static Controller2 getInstance(){
        return instance;
    }
}
