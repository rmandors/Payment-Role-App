// 21 Abril 2025
// Proyecto Final: Rol de Pagos
// NRC: 2108	
// Raymond Portilla - 00335050
// Nicolas Tovar - 00332988

import java.util.List;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaymentRole extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PaymentRole.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Payment Role");
        primaryStage.setResizable(false);
        primaryStage.show(); 
    }

    public static void main(String[] args){
        Application.launch(args);
    }

}