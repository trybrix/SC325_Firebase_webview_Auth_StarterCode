package com.example.csc325_firebase_webview_auth.view;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreen implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Pause for 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event ->{
            try{
                App.setRoot("/files/AccessFBView.fxml");
            }catch(IOException e){
                e.printStackTrace();
            }
        });

        pause.play();
    }
}
