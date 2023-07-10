package com.example.lab5;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

    SecretKey key1;

    @FXML
    private  TextField TextFieldEncryptedMessage;

    @FXML
    private  TextField TextFieldMessage;

    @FXML
    private  TextField TextFieldKey;

    @FXML
    private  TextField TextFieldResult;

    @FXML
    private  TextField TextFieldResultMessage;

    @FXML
    public static AnchorPane scenePane;
    static Stage stage;

    private static final String ALGORITHM = "DES";
    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";

    public void getKey() throws NoSuchAlgorithmException {
        key1 = generKey();
    }

    private void getAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PROGRAM INFO");
        alert.setHeaderText("u need to provide information for a program");
        alert.setContentText("Enter something in field message");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                stage = (Stage) scenePane.getScene().getWindow();
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void ClearAll() {
        TextFieldMessage.setText("");
        TextFieldEncryptedMessage.setText("");
        TextFieldResult.setText("");
        TextFieldResultMessage.setText("");
        TextFieldKey.setText("");
    }

    public void logout(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you wanna to save before exiting?");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }

    private IvParameterSpec generIV(int blockSize) {
        byte[] iv = new byte[blockSize];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public void encrypt() throws Exception {

        if (TextFieldMessage.getText().equals("")){
            getAlert();
        } else {
            byte[] plaintext = TextFieldMessage.getText().getBytes();

            SecretKey key = key1; ////

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = generIV(cipher.getBlockSize());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] ciphertext = new byte[iv.getIV().length + cipher.getOutputSize(plaintext.length)];
            System.arraycopy(iv.getIV(), 0, ciphertext, 0, iv.getIV().length);
            int numEncryptedBytes = cipher.update(plaintext, 0, plaintext.length, ciphertext, iv.getIV().length);
            cipher.doFinal(ciphertext, iv.getIV().length + numEncryptedBytes);

            TextFieldResult.setText(new String(ciphertext));
            TextFieldEncryptedMessage.setText(new String(ciphertext));
        }
    }

    private SecretKey generKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(Controller.ALGORITHM);
        keyGenerator.init(56);
        TextFieldKey.setText(new String(keyGenerator.generateKey().getEncoded()));
        return keyGenerator.generateKey();

    }



    public void decrypt() throws Exception {
        if (TextFieldEncryptedMessage.getText().equals("")){
            getAlert();
        } else {
            byte[] ciphertext = TextFieldEncryptedMessage.getText().getBytes();
            SecretKey key = key1;

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(ciphertext, 0, cipher.getBlockSize());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] plaintext = new byte[cipher.getOutputSize(ciphertext.length - iv.getIV().length)];
            int numDecryptedBytes = cipher.update(ciphertext, iv.getIV().length, ciphertext.length - iv.getIV().length, plaintext, 0);

            TextFieldResultMessage.setText(TextFieldMessage.getText());
        }
    }
}