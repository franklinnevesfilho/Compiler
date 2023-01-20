package com.example.compiler;

import com.example.compiler.model.PreProcessor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    private final PreProcessor processor = new PreProcessor();

    @FXML
    private TextArea resultTxt;
    @FXML
    private TextArea currentFile;
    private String filePath;

    public MainScreenController(){}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentFile.setEditable(true);
        resultTxt.setEditable(false);
    }

    @FXML
    private void uploadFile() throws Exception {
        FileChooser fChooser = new FileChooser();
        fChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File selectedFile = fChooser.showOpenDialog(null);
        if(selectedFile != null){
            this.filePath = selectedFile.getPath();
           BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
           String line;
           while((line = reader.readLine()) != null){
               currentFile.appendText(line + "\n");
           }

        }else{
            new Alert(Alert.AlertType.WARNING, "File Upload Failed");
        }
    }

    @FXML
    private void verify(){
        if(filePath != null){
            processor.setFile(filePath);
        }else{
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt"));
                writer.write(currentFile.getText());
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            processor.setFile("Output.txt");
        }

        processor.process();
        if(processor.passed){
            resultTxt.lookup(".content").setStyle("-fx-background-color: green;");
        }else{
            resultTxt.lookup(".content").setStyle("-fx-background-color: red;");
        }
    }
}
