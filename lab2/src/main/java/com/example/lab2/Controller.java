package com.example.lab2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    @FXML
    private ChoiceBox<String> ChoiceBoxEncrypt;
    private String[] choice = {"Encrypt" , "Decrypt"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxEncrypt.getItems().addAll(choice);
        ChoiceBoxEncrypt.setOnAction(this::getChoice);
        ChoiceBoxEncrypt.setValue("Encrypt");
    }

    public void ClearAll(ActionEvent event) {
        TextFieldMessage.setText("");
        TextFieldResult.setText("");
        TextFieldKey.setText("");
        ChoiceBoxEncrypt.setValue("Encrypt");
    }

    private void getChoice(ActionEvent event){
        String myChoice = ChoiceBoxEncrypt.getValue();
    }

    private void encrypt(){
        String text = TextFieldMessage.getText();
        String key = TextFieldKey.getText();

        for(int i = 0; i < text.length() % key.length(); i++){
            text += text.charAt(i);
        }

        for(int i=0; i < text.length() ;i += key.length()){
            char[] buffer = new char[key.length()];

            for (int j = 0; j < buffer.length; j++){
                buffer[Character.getNumericValue(key.charAt(j) - 1 )] = text.charAt(i+j);
            }

            TextFieldResult.setText(TextFieldResult.getText() + new String(buffer));
        }
    }

    private void decrypt(){
        String text = TextFieldMessage.getText();
        String key = TextFieldKey.getText();

        for(int i=0; i < text.length() ;i += key.length()){
            char[] buffer = new char[key.length()];

            for (int j = 0; j < buffer.length; j++){
                buffer[j] = text.charAt(Character.getNumericValue(key.charAt(j)) - 1);
            }

            TextFieldResult.setText(TextFieldResult.getText() + new String(buffer));
        }
    }

    public void Choice(ActionEvent event){
        TextFieldResult.setText("");
        if (ChoiceBoxEncrypt.getValue() == "Encrypt") {
            encrypt();
        } else {
            decrypt();
        }
    }


    @FXML
    private TextField TextFieldKey;


    @FXML
    private TextField TextFieldMessage;

    @FXML
    private TextField TextFieldResult;

}

