package sample;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;
import java.awt.*;

public class SwingFx {
    public static void main(String[] args) {
        Platform.runLater(new Runnable() {
            public void run() {
                initAndShowGUI();
            }
        });
    }

    public static void swingFx(){
        Platform.runLater(new Runnable() {
            public void run() {
                initAndShowGUI();
            }
        });
    }

    public static void initAndShowGUI() {
        JFrame frame = new JFrame("NonFocusableJFXPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setFocusable(false);
        frame.setAlwaysOnTop(true);
        frame.setFocusableWindowState(false);

        JPanel workArea = (JPanel) frame.getContentPane();

        final JFXPanel fxPanel = new JFXPanel();

        // With Java 10, works fine with .setFocusable(true).
        fxPanel.setFocusable(false);

        workArea.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(new Runnable() {
            public void run() {
                initFX(fxPanel);
            }
        });

        frame.setSize(600, 200);
        frame.setVisible(true);
    }

    public static int clickCount = 0;

    public static void initFX(JFXPanel fxPanel) {
        Button button = new Button("Click me ONCE and look at your console.");
        button.setOnMousePressed(e -> System.err.println(++clickCount));

        Scene scene = new Scene(button);
        fxPanel.setScene(scene);
    }
}
