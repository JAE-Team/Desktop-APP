import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Controller0 implements Initializable {

    @FXML
    private Label idField, nameField, surnameField, mailField, phoneField;

    @FXML
    private ImageView imgConsole;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private VBox vBoxList = new VBox();

    private static Controller0 instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getUsers();

        // Start choiceBox setting onaction event
        instance=this;
    }

    @FXML
    private void setView1() {
        UtilsViews.setViewAnimating("View0");
    }
    @FXML
    public void getUsers(){
        vBoxList.getChildren().clear();
        loading.setVisible(true);
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host + ":" + Main.port + "/api/get_profiles", "si", (response) -> {
            JSONObject objResponse = new JSONObject(response);
            //JSONArray JSONlist = objResponse.get("");
            JSONArray jsonArray = objResponse.getJSONArray("message");
            
            for(int i=0;i<jsonArray.length();i++){
                addPersona(jsonArray.getJSONObject(i));
            }
            loading.setVisible(false);
        });
    }
    private void addPersonaTest(String id, String name){
        try{
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();
            itemController.setName(name);
            itemController.setNumber(id);
            vBoxList.getChildren().add(itemTemplate);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Add template to the list
    }
    private void addPersona(JSONObject persona){
        try{
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();

            itemController.setPhone(String.valueOf(persona.get("userId")));
            itemController.setName(String.valueOf(persona.get("userName"))+String.valueOf(persona.get("userSurname")));
            itemController.setNumber(String.valueOf(persona.get("id")));
            itemController.setPerson(persona);
            /* Creamos el item i le especificamos los campos que modificara en caso de clicar-lo */
            itemController.setlinkedLabels(idField, nameField, surnameField, mailField, phoneField);
            vBoxList.getChildren().add(itemTemplate);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Add template to the list
    }
    @FXML
    private void setView3() {
        UtilsViews.setViewAnimating("View3");
    }


    public static Controller0 getInstance(){
        return instance;
    }

}