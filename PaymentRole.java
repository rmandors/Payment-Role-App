// 21 Abril 2025
// Proyecto Final: Rol de Pagos
// NRC: 2108	
// Raymond Portilla - 00335050
// Nicolas Tovar - 00332988

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;

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

    /*/
    // Reads the student data from a CSV file and prints it.
    private static void readCSV(String fileName){
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;

            reader.readLine(); // Skip header

            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                Employee student = new Employee(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                System.out.println(student);
            }
        } 
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // Writes the student list to a CSV file using Formatter
    private static void writeCSV(String fileName){
        try(Formatter output = new Formatter(new File(fileName))){
            output.format("id,name,age,credits%n");

            for(Employee s : employees){
            output.format("%s,%s,%d,%d%n", s.getId(), s.getName(), s.getAge(), s.getCredits());
        }

            System.out.println("CSV file written successfully!");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    */
}


