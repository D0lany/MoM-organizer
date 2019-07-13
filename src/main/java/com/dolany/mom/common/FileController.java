package com.dolany.mom.common;

import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileController {

    public FileController() {};

    public List<String> getFolders(String path) {
        File dir = new File(path);

        if(!dir.exists() || !dir.isDirectory()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Directory "+ dir.getAbsolutePath() + "does not exist!");
            errorAlert.showAndWait();
            return new ArrayList<>();
        }

        String[] directories = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        
        return Arrays.asList(directories);
    }

    public void deleteFiles(String path) {
        File dir = new File(path);

        if(!dir.exists() || !dir.isDirectory()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Directory "+ dir.getAbsolutePath() + "does not exist!");
            errorAlert.showAndWait();
            return;
        }

        if(dir.isDirectory()) {
            for(File file: dir.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    public void copyFiles(String from, String to) {
        File fromDir = new File(from);
        File toDir = new File(to);

        if(!fromDir.exists() || !fromDir.isDirectory()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Directory "+ fromDir.getAbsolutePath() + "does not exist!");
            errorAlert.showAndWait();
            return;
        }

        if(!toDir.exists() || !toDir.isDirectory()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Directory "+ toDir.getAbsolutePath() + "does not exist!");
            errorAlert.showAndWait();
            return;
        }

        try {
            String[] children = fromDir.list();
            for (int i=0; i<children.length; i++) {
                InputStream in = new FileInputStream(new File(buildPath(from,children[i])));
                OutputStream out = new FileOutputStream(new File(buildPath(to,children[i])));

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String buildPath(String one, String another) {
        if(one.endsWith("\\")) {
            one = one.substring(0, one.length()-2);
        } 
        if(another.startsWith("\\")) {
            another = another.substring(1, another.length()-1);
        } 
        return one + "\\" + another;
    }
}