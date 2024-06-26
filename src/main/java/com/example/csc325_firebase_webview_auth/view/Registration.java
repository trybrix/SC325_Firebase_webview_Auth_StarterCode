package com.example.csc325_firebase_webview_auth.view;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Registration{

    @FXML
    private TextField fnTextField;
    @FXML
    private TextField lnTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField verifyTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem goBackMenuItem;
    @FXML
    private Button registerButton;

    public void handleCloseMenuItem(){
        System.out.println("handleCloseMenuItem called");
        Platform.exit();
    }

    public void handleGoBackMenuItem() throws IOException{
        System.out.println("handleGoBackMenuItem called");
        App.setRoot("/files/AccessFBView.fxml");
    }


    public void handleRegisterButton(){
        System.out.println("handleRegisterButton called");

        String firstName = fnTextField.getText();
        String lastName = lnTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String verifyEmail = verifyTextField.getText();
        String password = passwordTextField.getText();



        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || verifyEmail.isEmpty()|| phoneNumber.isEmpty()){
            System.out.println("Please fill in all fields");
        }else if(!email.equals(verifyEmail)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Emails don't match");
            System.out.println("Emails don't match try again");
        }

        registerUser(firstName, lastName, email, password,phoneNumber);


        System.out.println("First Name: "+ firstName);
        System.out.println("Last Name: "+ lastName);
        System.out.println("Email: "+ email);
        System.out.println("Password: "+ password);
        System.out.println("Phone Number: "+ phoneNumber );

        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Complete");
        alert.showAndWait();
        fnTextField.clear();
        lnTextField.clear();
        phoneNumberTextField.clear();
        emailTextField.clear();
        passwordTextField.clear();

    }

    public void registerUser(String firstName, String lastName, String phonenumber,String email, String password){
        try{
            DocumentReference docRef = App.fstore.collection("Reference_Registration").document(UUID.randomUUID().toString());

            Map<String, Object> userData = new HashMap<>();
            userData.put("First Name", firstName);
            userData.put("Last Name", lastName);
            userData.put("Phone number",phonenumber);
            userData.put("Email", email);
            userData.put("Password", password);

            ApiFuture<WriteResult> result = docRef.set(userData);

            System.out.println ("User information added to Firebase successfully!");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}


