package com.example.lab3;

import java.math.BigDecimal;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

    public Controller() {
    }

    @FXML
    public AnchorPane scenePane;
    Stage stage;

    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private String encodedMessage;
    private Map<Character, Double> alphabetPercentage = new LinkedHashMap<>();
    private Map<Character, Double> charCountMap = new LinkedHashMap<>();
    private Map<Character, Double> elementPercentageCount = new LinkedHashMap<>();

    public void Encrypt(ActionEvent event) {
        createAlphabetMap();
        encodedMessage = TextFieldMessage.getText().toLowerCase();
        if (encodedMessage.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PROGRAM INFO");
            alert.setHeaderText("u need to provide information for a program");
            alert.setContentText("Enter something in field message");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    stage = (Stage) scenePane.getScene().getWindow();
                } catch (NullPointerException e) {
                }
            }

        } else {
            countElements();
            countPercentage();
            TextFieldResult.setText(decode(compare()));
        }
    }


    public void ClearAll(ActionEvent event) {
        TextFieldMessage.setText("");
        TextFieldResult.setText("");
    }

    @FXML
    private TextField TextFieldMessage;

    @FXML
    private TextField TextFieldResult;


    private void createAlphabetMap() {
        alphabetPercentage.put('a', 7.25);
        alphabetPercentage.put('b', 1.25);
        alphabetPercentage.put('c', 3.5);
        alphabetPercentage.put('d', 4.25);
        alphabetPercentage.put('e', 12.75);
        alphabetPercentage.put('f', 3.0);
        alphabetPercentage.put('g', 2.0);
        alphabetPercentage.put('h', 3.5);
        alphabetPercentage.put('i', 7.75);
        alphabetPercentage.put('j', 0.25);
        alphabetPercentage.put('k', 0.5);
        alphabetPercentage.put('l', 3.75);
        alphabetPercentage.put('m', 2.75);
        alphabetPercentage.put('n', 7.75);
        alphabetPercentage.put('o', 7.5);
        alphabetPercentage.put('p', 2.75);
        alphabetPercentage.put('q', 0.5);
        alphabetPercentage.put('r', 8.5);
        alphabetPercentage.put('s', 6.0);
        alphabetPercentage.put('t', 9.25);
        alphabetPercentage.put('u', 3.0);
        alphabetPercentage.put('v', 1.5);
        alphabetPercentage.put('w', 1.5);
        alphabetPercentage.put('x', 0.5);
        alphabetPercentage.put('y', 2.25);
        alphabetPercentage.put('z', 0.25);
    }


    private void countElements() {
        String text = TextFieldMessage.getText().toLowerCase();
        charCountMap.putAll(alphabetPercentage);

        charCountMap.replaceAll((k, v) -> v * 0);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // if the character is already in the map, increment its count
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            }
            // otherwise, add the character to the map with a count of 1
        }
    }

    private void countPercentage() {
        double percent;

        for (int i = 0; i < 26; i++) {
            percent = (double) (charCountMap.get(alphabet.charAt(i))) * 100.0 / (double) encodedMessage.length();
            percent = new BigDecimal(Double.toString(percent)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            elementPercentageCount.put(alphabet.charAt(i), percent);
        }
    }

    private int compare() {
        Double[] idealArray = new Double[26];
        Double[] idealArraySorted = new Double[26];
        Double[] myArray = new Double[26];
        Double[] myArraySorted = new Double[26];

        alphabetPercentage.values().toArray(idealArray);
        alphabetPercentage.values().toArray(idealArraySorted);
        Arrays.sort(idealArraySorted, Comparator.reverseOrder());

        elementPercentageCount.values().toArray(myArray);
        elementPercentageCount.values().toArray(myArraySorted);
        Arrays.sort(myArraySorted, Comparator.reverseOrder());

        int idealIndex = Arrays.asList(idealArray).indexOf(idealArraySorted[0]);

        int offset = 0;
        boolean possible = false;

        for (int i = 0; i < 7 && !possible; i++) {
            int count = 0;
            int index = Arrays.asList(myArray).indexOf(myArraySorted[i]);
            offset = Math.abs(index - idealIndex);
            for (int j = 0; j < 7; j++) {
                if (j != i) {
                    int indexOfElement = Arrays.asList(myArray).indexOf(myArraySorted[j]);
                    indexOfElement -= offset;
                    if (indexOfElement < 0) {
                        indexOfElement += 26;
                    }
                    if (idealArray[indexOfElement] > 7.0) {
                        count++;
                    }
                }
            }
            if (count >= 4) {
                possible = true;
            }
        }
        return offset;
    }

    private String decode(int offset) {
        String message = "";
        int realIndex;
        for (int i = 0; i < encodedMessage.length(); i++) {
            realIndex = (alphabet.indexOf(encodedMessage.charAt(i))) - offset;
            if (realIndex < 0) {
                realIndex += 26;
            }
            message += alphabet.charAt(realIndex);
        }

        return message;
    }
}