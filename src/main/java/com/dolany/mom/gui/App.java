package com.dolany.mom.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("test.fxml"));
        
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        stage.setTitle("MoM Organizer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        try {
            if (GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}