package miklos.mayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is responsible to start the application
 */
public class Starter extends Application {

    private FXMLLoader menuLoader;

    /**
     * The entry point of the program. Displays the menu to the user in the first place.
     *
     * @param args the starting arguments of the program.
     */
    public static void main(String[] args) {
        Application.launch(Starter.class, args);
    }

    /**
     * Initialize the user interface
     *
     * @param primaryStage the stage to set the user interface upon
     * @throws Exception if any problem occurs, throw it back to the main method
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = menuLoader.load();
        primaryStage.setTitle("Shop");
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setMinWidth(778);
        primaryStage.setMinHeight(300);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * The last method to call before the application terminates.
     * Save the database
     *
     * @throws Exception if any problem occurs, throw it back to the main method
     */
    @Override
    public void stop() throws Exception {
        ((Menu) menuLoader.getController()).saveCatalogue();
        super.stop();
    }
}
