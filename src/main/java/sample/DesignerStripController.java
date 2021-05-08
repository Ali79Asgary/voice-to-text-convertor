package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//کنترلر صفحه نوار
public class DesignerStripController implements Initializable {

    File audioFile;
    //متغیری برای اینکه بفهمیم ضبط صدا آغاز شده است یا پایان یافته است
    //وقتی در ابتدا دکمه ضبط صدا زده میشود و ضبط شروع میشود این متغیر true میشود
    //و وقتی ضبط صدا پایان میابد این متغیر false میشود
    boolean isRecordPressed = true;
    //کلاسی که در آن صدا ضبط میشود و از طریق RestFull Api برای سرور فرستاده میشود
    MainRecorder mainRecorder;
    //کلاسی که از طریق آن یک شمارنده زمان برای ضبط صدا ساخته میشود
    //و این شمارنده در مدت زمانی که صدا ضبط میشود برای کاربر بر روی
    //دکمه ضبط نشان داده میشود و مقدار زمان سپری شده را نمایش میدهد
    Stopwatch stopwatch;
    //متغیری برای تعداد فایل های موجود در پوشه صداهای ضبط شده
    int filesCount;
    String text = "";
    //کلاسی که با استفاده از grpc صدا را مستقیما از طریق میکروفون برای
    //سرور میفرستد و در لحظه نتیجه را برای کاربر نمایش میدهد (استریم میکند)
    StreamDirectly streamDirectly;

    @FXML
    private Button btnRecord;

    @FXML
    private Button btnMenuOption;

    @FXML
    private Button btnOption;

    @FXML
    private Button btnPerson;

    @FXML
    private ImageView questionMarkImageView;

    @FXML
    private ImageView minusImageView;

    @FXML
    private AnchorPane decibelSoundMeterParent;

    @FXML
    private Group decibelSoundMeterGroup;

    @FXML
    private AnchorPane decibelSoundMeterShow;

    @FXML
    private ImageView imageViewLaunch;

    //تابعی که موقع اجرای صفحه ، قبل از بقیه توابع اجرا میشود
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isRecordPressed = true;
        stopwatch = new Stopwatch(btnRecord);
        stopwatch.starterButton();
        try {
            URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
            Image microphoneIcon = new Image(String.valueOf(url));
            ImageView microphoneImageView = new ImageView(microphoneIcon);
            microphoneImageView.setFitHeight(28);
            microphoneImageView.setFitWidth(20.6);
            btnRecord.setText("");
            btnRecord.setGraphic(new ImageView(microphoneIcon));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //تابع مربوط به دکمه ضبط صدا در صفحه نوار
    @FXML
    void startRecording(ActionEvent event) {
        try {
            //در صفحه تنظیمات اگر کاربر انتخاب کند که صدا استریم شود ، ضبط صدا به صورت استریم انجام میشود
            //و اگر انتخاب کند که استریم نباشد ، ضبط صدا به صورت معمولی و با RestFull Api انجام خواهد شد.
            if (UtilStreamOrRest.isStream){
                streamWithGrpc();
            } else {
                sendingWithRest();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به ضبط صدا با RestFull Api
    public void sendingWithRest() {
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
                    btnRecord.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 100;");
                    btnRecord.setGraphic(null);
                    System.out.println("شروع ضبط");
                    stopwatch.playButton();
                    mainRecorder = new MainRecorder(true, decibelSoundMeterShow);
                    Thread thread = new Thread(mainRecorder);
                    thread.start();
                    isRecordPressed = false;
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try {
                    btnRecord.setStyle("-fx-background-color: #ff8a80; -fx-background-radius: 100;");
                    stopwatch.resetButton(btnRecord);
                    try {
                        URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
                        Image microphoneIcon = new Image(String.valueOf(url));
                        ImageView microphoneImageView = new ImageView(microphoneIcon);
                        microphoneImageView.setFitHeight(28);
                        microphoneImageView.setFitWidth(20.6);
                        btnRecord.setText("");
                        btnRecord.setGraphic(new ImageView(microphoneIcon));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("پایان ضبط");
                    mainRecorder.finish();
                    mainRecorder.cancel();
                    audioFile = new File("./AudioFiles/RecordAudio"+fileNumber+".wav");
                    JsonPostVoiceFile jsonPostVoiceFile = new JsonPostVoiceFile(UtilAccessToken.accessToken, audioFile);
                    jsonPostVoiceFile.setOnSucceeded((succeededEvent) ->{
                        if (jsonPostVoiceFile.responseCode == 200){
                            if (jsonPostVoiceFile.resultStatus == 200){
                                //بخشی که بعد از دریافت متن مربوط به هر صدا از طریق سرور ، آن متن را
                                //در clipboard کپی میکند و سپس در هر جا که کاربر فوکوس کرده باشد آن را چاپ میکند
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //تابع مربوط به ضبط صدا به صورت استریم با grpc
    public void streamWithGrpc() {
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
                    btnRecord.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 100;");
                    btnRecord.setGraphic(null);
                    System.out.println("شروع ضبط");
                    stopwatch.playButton();
                    streamDirectly = new StreamDirectly(true, decibelSoundMeterShow);
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(streamDirectly);
                    executorService.shutdown();
                    isRecordPressed = false;
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try {
                    btnRecord.setStyle("-fx-background-color: #ff8a80; -fx-background-radius: 100;");
                    stopwatch.resetButton(btnRecord);
                    try {
                        URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
                        Image microphoneIcon = new Image(String.valueOf(url));
                        ImageView microphoneImageView = new ImageView(microphoneIcon);
                        microphoneImageView.setFitHeight(28);
                        microphoneImageView.setFitWidth(20.6);
                        btnRecord.setText("");
                        btnRecord.setGraphic(new ImageView(microphoneIcon));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("پایان ضبط");
                    streamDirectly.finish();
                    streamDirectly.cancel();
                    isRecordPressed = true;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به باز کردن صفحه تنظیمات از طریق صفحه نوار
    @FXML
    void openSettingsStrip(ActionEvent event) {
        try {
            URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/settings.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle("تنظیمات");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به دکمه باز کردن صفحه اصلی برنامه از طریق صفحه نوار
    @FXML
    void openMainPageLaunch(MouseEvent event) {
        try {
            loadMainPage();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به باز کردن صفحه اصلی برنامه از طریق صفحه نوار
    void loadMainPage() {
        try {
            URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/main-page.fxml").toURI().toURL();
            Parent parent = FXMLLoader.load(url);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Deepmine");
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
