import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller3 {

    @FXML
    private TextField name,surname,mail,phone,street,city;
    @FXML
    private Label infoLabel;
    @FXML
    private void addPerson(){
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host + ":" + Main.port + "/addPerson", getAllValues().toString(), (response) -> {
            JSONObject objResponse = new JSONObject(response);
            if(objResponse.get("status").equals("KO")){
                infoLabel.setText("Error: "+objResponse.get("result"));
            }else{
                infoLabel.setText("");
                clearAllInputs();
                setView1();
            }
            
        });
        
    }
    @FXML
    private void setView1() {
        Controller1.getInstance().getPersonas();
        clearAllInputs();
        UtilsViews.setViewAnimating("View1");
    }
    private void clearAllInputs(){
        name.setText("");
        surname.setText("");
        mail.setText("");
        phone.setText("");
        street.setText("");
        city.setText("");
    }

    public JSONObject getAllValues(){
        JSONObject person = new JSONObject();
        person.put("name", name.getText());
        person.put("surname", surname.getText());
        person.put("mail", mail.getText());
        person.put("phone", phone.getText());
        person.put("street", street.getText());
        person.put("city", city.getText());
        return person;
    }
    
}
