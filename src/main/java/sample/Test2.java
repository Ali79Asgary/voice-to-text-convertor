package sample;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Test2 {
    private void initSwingComponents() {
        JFrame frame = new JFrame("Java FX in Swing");
        frame.setLayout(new BorderLayout());
        final JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel, BorderLayout.CENTER);
        final JPanel swingButtons = new JPanel();
//        final JButton okButton = new JButton("OK");
//        okButton.addActionListener(event -> System.out.println("Swing says 'OK'"));
//        final JButton exitButton = new JButton("Exit");
//        exitButton.addActionListener(event -> System.exit(0));
//        swingButtons.add(okButton);
//        swingButtons.add(exitButton);
//        frame.add(swingButtons, BorderLayout.SOUTH);
//
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
//        okButton.requestFocus();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(jfxPanel);
            }
        });
    }

    private void initFX(JFXPanel jfxPanel) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("strip.fxml"));
            Scene scene = new Scene(root, 250, 150);
//            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            jfxPanel.setScene(scene);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                test.initSwingComponents();
            }
        });
    }
}
