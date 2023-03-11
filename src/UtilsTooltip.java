import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UtilsTooltip {

    public static void setTooltipDelay(Button button, Tooltip tooltip, double seconds){
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        button.setTooltip(tooltip);
        button.setOnMouseEntered(event -> delay.play());
        button.setOnMouseExited(event -> delay.stop());
        button.setOnMouseExited(event -> {
            delay.stop();
            tooltip.hide();
        });
        delay.setOnFinished(event -> {
            double x = button.localToScreen(button.getBoundsInLocal()).getMinX() + button.getWidth();
            double y = button.localToScreen(button.getBoundsInLocal()).getMinY() + button.getHeight() / 2;
            tooltip.show(button, x, y);
        });
        button.setOnMouseClicked(event -> tooltip.hide());
        // button.setOnMouseMoved(event -> tooltip.hide());
        button.setOnMouseMoved(event -> {
            if (tooltip.isShowing()) {
                double tooltipX = tooltip.getAnchorX() + tooltip.getX();
                double tooltipY = tooltip.getAnchorY() + tooltip.getY();
                double tooltipWidth = tooltip.getWidth();
                double tooltipHeight = tooltip.getHeight();
                double mouseX = event.getScreenX();
                double mouseY = event.getScreenY();
                
                if (mouseX < tooltipX || mouseX > tooltipX + tooltipWidth
                    || mouseY < tooltipY || mouseY > tooltipY + tooltipHeight) {
                    tooltip.hide();
                    delay.stop();
                } else {
                    delay.playFromStart();
                }
            } else {
                delay.play();
            }
        });
    }

    public static void setTooltipDelay(ToggleButton button, Tooltip tooltip, double seconds){
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        button.setTooltip(tooltip);
        button.setOnMouseEntered(event -> delay.play());
        button.setOnMouseExited(event -> delay.stop());
        button.setOnMouseExited(event -> {
            delay.stop();
            tooltip.hide();
        });
        delay.setOnFinished(event -> {
            double x = button.localToScreen(button.getBoundsInLocal()).getMinX() + button.getWidth();
            double y = button.localToScreen(button.getBoundsInLocal()).getMinY() + button.getHeight() / 2;
            tooltip.show(button, x, y);
        });
        button.setOnMouseClicked(event -> checkTooltipVisibility(event, tooltip, delay));

        // button.setOnMouseMoved(event -> tooltip.hide());
        button.setOnMouseMoved(event -> checkTooltipVisibility(event, tooltip, delay));
    }

    private static void checkTooltipVisibility(MouseEvent event, Tooltip tooltip, PauseTransition delay) {
        if (tooltip.isShowing()) {
            double tooltipX = tooltip.getAnchorX() + tooltip.getX();
            double tooltipY = tooltip.getAnchorY() + tooltip.getY();
            double tooltipWidth = tooltip.getWidth();
            double tooltipHeight = tooltip.getHeight();
            double mouseX = event.getScreenX();
            double mouseY = event.getScreenY();
    
            if (mouseX < tooltipX || mouseX > tooltipX + tooltipWidth
                || mouseY < tooltipY || mouseY > tooltipY + tooltipHeight) {
                tooltip.hide();
                delay.stop();
            } else {
                delay.playFromStart();
            }
        } else {
            delay.play();
        }
    }
    
}
