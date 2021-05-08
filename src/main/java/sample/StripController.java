package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StripController implements Initializable {

    public Button btnRecordStrip;
    public Button btnCloseStrip;
    public Button btnMagnifyStrip;
    public Label lblTimerStrip;
    public AnchorPane anchorPaneMainStrip;
    File audioFile;
    boolean isRecordPressed = true;
    MainRecorder mainRecorder;
    Stopwatch stopwatch;
    int filesCount;
    String text = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRecordStrip.setShape(new Circle(100));
        btnCloseStrip.setShape(new Circle(100));
        btnMagnifyStrip.setShape(new Circle(100));

        anchorPaneMainStrip.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue){
                System.out.println("newValue true");
            } else {
                System.out.println("newValue false");
            }
        }));
//        anchorPaneMainStrip.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (newValue) {
//                    System.out.println("on focus tab pane");
//                } else {
//                    System.out.println("out focus tab pane");
//                }
//            }
//        });

        isRecordPressed = true;
        stopwatch = new Stopwatch(lblTimerStrip);
        stopwatch.starter();

        try {
            Font fontIRANSans = Font.loadFont(new FileInputStream(new File("D:\\IntelliJ\\Mr.Rasekh_JavaFX\\Library-Assistant\\src\\resources\\IRANSans_Bold.TTF")), 12);
            lblTimerStrip.setFont(fontIRANSans);
            btnRecordStrip.setFont(fontIRANSans);
            btnMagnifyStrip.setFont(fontIRANSans);
            btnCloseStrip.setFont(fontIRANSans);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void closeStrip(ActionEvent event) {
        try {
            close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void magnifyStrip(ActionEvent event) {
        try {
            loadMain();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void startRecordingStrip(ActionEvent event) {
        try {
            try {
                File audiosDir = new File("./AudioFiles");
                if (!audiosDir.exists()) {
                    audiosDir.mkdirs();
                    System.out.println("File created");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                filesCount = new File("./AudioFiles").listFiles().length;
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(filesCount);
            int fileNumber = filesCount - 1;
            if (isRecordPressed){
                try {
                    btnRecordStrip.setText("پایان ضبط");
                    System.out.println("شروع ضبط");
                    stopwatch.play();
                    mainRecorder = new MainRecorder(true);
                    Thread thread = new Thread(mainRecorder);
                    thread.start();
                    isRecordPressed = false;
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try {
                    btnRecordStrip.setText("شروع ضبط");
                    System.out.println("پایان ضبط");
                    stopwatch.reset(lblTimerStrip);
                    mainRecorder.finish();
                    mainRecorder.cancel();
                    audioFile = new File("./AudioFiles/RecordAudio"+fileNumber+".wav");
                    JsonPostVoiceFile jsonPostVoiceFile = new JsonPostVoiceFile(UtilAccessToken.accessToken, audioFile);
                    jsonPostVoiceFile.setOnSucceeded((succeededEvent) ->{
                        if (jsonPostVoiceFile.responseCode == 200){
                            if (jsonPostVoiceFile.resultStatus == 200){
                                text = "";
                                text = text.concat(" ").concat(jsonPostVoiceFile.text).concat(" ");
                                StringSelection stringSelection = new StringSelection(text);
                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clipboard.setContents(stringSelection, null);
                                try {
                                    Robot robot = new Robot();
                                    robot.keyPress(KeyEvent.VK_CONTROL);
                                    robot.keyPress(KeyEvent.VK_V);
                                    robot.keyRelease(KeyEvent.VK_CONTROL);
                                    robot.keyRelease(KeyEvent.VK_V);
                                } catch (AWTException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(jsonPostVoiceFile);
                    executorService.shutdown();
                    isRecordPressed = true;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
//            Stage stage = (Stage) btnCloseStrip.getScene().getWindow();
//            stage.close();
//            System.exit(0);
            Test.frame.dispose();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("دیپ ماین");
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
