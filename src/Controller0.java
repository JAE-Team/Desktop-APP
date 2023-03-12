import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private VBox vBoxTransactions = new VBox();

    @FXML
    private ToggleButton buttonFilterState, buttonFilterBalances, buttonFilterTransactions;

    @FXML
    private Button buttonValidateUser, buttonResetFilters, buttonSearch;

    private Tooltip tooltipValidate = new Tooltip(
            "Obrir la vista per validar el compte d'un usuari \na partir de les fotos del DNI que ha pujat"),
            tooltipReset = new Tooltip("Reiniciar tots els filtres"),
            tooltipSearch = new Tooltip("Fer una cerca d'usuaris amb els filtres seleccionats"),
            tooltipState = new Tooltip("Filtrar segons l'estat de verificació del compte l'usuari"),
            tooltipBalance = new Tooltip(
                    "Filtrar els usuaris que tinguin un Balanç \nde moneda social entre els dos valors especificats"),
            tooltipTransactions = new Tooltip(
                    "Filtrar els usuaris que tinguin \nuna quantitat de transaccions compresa \nentre el maxim i el minim de transaccions");

    @FXML
    private ChoiceBox<String> choiceBoxStatus;
    private String[] filtersOptions = { "All", "Not\nverified", "Waiting\nVerification", "Verification\nAccepted",
            "Verification\nRejected" };

    @FXML
    private TextField maxBalances, minBalances, maxTransactions, minTransactions;

    private static Controller0 instance;

    private JSONObject filters = new JSONObject("{}");
    /*
     * "filters" : {"filterBalance":"0;100", "filterTransactions":"0;10",
     * "filterStatus":"WAITING_VERIFICATION"}
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getUsers();

        buttonFilterState.setSelected(false);
        buttonFilterBalances.setSelected(false);
        buttonFilterTransactions.setSelected(false);

        choiceBoxStatus.getItems().addAll(filtersOptions);
        choiceBoxStatus.setValue(filtersOptions[0]);
        choiceBoxStatus.setStyle("-fx-font-size: 13px;");

        setAllTooltips();
        instance = this;
    }

    @FXML
    private void setView1() {
        UtilsViews.setViewAnimating("View0");
    }

    @FXML
    private void choiceBoxAction() {
    }

    /*
     * Cada uno de los 3 Botones de filtro tendra 2 metodos, uno para cuando se
     * clica añadir un filtro,
     * otro cuando se suelta para quitar el filtro, actuaran sobre el JSONObject
     * filters
     */

    @FXML
    private void statesPressed(ActionEvent event) {
        if (buttonFilterState.isSelected()) {
            buttonFilterState.setStyle("-fx-background-color: #a6a6a6; -fx-border-color: transparent;");
        } else {
            buttonFilterState
                    .setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px;");
        }
    }

    @FXML
    private void balancesPressed(ActionEvent event) {
        if (buttonFilterBalances.isSelected()) {
            buttonFilterBalances.setStyle("-fx-background-color: #a6a6a6; -fx-border-color: transparent;");
            /*
             * String maxBalancesInput = maxBalances.getText();
             * String minBalancesInput = minBalances.getText();
             * setFilterBalance(minBalancesInput + ";" + maxBalancesInput);
             */
        } else {
            buttonFilterBalances
                    .setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px;");
        }
    }

    @FXML
    private void transactionsPressed(ActionEvent event) {
        if (buttonFilterTransactions.isSelected()) {
            buttonFilterTransactions.setStyle("-fx-background-color: #a6a6a6; -fx-border-color: transparent;");
            /*
             * String maxTransactionsInput = maxTransactions.getText();
             * String minTransactionsInput = minTransactions.getText();
             * setFilterTransactions(minTransactionsInput + ";" + maxTransactionsInput);
             */
        } else {
            buttonFilterTransactions
                    .setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px;");
        }
    }

    private void setFilterStatus(String status) {
        removeFilterStatus();
        filters.append("filterStatus", status);
    }

    private void removeFilterStatus() {
        filters.remove("filterStatus");
    }

    private void setFilterBalance(String balance) {
        removeFilterBalance();
        filters.append("filterBalance", balance);
    }

    private void removeFilterBalance() {
        filters.remove("filterBalance");

    }

    private void setFilterTransactions(String transactions) {
        removeFilterTransactions();
        filters.append("filterTransactions", transactions);
    }

    private void removeFilterTransactions() {
        filters.remove("filterTransactions");

    }

    /*
     * Metodo para implementar la spec 35, nos abrira una vista donde veremos las
     * fotografias en grande y nos saldra el desplegable
     */
    @FXML
    private void validateUser() {
        if (idField.getText() == null || idField.getText() == "") {
            return;
        } else {
            UtilsViews.setViewAnimating("ViewValidation");
            ControllerValidation ctrl = (ControllerValidation) UtilsViews.getController("ViewValidation");
            ctrl.setId(idField.getText());
        }
    }

    private void openRangeDialog() {
        try {
            // Load the rangeDialog FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./assets/rangeDialog.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Create a new stage to display the scene
            Stage stage = new Stage();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Metodo que comprueba todos los campos y atualiza los filtros que enviaremos
     */
    private void updateFilters() throws Exception {
        if (buttonFilterState.isSelected()) {
            String filterStatus = choiceBoxStatus.getValue().toString().replace("\n", " ");
            setFilterStatus(filterStatus);

        } else {
            removeFilterStatus();
        }

        if (buttonFilterBalances.isSelected()) {
            String maxBalancesInput = maxBalances.getText();
            String minBalancesInput = minBalances.getText();

            try {
                double max = Double.parseDouble(maxBalancesInput);
                double min = Double.parseDouble(minBalancesInput);

                if (max >= min) {
                    setFilterBalance(minBalancesInput + ";" + maxBalancesInput);
                } else {
                    UtilsAlerts.alertError("Error en el filtre de balanços",
                            "La quantitat maxima de balanços ha de ser major o igual que el valor mínim de transaccions");
                    throw new Exception("Filter Balance Error");
                }
            } catch (NumberFormatException e) {
                if (maxBalancesInput.contains(",") || minBalancesInput.contains(",")) {
                    UtilsAlerts.alertError("Error en el filtre de balanços",
                            "El separador the que ser una coma");
                } else {
                    UtilsAlerts.alertError("Error en el filtre de balanços",
                            "Un dels valors introduïts no és un número");
                }

                throw new Exception("Filter Balance Error");
            }

        } else {
            removeFilterBalance();
        }

        if (buttonFilterTransactions.isSelected()) {
            String maxTransactionsInput = maxTransactions.getText();
            String minTransactionsInput = minTransactions.getText();

            try {
                int max = Integer.parseInt(maxTransactionsInput);
                int min = Integer.parseInt(minTransactionsInput);

                if (max >= min) {
                    setFilterTransactions(minTransactionsInput + ";" + maxTransactionsInput);
                } else {
                    UtilsAlerts.alertError("Error en el filtre de transaccions",
                            "El valor màxim de transaccions ha de ser major o igual que el valor mínim de transaccions");
                    throw new Exception("Filter Transactions Error");
                }
            } catch (NumberFormatException e) {
                if (maxTransactionsInput.contains(",") || maxTransactionsInput.contains(".")
                        || minTransactionsInput.contains(",") || minTransactionsInput.contains(".")) {
                    UtilsAlerts.alertError("Error en el filtre de balanços",
                            "El valor no pot ser decimal");
                } else {
                    UtilsAlerts.alertError("Error en el filtre de transaccions",
                            "Un dels valors introduïts no és un número");
                }

                throw new Exception("Filter Transactions Error");
            }

        } else {
            removeFilterTransactions();
        }
    }

    /* Para resetear todos los filtros */
    @FXML
    private void resetFilters() {
        buttonFilterState.setSelected(false);
        buttonFilterBalances.setSelected(false);
        buttonFilterTransactions.setSelected(false);
        buttonFilterState.setStyle(
                "-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px; -fx-padding: -5;");
        buttonFilterBalances
                .setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px;");
        buttonFilterTransactions
                .setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #455A64; -fx-border-width: 3px;");
        removeFilterStatus();
        removeFilterBalance();
        removeFilterTransactions();
        maxBalances.setText("");
        minBalances.setText("");
        maxTransactions.setText("");
        minTransactions.setText("");
        choiceBoxStatus.setValue(filtersOptions[0]);
    }

    /*
     * El metodo de cargar perfiles requerira que se le especifique un filtro,
     * por lo que se le pasara el JSONObject filters
     * Cuando se apriete el boton, el metodo updateFilters revisara los botones y
     * campos para
     * establecer los filtros
     * 
     * Si algun dato de los filtros de transacciones o balances esta mal,
     * saltara el error y no continuara con el envio del post
     */
    @FXML
    public void getUsers() {
        try {
            updateFilters();
            vBoxList.getChildren().clear();
            loading.setVisible(true);
            JSONObject objJSON = new JSONObject("{}");
            objJSON.put("filters", this.filters);
            System.out.println("Post enviado:" + objJSON.toString());
            UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles",
                    objJSON.toString(),
                    (response) -> {
                        JSONObject objResponse = new JSONObject(response);
                        // JSONArray JSONlist = objResponse.get("");

                        JSONArray jsonArray = objResponse.getJSONArray("message");

                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            jsonObject.remove("anvers");
                            jsonObject.remove("revers");
                        }
                        System.out.println("Recibido del server: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            addPersona(jsonArray.getJSONObject(i));
                        }
                        loading.setVisible(false);
                    });
        } catch (Exception e) {
            return;
        }
    }

    private void addPersona(JSONObject persona) {
        try {
            URL resource = this.getClass().getResource("./assets/listItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerItem itemController = loader.getController();

            itemController.setPhone(String.valueOf(persona.get("userId")));
            itemController.setName(
                    String.valueOf(persona.get("userName")) + " " + String.valueOf(persona.get("userSurname")));
            itemController.setNumber(String.valueOf(persona.get("id")));
            itemController.setPerson(persona);
            /*
             * Creamos el item i le especificamos los campos que modificara en caso de
             * clicar-lo
             */
            vBoxList.getChildren().add(itemTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add template to the list
    }

    /*
     * Carga una unica transaccion, este metodo sera llamado al clicar
     * el ItemList del usuario tantas veces como transacciones tenga este
     */
    public void addTransaction(JSONObject transactionObject) {
        try {
            URL resource = this.getClass().getResource("./assets/transactionItem.fxml");
            FXMLLoader loader = new FXMLLoader(resource);
            Parent transaction = loader.load();
            ControllerTransaction transactionController = loader.getController();
            String payer = transactionObject.isNull("userOrigin") ? "Undefined"
                    : transactionObject.getString("originName")
                            + " " + transactionObject.getString("originSurname");
            transactionController.setPayer(payer);

            String receiver = transactionObject.isNull("userDestiny") ? "Undefined"
                    : transactionObject.getString("destinyName")
                            + " " + transactionObject.getString("destinySurname");
            transactionController.setReceiver(receiver);

            double amountValue = transactionObject.isNull("ammount") ? Double.NaN
                    : transactionObject.getDouble("ammount");
            String amount = Double.isNaN(amountValue) ? "Undefined" : String.valueOf(amountValue);
            transactionController.setAmount(amount);

            String status = transactionObject.isNull("accepted") ? "Undefined"
                    : transactionObject.getString("accepted");
            transactionController.setStatus(status);

            String date = transactionObject.isNull("timeSetup") ? "Undefined"
                    : transactionObject.getString("timeSetup");
            transactionController.setDate(date);

            vBoxTransactions.getChildren().add(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setView3() {
        UtilsViews.setViewAnimating("View3");
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public static Controller0 getInstance() {
        return instance;
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setSurname(String surname) {
        surnameField.setText(surname);
    }

    public void setMail(String mail) {
        mailField.setText(mail);
    }

    public void setPhone(String phone) {
        phoneField.setText(phone);
    }

    public void setAmmount(String ammount) {
        ammountField.setText(ammount);
    }

    public void clearTransactions() {
        vBoxTransactions.getChildren().clear();
    }

    /*
     * Codigo para implementar que cuando se deja sobre el cursor sobre un boton
     * aparezca
     * un texto explicativo que nos de mas información, los tooltips.
     */

    private void setAllTooltips() {
        UtilsTooltip.setTooltipDelay(buttonFilterState, tooltipState, 0.5);
        UtilsTooltip.setTooltipDelay(buttonFilterBalances, tooltipBalance, 0.5);
        UtilsTooltip.setTooltipDelay(buttonFilterTransactions, tooltipTransactions, 0.5);
        UtilsTooltip.setTooltipDelay(buttonResetFilters, tooltipReset, 0.5);
        UtilsTooltip.setTooltipDelay(buttonSearch, tooltipSearch, 0.5);
        UtilsTooltip.setTooltipDelay(buttonValidateUser, tooltipValidate, 0.5);
    }
}