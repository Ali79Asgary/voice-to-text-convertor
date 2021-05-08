package sample;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {

    public static JFrame frame;
    void initSwingComponents() {
        frame = new JFrame("Java FX in Swing");
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(false);
        frame.setFocusableWindowState(false);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 260, 100, 100, 100));
        frame.setAlwaysOnTop(true);
        frame.setBounds(0, 0, 260, 100);
        final JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel, BorderLayout.CENTER);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(jfxPanel);
            }
        });
    }

    void initFX(JFXPanel jfxPanel) {
        try {
            URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/designer-strip.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            jfxPanel.setScene(scene);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    public static void startTest(){
        Test test = new Test();
//        SwingUtilities.invokeLater(() -> test.initSwingComponents() );
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                test.initSwingComponents();
            }
        });
    }

    public static void main(String[] args) {
//        Test test = new Test();
//        SwingUtilities.invokeLater(() -> test.initSwingComponents() );
        startTest();
    }
}
