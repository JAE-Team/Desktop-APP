import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerTransaction{

    /* Las transacciones seran como los listItem
     * de momento he hecho que muestren nombre del que paga
     * nombre del que recibe, cantidad y fecha
     * 
     * la fecha puede ser cualquera de las 3, lo idoneo quizas la ultima, finishPayment
     */

    @FXML
    private Label fieldPayer, fieldReceiver, fieldAmount, fieldStatus, fieldDate;

    public void setPayer(String payer){
        fieldPayer.setText(payer);
    }

    public void setReceiver(String receiver) {
        fieldReceiver.setText(receiver);
    }

    public void setStatus(String status) {
        fieldStatus.setText(status);
    }
    
    public void setAmount(String amount){
        fieldAmount.setText(amount);
    }

    public void setDate(String date){
        fieldDate.setText(date);
    }
}