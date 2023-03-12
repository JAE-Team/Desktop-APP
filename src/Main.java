import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

    public static UtilsWS socketClient;

    public static int port = 443;
    public static String protocol = "https";
    public static String host = "server-production-e914.up.railway.app";

    // Exemple de configuració per Railway
    // public static int port = 443;
    // public static String protocol = "https";
    // public static String host = "server-production-46ef.up.railway.app";
    // public static String protocolWS = "wss";

    // Camps JavaFX a modificar
    public static Label consoleName = new Label();
    public static Label consoleDate = new Label();
    public static Label consoleBrand = new Label();
    public static ImageView imageView = new ImageView();

    public static void main(String[] args) {
        // Iniciar app JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        final int windowWidth = 1050;
        final int windowHeight = 800;

        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "View0", "./assets/view0.fxml");
        UtilsViews.addView(getClass(), "ViewValidation", "./assets/verificationDNI.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.onCloseRequestProperty(); // Call close method when closing window
        stage.setTitle("CORN DESKTOP");
        stage.setMinWidth(windowWidth);
        stage.setMinHeight(windowHeight);
        stage.show();

        String path = System.getProperty("user.dir") +
                File.separator + "assets" + File.separator + "logo1.png";
        stage.getIcons().add(new Image(path));
    }

    @Override
    public void stop() {
        if (socketClient != null) {
            socketClient.close();
        }
        System.exit(1); // Kill all executor services
    }
}
