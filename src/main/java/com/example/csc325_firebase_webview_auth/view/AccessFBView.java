package com.example.csc325_firebase_webview_auth.view;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AccessFBView {

    @FXML
    private TextField fnField;
    @FXML
    private TextField lnField;
    @FXML
    private TextField depField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField imgField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;
    @FXML
    private Button switchButton;
    @FXML
    private TableView<Map<String, String>> tableView;
    @FXML
    private TableColumn<Map<String, String>, String> colFN;
    @FXML
    private TableColumn<Map<String, String>, String> colLN;
    @FXML
    private TableColumn<Map<String, String>, String> colDep;
    @FXML
    private TableColumn<Map<String, String>, String> colMajor;
    @FXML
    private TableColumn<Map<String, String>, String> colEmail;
    @FXML
    private TableColumn<Map<String, String>, String> colImg;

    private ObservableList<Map<String, String>> listOfUsers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tableView.setItems(listOfUsers);
        colFN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("First Name")));
        colLN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Last Name")));
        colDep.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Department")));
        colMajor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Major")));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Email")));
        colImg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Image URL")));
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

    @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }


    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("Switching to HTML View");
        App.setRoot("/files/WebContainer.fxml");
    }

    @FXML
    private void handleClose(ActionEvent event) {
        System.out.println("Closing application");
        System.exit(0);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        System.out.println("Delete action triggered");
        Map<String, String> selectedRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            String firstName = selectedRecord.get("First Name");
            ApiFuture<QuerySnapshot> future = App.fstore.collection("References").whereEqualTo("First Name", firstName).get();
            try {
                List<QueryDocumentSnapshot> documents = future.get().getDocuments();
                if (!documents.isEmpty()) {
                    for (QueryDocumentSnapshot document : documents) {
                        document.getReference().delete();
                    }
                    listOfUsers.remove(selectedRecord);
                    System.out.println("Record deleted successfully.");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No record selected for deletion.");
        }
    }

    @FXML
    private void handleChangeTheme(ActionEvent event) {
        System.out.println("Change Theme action triggered");
        String currentTheme = App.scene.getStylesheets().isEmpty() ? "default" : App.scene.getStylesheets().get(0);
        if (currentTheme.contains("theme1.css")) {
            App.scene.getStylesheets().clear();
            App.scene.getStylesheets().add(getClass().getResource("/files/theme2.css").toExternalForm());
        } else {
            App.scene.getStylesheets().clear();
            App.scene.getStylesheets().add(getClass().getResource("/files/theme1.css").toExternalForm());
        }
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        System.out.println("About action triggered");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About This Application");
        alert.setContentText("This is a sample JavaFX application demonstrating Firebase integration.");
        alert.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void addData() {
        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("First Name", fnField.getText());
        data.put("Last Name", lnField.getText());
        data.put("Department", depField.getText());
        data.put("Major", majorField.getText());
        data.put("Email", emailField.getText());
        data.put("Image URL", imgField.getText());
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public boolean readFirebase() {
        boolean key = false;
        ApiFuture<QuerySnapshot> future = App.fstore.collection("References").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (documents.size() > 0) {
                System.out.println("Outing....");
                for (QueryDocumentSnapshot document : documents) {
                    Map<String, Object> data = document.getData();

                    String firstName = data.getOrDefault("First Name", "").toString();
                    String lastName = data.getOrDefault("Last Name", "").toString();
                    String department = data.getOrDefault("Department", "").toString();
                    String major = data.getOrDefault("Major", "").toString();
                    String email = data.getOrDefault("Email", "").toString();
                    String imageUrl = data.getOrDefault("Image URL", "").toString();

                    outputField.setText(outputField.getText() + "First Name: " + firstName + " , Last Name: " +
                            lastName + " , Department: " +
                            department + " , Major: " +
                            major + " , Email: " +
                            email + " , Image URL: " +
                            imageUrl + " \n ");

                    Map<String, String> person = new HashMap<>();
                    person.put("First Name", firstName);
                    person.put("Last Name", lastName);
                    person.put("Department", department);
                    person.put("Major", major);
                    person.put("Email", email);
                    person.put("Image URL", imageUrl);
                    listOfUsers.add(person);
                }
            } else {
                System.out.println("No data");
            }
            key = true;
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }
}

