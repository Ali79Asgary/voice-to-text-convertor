package sample;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;

public class StripControllerSwing extends JComponent {

    public static void startFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame frame = new JFrame("Strip");
                    frame.setUndecorated(true);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setFocusable(false);
                    frame.setFocusableWindowState(false);
                    frame.setAlwaysOnTop(true);
                    frame.setBounds(600, 0, 700, 90);
                    Parent root = FXMLLoader.load(getClass().getResource("strip.fxml"));
                    JFXPanel jfxPanel = new JFXPanel();
                    frame.add(jfxPanel, BorderLayout.CENTER);
                    jfxPanel.setScene(new Scene(root));
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFrame frame = new JFrame("Strip");
//                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//                frame.setSize(700, 90);
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//                frame.setFocusable(false);
//                frame.setFocusableWindowState(false);
//                frame.setAlwaysOnTop(true);
//            }
//        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame frame = new JFrame("Strip");
                    frame.setLayout(new BorderLayout());
                    frame.setUndecorated(true);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setFocusable(false);
                    frame.setFocusableWindowState(false);
                    frame.setAlwaysOnTop(true);
                    frame.setBounds(600, 0, 700, 90);
                    Parent root = FXMLLoader.load(getClass().getResource("strip.fxml"));
                    final JFXPanel jfxPanel = new JFXPanel();
                    frame.add(jfxPanel, BorderLayout.CENTER);
                    jfxPanel.setScene(new Scene(root));
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JFrame frame = new JFrame("Strip");
//                    frame.setUndecorated(true);
//                    frame.pack();
//                    frame.setLocationRelativeTo(null);
//                    frame.setVisible(true);
//                    frame.setFocusable(false);
//                    frame.setFocusableWindowState(false);
//                    frame.setAlwaysOnTop(true);
//                    frame.setBounds(600, 0, 700, 90);
//                    try {
//                        Parent root = FXMLLoader.load(getClass().getResource("strip.fxml"));
//                    } catch (IOException e){
//                        e.printStackTrace();
//                        System.out.println(e.getMessage());
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                    System.out.println(e.getMessage());
//                }
//                JPanel content = new JPanel(new BorderLayout());
//                content.setBorder(new EmptyBorder(25, 50, 25, 50));

//                JFXPanel jfxPanel = new JFXPanel();
//                frame.add(jfxPanel, BorderLayout.CENTER);
//
//                Button btnRecordStrip = new Button("شروع ضبط");
//                Button btnCloseStrip = new Button("خروج");
//                Scene scene = new Scene(btnRecordStrip);
//
//                jfxPanel.setScene(scene);

//                Button btnRecordStrip = new Button("شروع ضبط");
//                content.add(btnRecordStrip, BorderLayout.CENTER);
//                Button btnCloseStrip = new Button("خروج");
//                content.add(btnCloseStrip, BorderLayout.CENTER);

//                frame.setContentPane(content);
//            }
//        });
    }
}
