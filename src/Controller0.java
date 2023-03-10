import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Controller0 implements Initializable {

    @FXML
    private Label idField, nameField, surnameField, mailField, phoneField, ammountField;

    @FXML
    private ImageView imgConsole;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private VBox vBoxList = new VBox();

    @FXML
    private VBox vBoxTransactions= new VBox();

    @FXML
    private ToggleButton buttonFilterState, buttonFilterBalances, buttonFilterTransactions;

    @FXML
    private Button buttonValidateUser;

    private static Controller0 instance;

    private JSONObject filters= new JSONObject("{}");
    /* 
     * "filters" : {"filterBalance":"0;100", "filterTransactions":"0;10", "filterStatus":"WAITING_VERIFICATION"}
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getUsers();

        buttonFilterState.setSelected(false);

        buttonFilterBalances.setSelected(false);

        buttonFilterTransactions.setSelected(false);

        instance=this;
    }

    @FXML
    private void setView1() {
        UtilsViews.setViewAnimating("View0");
    }

    /* Cada uno de los 3 Botones de filtro tendra 2 metodos, uno para cuando se clica añadir un filtro, 
     * otro cuando se suelta para quitar el filtro, actuaran sobre el JSONObject filters
    */


    @FXML
    private void statesPressed(ActionEvent event) {
        if (buttonFilterState.isSelected()) {
            System.out.println("States pressed");
            buttonFilterState.setStyle("-fx-background-color: #CFD8DC;");
        } else {
            System.out.println("States released");
            buttonFilterState.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #455A64; -fx-border-width: 3px; -fx-padding: -5;");
        }
    }

    @FXML
    private void balancesPressed(ActionEvent event) {
        if (buttonFilterBalances.isSelected()) {
            System.out.println("Balances pressed");
            buttonFilterBalances.setStyle("-fx-background-color: #CFD8DC;");
        } else {
            System.out.println("Balances released");
            buttonFilterBalances.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #455A64; -fx-border-width: 3px;");
        }
    }

    @FXML
    private void transactionsPressed(ActionEvent event) {
        if (buttonFilterTransactions.isSelected()) {
            System.out.println("Transactions pressed");
            buttonFilterTransactions.setStyle("-fx-background-color: #CFD8DC;");
        } else {
            System.out.println("Transactions released");
            buttonFilterTransactions.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #455A64; -fx-border-width: 3px;");
        }
    }

    private void setFilterStatus(String status){
        filters.append("filterStatus", status);
    }

    private void removeFilterStatus(){
        filters.remove("filterStatus");

    }

    private void setFilterBalance(String balance){
        filters.append("filterBalance", balance);
    }

    private void removeFilterBalance(){
        filters.remove("filterBalance");

    }

    private void setFilterTransactions(String transactions){
        filters.append("filterTransactions", transactions);
    }

    private void removeFilterTransactions(){
        filters.remove("filterTransactions");

    }

    /* Metodo para implementar la spec 35, nos abrira una vista donde veremos las fotografias en grande y nos saldra el desplegable */
    @FXML
    private void validateUser(){
        if (idField.getText() == null || idField.getText() == "") {
            return;
        } else {
            UtilsViews.setViewAnimating("ViewValidation");
            ControllerValidation ctrl = (ControllerValidation) UtilsViews.getController("ViewValidation");
            ctrl.setId(idField.getText());
        }
    }


    
    /* El metodo de cargar perfiles requerira que se le especifique un filtro, por lo que se le pasara el JSONObject filters */
    public void getUsers() {
        vBoxList.getChildren().clear();
        loading.setVisible(true);
        JSONObject objJSON = new JSONObject("{}");
        objJSON.put("filters", this.filters);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles", objJSON.toString(),
                (response) -> {
                    JSONObject objResponse = new JSONObject(response);
                    //JSONArray JSONlist = objResponse.get("");
                    
                    JSONArray jsonArray = objResponse.getJSONArray("message");
                    // Assuming you already have a JSONArray object called jsonArray
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        jsonObject.remove("anvers");
                        jsonObject.remove("revers");
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        addPersona(jsonArray.getJSONObject(i));
                    }
                    loading.setVisible(false);
                });
    }

    private void addPersona(JSONObject persona){
        try{
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();

            itemController.setPhone(String.valueOf(persona.get("userId")));
            itemController.setName(String.valueOf(persona.get("userName"))+" "+String.valueOf(persona.get("userSurname")));
            itemController.setNumber(String.valueOf(persona.get("id")));
            itemController.setPerson(persona);
            /* Creamos el item i le especificamos los campos que modificara en caso de clicar-lo */
            vBoxList.getChildren().add(itemTemplate);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Add template to the list
    }

    /* Añade una unica transaccion, este metodo sera llamado al clicar 
    el ItemList del usuario tantas veces como transacciones tenga este
     */
    public void addTransaction(JSONObject transactionObject){
        try{
            URL resource = this.getClass().getResource("./assets/transactionItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent transaction = loader.load();
            ControllerTransaction transactionController = loader.getController();
            String payer = transactionObject.isNull("userOrigin") ? "Undefined" : transactionObject.getString("originName") 
            + " " +transactionObject.getString("originSurname");
            transactionController.setPayer(payer);

            String receiver = transactionObject.isNull("userDestiny") ? "Undefined" : transactionObject.getString("destinyName")
            + " " +transactionObject.getString("destinySurname");
            transactionController.setReceiver(receiver);

            double amountValue = transactionObject.isNull("ammount") ? Double.NaN : transactionObject.getDouble("ammount");
            String amount = Double.isNaN(amountValue) ? "Undefined" : String.valueOf(amountValue);
            transactionController.setAmount(amount);

            String status = transactionObject.isNull("accepted") ? "Undefined" : transactionObject.getString("accepted");
            transactionController.setStatus(status);

            String date = transactionObject.isNull("timeSetup") ? "Undefined" : transactionObject.getString("timeSetup");
            transactionController.setDate(date);

            vBoxTransactions.getChildren().add(transaction);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String generateRandomString() {
        int length = new Random().nextInt(26) + 5; // Random length between 5 and 30
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    /* Experimento, hay que quitar */

    @FXML
    private void setView3() {
        UtilsViews.setViewAnimating("View3");
    }

    public void setId(String id){
        idField.setText(id);
    }

    public static Controller0 getInstance(){
        return instance;
    }

    public void setName(String name){
        nameField.setText(name);
    }

    public void setSurname(String surname){
        surnameField.setText(surname);
    }

    public void setMail(String mail){
        mailField.setText(mail);
    }

    public void setPhone(String phone){
        phoneField.setText(phone);
    }

    public void setAmmount(String ammount){
        ammountField.setText(ammount);
    }

    public void clearTransactions(){
        vBoxTransactions.getChildren().clear();
    }

}