package sample;

//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println(new File(".\\src\\main\\java\\sample\\login.fxml").getPath());

        String basePath = new File("").getAbsolutePath();
        System.out.println(basePath);

        URL path = new File("src/main/java/sample/login.fxml").toURI().toURL();
        System.out.println(path);

        String filePath = new File("").getAbsolutePath();
        System.out.println(filePath);
        filePath.concat("/src/main/java/sample/login.fxml");

        System.out.println(filePath);
//        Parent root = FXMLLoader.load(path);
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

//        ManagedChannel channel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443).usePlaintext().build();
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
