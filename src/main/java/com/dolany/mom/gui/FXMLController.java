package com.dolany.mom.gui;

import com.dolany.mom.common.FileController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.util.List;
import java.util.prefs.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FXMLController {

    FileController fileController = new FileController();

    private Preferences prefs;

    private static String saveLocationPref = "SAVE_LOCATION_PREF";
    private static String resourceLocationPref = "RESOURCE_LOCATION_PREF";

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
    private void handleButtonAction(ActionEvent event) {

    }

    @FXML
    private void updateLocations() {

        List<String> saveResources = fileController.getFolders(resourcesLocationField.getText());

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

        
        String toDir = fileController.buildPath(savesLocationField.getText(), slotSelect.getSelectionModel().getSelectedItem());
        String fromDir = fileController.buildPath(resourcesLocationField.getText(), resourceList.getSelectionModel().getSelectedItem());

        fileController.deleteFiles(toDir);
        fileController.copyFiles(fromDir, toDir);
        System.out.println("Loading from " + fromDir + " to " + toDir);
    }

    @FXML
    public void initialize() {
        prefs = Preferences.userRoot().node(this.getClass().getName());

        slotSelect.getItems().removeAll(slotSelect.getItems());
        slotSelect.getItems().addAll("Slot1","Slot2","Slot3");
        slotSelect.getSelectionModel().select("Slot1");

        savesLocationField.setText(prefs.get(saveLocationPref, ""));
        resourcesLocationField.setText(prefs.get(resourceLocationPref, ""));

        if (!resourcesLocationField.getText().isEmpty()) {
            List<String> saveResources = fileController.getFolders(resourcesLocationField.getText());

            resourceList.getItems().removeAll(resourceList.getItems());
            resourceList.getItems().addAll(saveResources);
        }
    }    
}