package com.dolany.mom.common;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CustomKeyListener implements NativeKeyListener {

    ComboBox<String> slotSelect;

    TextField savesLocationField;

    TextField resourcesLocationField;

    ListView<String> resourceList;

    public CustomKeyListener(ComboBox<String> slotSelect, TextField savesLocationField, TextField resourcesLocationField, ListView<String> resourceList) {
        this.slotSelect = slotSelect;
        this.savesLocationField = savesLocationField;
        this.resourcesLocationField = resourcesLocationField;
        this.resourceList = resourceList;
    }

	public void nativeKeyPressed(NativeKeyEvent e) {

		if (e.getKeyCode() == NativeKeyEvent.VC_0) {
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
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}