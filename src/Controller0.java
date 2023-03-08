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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Controller0 implements Initializable {

    @FXML
    private Label idField, nameField, surnameField, mailField, phoneField, ammountField;

    @FXML
    private ImageView imgConsole;

    @FXML
    private ChoiceBox<String> choiceBoxFilter;
    private String[] filters = {"Tots", "estat del compte", "rang de saldos", "rang del nombre de transaccions"};

    @FXML
    private ProgressIndicator loading;

    @FXML
    private VBox vBoxList = new VBox();

    @FXML
    private VBox vBoxTransactions= new VBox();


    private static Controller0 instance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getUsers();
        choiceBoxFilter.getItems().addAll(filters);
        choiceBoxFilter.setValue(filters[0]);

            // Set action listener for the choice box
        choiceBoxFilter.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String selectedOption = choiceBoxFilter.getSelectionModel().getSelectedItem();
            System.out.println("Selected option: " + selectedOption);
            handleSelection(selectedOption);
            }
        });
    instance=this;
    }

    /* Segun la opcion seleccionada por el usuario, que entramos
     * en este metodo, se hara un post u otro para cargar ciertos usuarios
     */
    private void handleSelection(String option) {
        String[] options;
        String value;
        List<String> optionsList;
        switch(option){
            case "Tots":
                getUsers();
                break;
            case "estat del compte":
                options = new String[]{"active", "inactive"};
                value = UtilsAlerts.inputDialog("Selecciona un estat", "Estats disponibles: "+getOptions(options), "Estat:");
                System.out.println(value);
                optionsList = Arrays.asList(options);
                if (optionsList.contains(value)){
                    getUsers("Where userStatus = "+value+";");
                }else{
                    UtilsAlerts.alertError("Error", "Opció no vàlida", "L'opció introduida no és vàlida");
                }

                break;
            case "rang de saldos":

                getUsers();
                break;
            case "rang del nombre de transaccions":
            
                getUsers();
                break;
        }

    }

    private String getOptions(String[] options) {
        String optionsStr = "";
        for (int i = 0; i < filters.length; i++) {
            optionsStr += filters[i] + "\n";
        }
        return optionsStr;
    }

    @FXML
    private void setView1() {
        UtilsViews.setViewAnimating("View0");
    }
    
    /* El metodo de cargar perfiles requerira de una query,
     * la query puede estar vacia, los carga todos, o puede
     * ser un WHERE, que se añadira al SELECT * FROM users; en la api
     */
    public void getUsers(String queryWhere) {
        vBoxList.getChildren().clear();
        loading.setVisible(true);
        JSONObject objJSON = new JSONObject("{}");
        objJSON.put("query", queryWhere);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles", objJSON.toString(),
                (response) -> {
                    JSONObject objResponse = new JSONObject(response);
                    //JSONArray JSONlist = objResponse.get("");
                    JSONArray jsonArray = objResponse.getJSONArray("message");
                    System.out.println("Recibido del server:" +jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        addPersona(jsonArray.getJSONObject(i));
                    }
                    loading.setVisible(false);
                });
    }

    @FXML
    public void getUsers(){
        getUsers("");
    }
    
    private void addPersonaTest(String id, String name) {
        try {
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();
            itemController.setName(name);
            itemController.setNumber(id);
            vBoxList.getChildren().add(itemTemplate);
        } catch (Exception e) {
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

    /* Experimento, hay que quitar */

    public void pruebaTransacciones(){
        try{
            URL resource = this.getClass().getResource("./assets/transactionItem.fxml");
            
            clearTransactions();
            for (int i=1;i<10;i++){
                FXMLLoader loader = new FXMLLoader(resource);
                Parent transaction = loader.load();
                ControllerTransaction transactionController = loader.getController();
                transactionController.setPayer(generateRandomString()+i);
                transactionController.setReceiver(generateRandomString()+i);
                transactionController.setAmount("Amount "+i);
                transactionController.setDate("Date "+i);
                vBoxTransactions.getChildren().add(transaction);
            }
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