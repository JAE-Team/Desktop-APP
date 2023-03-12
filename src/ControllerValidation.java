import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

public class ControllerValidation implements Initializable {

    @FXML
    private Label imgLabel;

    @FXML
    private Button goBack;

    @FXML
    private ImageView anvers;

    @FXML
    private ImageView revers;

    @FXML
    private Button rebutjar;

    @FXML
    private Button acceptar;

    @FXML
    private ProgressIndicator loading;

    private String idUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loading.setVisible(false);
        imgLabel.setVisible(true);
        imgLabel.setText("Aquí apareixeràn les imatges");
    }

    @FXML
    private void acceptedValidation() {
        JSONObject objJSON = new JSONObject("{}");
        objJSON.put("user_id", idUser);
        objJSON.put("status", "accepted");

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/make_verification",
                objJSON.toString(),
                (response) -> {
                    try {
                        JSONObject objResponse = new JSONObject(response);
                        UtilsAlerts.alertInformation("Verificació d'usuari", objResponse.getString("message"));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                });
    }

    @FXML
    private void rejectedValidation() {
        JSONObject objJSON = new JSONObject("{}");
        objJSON.put("user_id", idUser);
        objJSON.put("status", "rejected");

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/make_verification",
                objJSON.toString(),
                (response) -> {
                    try {
                        JSONObject objResponse = new JSONObject(response);
                        UtilsAlerts.alertInformation("Verificació d'usuari", objResponse.getString("message"));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                });
    }

    private void chargeImages(String response) {

        JSONObject objResponse = new JSONObject(response);

        if (objResponse.getString("status").equals("OK")) {
            JSONArray array = new JSONArray(objResponse.getJSONArray("message"));
            JSONObject jsonUser = array.getJSONObject(0);

            String reversKey = jsonUser.getString("revers");
            String anversKey = jsonUser.getString("anvers");

            if (reversKey == null || anversKey == null) {

            } else {
                byte[] decodedBytesAnvers = Base64.getDecoder().decode(reversKey);
                byte[] decodedBytesRevers = Base64.getDecoder().decode(anversKey);

                Image imgAnvers = new Image(new java.io.ByteArrayInputStream(decodedBytesAnvers));
                Image imgRevers = new Image(new java.io.ByteArrayInputStream(decodedBytesRevers));

                anvers.setImage(imgAnvers);
                revers.setImage(imgRevers);
            }

        }
    }

    public void goToView0() {
        UtilsViews.setViewAnimating("View0");
        anvers.setImage(null);
        revers.setImage(null);
    }

    public void setId(String id) {
        this.idUser = id;

        JSONObject objJSON = new JSONObject("{}");
        objJSON.put("user_id", id);

        loading.setVisible(true);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_userDNI", objJSON.toString(),
                (response) -> {
                    chargeImages(response);
                    loading.setVisible(false);
                    imgLabel.setVisible(false);
                });
    }
}