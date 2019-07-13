package com.dolany.mom.gui;

import com.dolany.mom.common.FileController;
import com.dolany.mom.common.JavaFxDispatchService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.dolany.mom.common.CustomKeyListener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;

public class FXMLController {

    private Preferences prefs;

    private static String saveLocationPref = "SAVE_LOCATION_PREF";
    private static String resourceLocationPref = "RESOURCE_LOCATION_PREF";
    private static String globalHotkeyPref = "GLOBAL_HOTKEY_PREF";

    @FXML
    ComboBox<String> slotSelect;

    @FXML
    TextField savesLocationField;

    @FXML
    TextField resourcesLocationField;

    @FXML
    Button buttonSaveLocations;

    @FXML
    Button buttonLoadSave;

    @FXML
    ListView<String> resourceList;

    @FXML
    CheckBox globalHotkeyCheckbox; 
    
    @FXML
    private void handleButtonAction(ActionEvent event) {

    }

    @FXML
    private void updateLocations() {

        List<String> saveResources = FileController.getFolders(resourcesLocationField.getText());

        prefs.put(saveLocationPref, savesLocationField.getText());
        prefs.put(resourceLocationPref, resourcesLocationField.getText());

        resourceList.getItems().removeAll(resourceList.getItems());
        resourceList.getItems().addAll(saveResources);
    }

    @FXML
    private void loadSave() {

        if(savesLocationField.getText().isEmpty() || resourcesLocationField.getText().isEmpty()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Please fill out both input fields!");
            errorAlert.showAndWait();
            return;
        }

        
        String toDir = FileController.buildPath(savesLocationField.getText(), slotSelect.getSelectionModel().getSelectedItem());
        String fromDir = FileController.buildPath(resourcesLocationField.getText(), resourceList.getSelectionModel().getSelectedItem());

        FileController.deleteFiles(toDir);
        FileController.copyFiles(fromDir, toDir);
        System.out.println("Loading from " + fromDir + " to " + toDir);
    }

    @FXML
    private void changeGlobalHotkeySettings(ActionEvent event) {
        boolean checked = globalHotkeyCheckbox.isSelected();
        prefs.put(globalHotkeyPref, String.valueOf(checked));

        changeGlobalHotkeySettings(checked);
    }

    private void changeGlobalHotkeySettings(boolean checked) {
        if(checked) {
            GlobalScreen.addNativeKeyListener( new CustomKeyListener(slotSelect, savesLocationField,  resourcesLocationField, resourceList));
            
            try {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.registerNativeHook();
                    GlobalScreen.setEventDispatcher(new JavaFxDispatchService());
                }
            }
            catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
    
                System.exit(1);
            }
        } else {
            try {
                if (GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.unregisterNativeHook();
                }
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
        }
    }

    @FXML
    public void initialize() {
        prefs = Preferences.userRoot().node(this.getClass().getName());

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        boolean globalHotkeys = Boolean.valueOf(prefs.get(globalHotkeyPref, "false"));
        globalHotkeyCheckbox.setSelected(globalHotkeys);
        changeGlobalHotkeySettings(globalHotkeys);

        slotSelect.getItems().removeAll(slotSelect.getItems());
        slotSelect.getItems().addAll("Slot1","Slot2","Slot3");
        slotSelect.getSelectionModel().select("Slot1");

        savesLocationField.setText(prefs.get(saveLocationPref, ""));
        resourcesLocationField.setText(prefs.get(resourceLocationPref, ""));

        if (!resourcesLocationField.getText().isEmpty()) {
            List<String> saveResources = FileController.getFolders(resourcesLocationField.getText());

            resourceList.getItems().removeAll(resourceList.getItems());
            resourceList.getItems().addAll(saveResources);
        }
    }    
}