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

public class Controller1 implements Initializable {

    @FXML
    private Label number, name;

    @FXML
    private ImageView imgConsole;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ProgressIndicator loading;
    private int loadingCounter = 0;

    @FXML
    private VBox vBoxList = new VBox();

    private static Controller1 instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Start choiceBox setting onaction event
        instance=this;
    }

    @FXML
    private void setView1() {
        UtilsViews.setViewAnimating("View0");
    }
    @FXML
    public void getPersonas(){
        vBoxList.getChildren().clear();
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host + ":" + Main.port + "/people", "si", (response) -> {
            JSONObject objResponse = new JSONObject(response);
            //JSONArray JSONlist = objResponse.get("");
            JSONArray jsonArray = objResponse.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++){
                addPersona(jsonArray.getJSONObject(i));
            }
            
        });
    }
    private void addPersona(JSONObject persona){
        try{
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();
            itemController.setName(String.valueOf(persona.get("name")));
            itemController.setNumber(String.valueOf(persona.get("id")));
            itemController.setPerson(persona);
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
    private void showLoading () {
        loadingCounter++;
        loading.setVisible(true);
    }

    private void hideLoading () {
        loadingCounter--;
        if (loadingCounter <= 0) {
            loadingCounter = 0;
            loading.setVisible(false);
        }
    }

    public static Controller1 getInstance(){
        return instance;
    }

}