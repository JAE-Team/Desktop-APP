import java.io.Console;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.sampled.SourceDataLine;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller0 implements Initializable {


    @FXML
    private TextField serverIP;

    @FXML
    private TextField serverPort;
    @FXML
    private Label infoLabel;
    @FXML
    private ChoiceBox protocolChoise;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] cities = {"https","http"};
        ObservableList<String> list = FXCollections.observableArrayList(cities);
        protocolChoise.setValue("https");
        protocolChoise.setItems(list);
    }

    @FXML
    private void setView0() {
        Main.protocol=protocolChoise.getSelectionModel().getSelectedItem().toString();
        Main.port=Integer.parseInt(serverPort.getText());
        Main.host=serverIP.getText();
        System.out.println(Main.protocol+"://" + Main.host + ":" + Main.port + "/checkServer");
        UtilsHTTP.sendPOST(Main.protocol+"://" + Main.host + ":" + Main.port + "/checkServer", "", (response) -> {
            System.out.println(response);
            JSONObject objResponse = new JSONObject(response);
            if(!objResponse.get("status").equals("KO")){
                UtilsViews.setViewAnimating("View1");
                Controller1.getInstance().getPersonas();
            }else{
                System.out.println("ERROR");
                infoLabel.setText("Error: Unable to connect to server");
            }
            
        });
        
    }

    

}