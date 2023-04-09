package com.example.lab_1;

        import java.net.URL;
        import java.util.ResourceBundle;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.*;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private AnchorPane scenePane;
    Stage stage;

    private final String ALPHABET = "abcdefghijklvnopqrstuvwxyz";

    public void ClearAll(ActionEvent event) {
        TextFieldMessage.setText("");
        TextFieldResult.setText("");
        TextFieldStep.setText("");
        ChoiceBoxStep.setValue("");
    }

    public void Encrypt(ActionEvent event){
        TextFieldResult.setText("");
        try{
            int step = Integer.parseInt(TextFieldStep.getText());
            for (int i = 0; i < TextFieldMessage.getText().length(); i++) {
                if (!ALPHABET.contains(Character.toString(TextFieldMessage.getText().toLowerCase().toCharArray()[i]))) {
                    TextFieldResult.setText(TextFieldResult.getText() + TextFieldMessage.getText().toCharArray()[i]);
                    continue;
                }
                int index = ALPHABET.indexOf(TextFieldMessage.getText().toLowerCase().toCharArray()[i]);
                if (ChoiceBoxStep.getValue() == "+") {
                    index += step;
                    index %= 26;
                    TextFieldResult.setText(TextFieldResult.getText() + ALPHABET.toCharArray()[index]);
                } else {
                    index -= step;
                    index %= 26;
                    if(index < 0)
                        index += ALPHABET.length();
                    TextFieldResult.setText(TextFieldResult.getText() + ALPHABET.toCharArray()[index]);
                }
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("NumberFormatException");
            alert.setHeaderText("NumberFormatException");
            alert.setContentText("Check the fields again and input correctly");

            if(alert.showAndWait().get() == ButtonType.OK){
                stage = (Stage) scenePane.getScene().getWindow();
            }

        }
    }

        @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> ChoiceBoxStep;
    private String[] choice = {"+" , "-"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxStep.getItems().addAll(choice);
        ChoiceBoxStep.setOnAction(this::getChoice);
    }

    public void getChoice(ActionEvent event){
        String myChoice = ChoiceBoxStep.getValue();

    }

    public void logout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you wanna to save before exiting?");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private TextField TextFieldMessage;

    @FXML
    private TextField TextFieldResult;

    @FXML
    private TextField TextFieldStep;

    @FXML
    void initialize() {

    }

}

