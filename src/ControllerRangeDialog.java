import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/* Para obtener rangos, como necesito un dialogo
con dos campos de texto, al final he creado una a mano
 */

public class ControllerRangeDialog{

    @FXML
    private TextField campMax, campMin;

    @FXML
    private Label textInfo;
    
    @FXML
    private void acceptData(){
        String max = campMax.getText();
        String min = campMin.getText();
        System.out.println("Maxim: " + max + " Minim: " + min);
    }

    @FXML
    private void cancel(){
        campMax.setText("");
        campMax.setText("");
    }
}
