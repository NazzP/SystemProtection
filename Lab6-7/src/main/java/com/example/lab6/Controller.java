package com.example.lab6;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static java.lang.Math.sqrt;
public class Controller {

    @FXML
    public static AnchorPane scenePane;
    static Stage stage;

    // Function to implement Hill Cipher
    public void encrypt()
    {

        // Get the message to be encrypted
        String message = TextFieldMessage.getText();
        int messageLength = message.length();

        // Get the key
        String key = TextFieldKey.getText();
        int keyRootLength = (int) sqrt(key.length());

        if (keyRootLength != messageLength){
            getAlert();
        } else {

            // Get key matrix from the key string
            double[][] keyMatrix = new double[keyRootLength][keyRootLength];
            getKeyMatrix(key, keyMatrix, keyRootLength);

            double[] messageVector = new double[messageLength];

            // Generate vector for the message
            for (int i = 0; i < messageLength; i++) {
                if (message.charAt(i) > 95) {
                    messageVector[i] = (message.charAt(i)) % 97;
                } else {
                    messageVector[i] = (message.charAt(i)) % 65;
                }
            }

            double[] cipherMatrix = new double[messageLength];

            // Following function generates
            // the encrypted vector
            encryption(cipherMatrix, keyMatrix, messageVector);

            String CipherText = "";

            // Generate the encrypted text from
            // the encrypted vector
            for (int i = 0; i < messageLength; i++) {
                CipherText += (char) (cipherMatrix[i] + 65);
            }

            // Finally print the ciphertext
            TextFieldResult.setVisible(true);
            DecryptButton.setVisible(true);
            TextFieldResult.setText(CipherText);
        }
    }

    private void getKeyMatrix(String key, double[][] keyMatrix, int sqrtLength)
    {
        int k = 0;
        for (int i = 0; i < sqrtLength; i++)
        {
            for (int j = 0; j < sqrtLength; j++)
            {
                keyMatrix[i][j] = (key.charAt(k)) % 65;
                k++;
            }
        }
    }

    // Following function encrypts the message
    private void encryption(double cipherMatrix[],
                            double keyMatrix[][],
                            double messageVector[])
    {
        int m = keyMatrix.length;
        int n = keyMatrix[0].length;
        if (n != messageVector.length) {
            throw new IllegalArgumentException("Matrix and vector dimensions don't match");
        }
        for (int i = 0; i < m; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += keyMatrix[i][j] * messageVector[j];
            }
            cipherMatrix[i] = sum;
            cipherMatrix[i] %= 26;
        }
    }

    private void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void decrypt() {
        String resultMessage = TextFieldResult.getText();
        int messageLength = resultMessage.length();

        String key = TextFieldKey.getText();
        int keyRootLength = (int) sqrt(key.length());

        double[][] keyMatrix = new double[keyRootLength][keyRootLength];
        getKeyMatrix(key, keyMatrix, keyRootLength);

        System.out.println("----- A -----");
        printMatrix(keyMatrix);


        keyMatrix = cofact(keyMatrix, keyRootLength, keyMatrix);

        System.out.println("----- A^-1 ----- for Hill cipher ( using mod26 )");
        printMatrix(keyMatrix);




        double[] messageVector = new double[messageLength];

        // Generate vector for the message
        for (int i = 0; i < messageLength; i++) {
            messageVector[i] = (resultMessage.charAt(i)) % 65;
        }

        double[] cipherMatrix = new double[messageLength];

        // Following function generates
        // the encrypted vector
        encryption(cipherMatrix, keyMatrix, messageVector);

        String CipherText = "";

        // Generate the encrypted text from
        // the encrypted vector
        for (int i = 0; i < messageLength; i++) {
            CipherText += (char) (cipherMatrix[i] + 65);
        }

        TextFieldResultMessage.setVisible(true);
        TextFieldResultMessage.setText(CipherText);
    }

    private double calDeterminant(double[][] A, int N)
    {
        double resultOfDet;
        switch (N) {
            case 1:
                resultOfDet = A[0][0];
                break;
            case 2:
                resultOfDet = A[0][0] * A[1][1] - A[1][0] * A[0][1];
                break;
            default:
                resultOfDet = 0;
                for (int j1 = 0; j1 < N; j1++)
                {
                    double m[][] = new double[N - 1][N - 1];
                    for (int i = 1; i < N; i++)
                    {
                        int j2 = 0;
                        for (int j = 0; j < N; j++)
                        {
                            if (j == j1)
                                continue;
                            m[i - 1][j2] = A[i][j];
                            j2++;
                        }
                    }
                    resultOfDet += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1]
                            * calDeterminant(m, N - 1);
                }   break;
        }
        return resultOfDet;
    }

    public double[][] cofact(double[][] num, int f, double[][] km)
    {
        double b[][], fac[][];
        b = new double[f][f];
        fac = new double[f][f];
        int p, q, m, n, i, j;
        for (q = 0; q < f; q++)
        {
            for (p = 0; p < f; p++)
            {
                m = 0;
                n = 0;
                for (i = 0; i < f; i++)
                {
                    for (j = 0; j < f; j++)
                    {
                        b[i][j] = 0;
                        if (i != q && j != p)
                        {
                            b[m][n] = num[i][j];
                            if (n < (f - 2))
                                n++;
                            else
                            {
                                n = 0;
                                m++;
                            }
                        }
                    }
                }
                fac[q][p] = (int) Math.pow(-1, q + p) * calDeterminant(b, f - 1);
            }
        }
        return trans(fac, f, km);
    }
    double[][] trans(double[][] fac, int r, double[][] km)
    {
        int i, j;
        double b[][], inv[][];
        b = new double[r][r];
        inv = new double[r][r];
        int d = (int) calDeterminant(km, r);
        int mi = mi(d % 26);
        mi %= 26;
        if (mi < 0)
            mi += 26;
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                b[i][j] = fac[j][i];
            }
        }
        for (i = 0; i < r; i++)
        {
            for (j = 0; j < r; j++)
            {
                inv[i][j] = b[i][j] % 26;
                if (inv[i][j] < 0)
                    inv[i][j] += 26;
                inv[i][j] *= mi;
                inv[i][j] %= 26;
            }
        }
        //System.out.println("\nInverse key:");
        //matrixtoinvkey(inv, r);

        km = inv;
        return km;
    }
    public int mi(int d)
    {
        int q, r1, r2, r, t1, t2, t;
        r1 = 26;
        r2 = d;
        t1 = 0;
        t2 = 1;
        while (r1 != 1 && r2 != 0)
        {
            q = r1 / r2;
            r = r1 % r2;
            t = t1 - (t2 * q);
            r1 = r2;
            r2 = r;
            t1 = t2;
            t2 = t;
        }
        return (t1 + t2);
    }

    public void ClearAll(ActionEvent event) {
        TextFieldMessage.setText("");
        TextFieldKey.setText("");
        TextFieldResult.setText("");
        TextFieldResultMessage.setText("");
    }

    private void getAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PROGRAM INFO");
        alert.setHeaderText("u need to provide information for a program");
        alert.setContentText("keyRootLength should be equal to messageLength");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                stage = (Stage) scenePane.getScene().getWindow();
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    private Button DecryptButton;


    @FXML
    private TextField TextFieldKey;

    @FXML
    private TextField TextFieldMessage;

    @FXML
    private TextField TextFieldResult;

    @FXML
    private TextField TextFieldResultMessage;

}