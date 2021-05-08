package sample;

//import com.itextpdf.text.*;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.pdf.PdfWriter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainPageController implements Initializable {

    File audioFile;
    File textFile;
    //متغیری برای اینکه بفهمیم ضبط صدا آغاز شده است یا پایان یافته است
    //وقتی در ابتدا دکمه ضبط صدا زده میشود و ضبط شروع میشود این متغیر true میشود
    //و وقتی ضبط صدا پایان میابد این متغیر false میشود
    boolean isRecordPressed = true;
    //متغیری برای اینکه بفهمیم استریم صدا آغاز شده است یا پایان یافته است
    //وقتی در ابتدا دکمه ضبط صدا زده میشود و ضبط شروع میشود این متغیر true میشود
    //و وقتی ضبط صدا پایان میابد این متغیر false میشود
    boolean isStreamPressed = true;
    boolean isUnderlined = false;
    boolean isBold = false;
    boolean isItalic = false;
    boolean isBreakWhile = false;
    //کلاسی که در آن صدا ضبط میشود و از طریق RestFull Api برای سرور فرستاده میشود
    MainRecorder mainRecorder;
    StreamRecorder streamRecorder;
    StreamRecorder1 streamRecorder1;
    //کلاسی که با استفاده از grpc صدا را مستقیما از طریق میکروفون برای
    //سرور میفرستد و در لحظه نتیجه را برای کاربر نمایش میدهد (استریم میکند)
    StreamDirectly streamDirectly;
    //کلاسی که از طریق آن یک شمارنده زمان برای ضبط صدا ساخته میشود
    //و این شمارنده در مدت زمانی که صدا ضبط میشود برای کاربر بر روی
    //دکمه ضبط نشان داده میشود و مقدار زمان سپری شده را نمایش میدهد
    Stopwatch stopwatch;
    Stopwatch streamStopwatch;
    boolean isStripOpen = true;
    String textFieldText = "";
    //متغیری برای تعداد فایل های موجود در پوشه صداهای ضبط شده
    int filesCount = 0;
    String text = "";
    int pdfFilesCount = 0;
    File pdfFile;

    @FXML
    private AnchorPane mainPageAnchorPane;

    @FXML
    private AnchorPane decibelSoundMeterParent;

    @FXML
    private Group decibelSoundMeterGroup;

    @FXML
    private AnchorPane decibelSoundMeterShow;

    @FXML
    private Button btnRecordMainPage;

    @FXML
    private Button btnChooseFileMainPage;

    @FXML
    private Label lblVoiceText;

    @FXML
    private Label lblDocumentName;

    @FXML
    private Label lblDocumentName1;

    @FXML
    private Button btnExportWord;

    @FXML
    private Button btnExportPDF;

    @FXML
    private Button btnBold;

    @FXML
    private Button btnItalic;

    @FXML
    private Button btnUnderline;

    @FXML
    private Button btnNumberedList;

    @FXML
    private Button btnStrikethrough;

    @FXML
    private Button btnIncreaseTextSize;

    @FXML
    private Button btnTextSize;

    @FXML
    private Button btnDecreaseTextSize;

    @FXML
    private Button btnUndo;

    @FXML
    private Button btnRedo;

    @FXML
    private Button btnChooseTextFile;

    @FXML
    private Button btnStreamVoice;

    @FXML
    private Button btnAddDocument;

    @FXML
    private Button btnDontSaveDocument;

    @FXML
    private Button btnSaveDocument;

    @FXML
    private GridPane gridPaneDocument;

    @FXML
    private Button btnSettingsMainPage;

    //تابعی که موقع اجرای صفحه ، قبل از بقیه توابع اجرا میشود
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            text = "";
            isRecordPressed = true;
            stopwatch = new Stopwatch(btnRecordMainPage);
            stopwatch.starterButton();
            streamStopwatch = new Stopwatch(btnRecordMainPage);
            streamStopwatch.starterButton();
            lblVoiceText.setText(text);

            //بخش مربوط به دریافت لیست اسناد از سرور
            JsonDocumentList jsonDocumentList = new JsonDocumentList();
            jsonDocumentList.setOnSucceeded((succeededEvent) -> {
                if (jsonDocumentList.responseCode == 200){

                }
            });
            ExecutorService executorService1 = Executors.newFixedThreadPool(1);
            executorService1.execute(jsonDocumentList);
            executorService1.shutdown();

            try {
                String txtButton = "دسته اول";
                URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-close-document.png").toURI().toURL();
                Image microphoneIcon = new Image(String.valueOf(url));
                ImageView microphoneImageView = new ImageView(microphoneIcon);
                microphoneImageView.setFitHeight(14);
                microphoneImageView.setFitWidth(14);
                Button btn = new Button();
                btn.setPrefHeight(24);
                btn.setPrefWidth(80);
                btn.setStyle("-fx-background-color: #56564f; -fx-background-radius: 19; -fx-text-fill: #fff59d; -fx-font-size: 12px;");
                btn.setGraphic(microphoneImageView);
                btn.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                btn.setText(txtButton);
                gridPaneDocument.add(btn, 0, 0);
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
                Image microphoneIcon = new Image(String.valueOf(url));
                ImageView microphoneImageView = new ImageView(microphoneIcon);
                microphoneImageView.setFitHeight(28);
                microphoneImageView.setFitWidth(20.6);
                btnRecordMainPage.setText("");
                btnRecordMainPage.setGraphic(new ImageView(microphoneIcon));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به دکمه ضبط صدا در صفحه اصلی
    @FXML
    void startRecordingMainPage(ActionEvent event) {
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

//    @FXML
//    void startStreamRecordingMainPage(ActionEvent event) {
//        try {
//            try {
//                File audiosDir = new File("./StreamAudioFiles");
//                if (!audiosDir.exists()) {
//                    audiosDir.mkdirs();
//                    System.out.println("Stream File created");
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//            try {
//                filesCount = new File("./StreamAudioFiles").listFiles().length;
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println(filesCount);
//            int fileNumber = filesCount - 1;
//            if (isStreamPressed){
//                try {
//                    btnStreamVoice.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 100;");
//                    btnStreamVoice.setGraphic(null);
////                    btnRecord.setText("پایان ضبط");
//                    System.out.println("شروع استریم ضبط");
//                    streamStopwatch.playButton();
//                    isBreakWhile = true;
////                    while (isBreakWhile){
//////                        if (!isBreakWhile){
//////                            System.out.println("While break");
//////                            break;
//////                        }
////                        System.out.println("while hello!");
//                    UtilStillRecord.stillRecord = true;
////                    streamRecorder = new StreamRecorder(true , lblVoiceText);
//
////                    StreamingRecognize streamingRecognize = new StreamingRecognize(lblVoiceText);
////                    Thread thread = new Thread(streamingRecognize);
////                    thread.start();
//
////                    Thread thread = new Thread(new Runnable() {
////                        @Override
////                        public void run() {
//////                            GrpcClient client = new GrpcClient(lblVoiceText);
//////                            client.runBidiTest();
////
////                            NewGrpcSpeechClient speechClient = new NewGrpcSpeechClient(lblVoiceText);
////                            speechClient.runBidiTest();
////                        }
////                    });
////                    thread.start();
//
////                    streamRecorder1 = new StreamRecorder1(lblVoiceText);
////                    ExecutorService executorService = Executors.newFixedThreadPool(1);
////                    executorService.execute(streamRecorder1);
////                    executorService.shutdown();
//
//                    streamDirectly = new StreamDirectly(lblVoiceText, false, decibelSoundMeterShow);
//                    ExecutorService executorService = Executors.newFixedThreadPool(1);
//                    executorService.execute(streamDirectly);
//                    executorService.shutdown();
//
////                    new Thread(new Runnable() {
////                        @Override
////                        public void run() {
////                            streamDirectly = new StreamDirectly(lblVoiceText);
////                            streamDirectly.runBidiTest();
////                        }
////                    }).start();
//
////                    JsonDocumentList jsonDocumentList = new JsonDocumentList();
////                    ExecutorService executorService1 = Executors.newFixedThreadPool(1);
////                    executorService1.execute(jsonDocumentList);
////                    executorService1.shutdown();
//
////                        TimeUnit.MILLISECONDS.sleep(400);
////                        streamRecorder.finish();
////                        streamRecorder.cancel();
//                    isStreamPressed = false;
////                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    UtilStillRecord.stillRecord = false;
//                    isBreakWhile = false;
//                    btnStreamVoice.setStyle("-fx-background-color: #ff8a80; -fx-background-radius: 100;");
//                    streamStopwatch.resetButton(btnStreamVoice);
//                    try {
//                        URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
//                        Image microphoneIcon = new Image(String.valueOf(url));
//                        ImageView microphoneImageView = new ImageView(microphoneIcon);
//                        microphoneImageView.setFitHeight(28);
//                        microphoneImageView.setFitWidth(20.6);
//                        btnStreamVoice.setText("");
//                        btnStreamVoice.setGraphic(new ImageView(microphoneIcon));
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
////                    btnRecord.setText("شروع ضبط");
//                    System.out.println("پایان استریم ضبط");
//                    streamDirectly.finish();
//                    streamDirectly.cancel();
////                    streamRecorder1.finish();
////                    streamRecorder1.cancel();
//                    audioFile = new File("./StreamAudioFiles/RecordAudio"+fileNumber+".wav");
//                    isStreamPressed = true;
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    //تابع مربوط به ضبط صدا به صورت استریم با grpc
    public void streamWithGrpc(){
        try {
            try {
                File audiosDir = new File("./StreamAudioFiles");
                if (!audiosDir.exists()) {
                    audiosDir.mkdirs();
                    System.out.println("Stream File created");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                filesCount = new File("./StreamAudioFiles").listFiles().length;
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(filesCount);
            int fileNumber = filesCount - 1;
            if (isStreamPressed){
                try {
                    btnRecordMainPage.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 100;");
                    btnRecordMainPage.setGraphic(null);
                    System.out.println("شروع استریم ضبط");
                    streamStopwatch.playButton();
                    isBreakWhile = true;
                    UtilStillRecord.stillRecord = true;
                    streamDirectly = new StreamDirectly(lblVoiceText, false, decibelSoundMeterShow);
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(streamDirectly);
                    executorService.shutdown();

                    isStreamPressed = false;

                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try {
                    UtilStillRecord.stillRecord = false;
                    isBreakWhile = false;
                    btnRecordMainPage.setStyle("-fx-background-color: #ff8a80; -fx-background-radius: 100;");
                    streamStopwatch.resetButton(btnRecordMainPage);
                    try {
                        URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
                        Image microphoneIcon = new Image(String.valueOf(url));
                        ImageView microphoneImageView = new ImageView(microphoneIcon);
                        microphoneImageView.setFitHeight(28);
                        microphoneImageView.setFitWidth(20.6);
                        btnRecordMainPage.setText("");
                        btnRecordMainPage.setGraphic(new ImageView(microphoneIcon));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("پایان استریم ضبط");
                    streamDirectly.finish();
                    streamDirectly.cancel();
                    audioFile = new File("./StreamAudioFiles/RecordAudio"+fileNumber+".wav");
                    isStreamPressed = true;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به ضبط صدا با RestFull Api
    public void sendingWithRest(){
        try {
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
                        btnRecordMainPage.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 100;");
                        btnRecordMainPage.setGraphic(null);
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
                        btnRecordMainPage.setStyle("-fx-background-color: #ff8a80; -fx-background-radius: 100;");
                        stopwatch.resetButton(btnRecordMainPage);
                        try {
                            URL url = new File("C:/Users/WIN10/Desktop/Games/grpc-java-gradle/src/main/java/sample/icons-microphone.png").toURI().toURL();
                            Image microphoneIcon = new Image(String.valueOf(url));
                            ImageView microphoneImageView = new ImageView(microphoneIcon);
                            microphoneImageView.setFitHeight(28);
                            microphoneImageView.setFitWidth(20.6);
                            btnRecordMainPage.setText("");
                            btnRecordMainPage.setGraphic(new ImageView(microphoneIcon));
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
                                    text = text.concat(" ").concat(jsonPostVoiceFile.text);
                                    lblVoiceText.setText(text);
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی که بعد از زدن دکمه انتخاب فایل صوتی در صفحه اصلی
    //اجرا میشود و صفحه انتخاب فایل صوتی باز میشود و کاربر
    //فایل صوتی را که میخواهد متنش نمایش داده شود را انتخاب میکند
    //و این فایل به سمت سرور فرستاده میشود و متنش نمایش داده میشود
    @FXML
    void chooseFileMainPage(ActionEvent event) {
        try {
            audioFile = selectFile(getStage());
            JsonPostVoiceFile jsonPostVoiceFile = new JsonPostVoiceFile(UtilAccessToken.accessToken, audioFile, lblVoiceText);
            jsonPostVoiceFile.setOnSucceeded((succeededEvent) ->{
                if (jsonPostVoiceFile.responseCode == 200){
                    if (jsonPostVoiceFile.resultStatus == 200){
                        textFieldText = textFieldText.concat(" ").concat(jsonPostVoiceFile.text);
                        lblVoiceText.setText(textFieldText);
                    }
                }
            });
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(jsonPostVoiceFile);
            executorService.shutdown();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی برای انتخاب فایل صوتی از دستگاه کاربر
    //که پنجره ای را برای انتخاب فایل نمایش میدهد
    public File selectFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("فایل صوتی خود را انتخاب نمایید!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("wav files", "*.wav"));
        return fileChooser.showOpenDialog(stage);
    }

    //تابعی برای انتخاب فایل متنی از دستگاه کاربر
    //که پنجره ای را برای انتخاب فایل نمایش میدهد
    public File selectTextFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("فایل متنی خود را انتخاب نمایید!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text files", "*.txt"));
        return fileChooser.showOpenDialog(stage);
    }

    //تابعی برای دریافت استیج مربوط به صفحه اصلی
    private Stage getStage() {
        return (Stage) mainPageAnchorPane.getScene().getWindow();
    }

    //تابعی که بعد از زدن دکمه انتخاب فایل متنی در صفحه اصلی
    //اجرا میشود و صفحه انتخاب فایل متنی باز میشود و کاربر
    //فایل متنی را که میخواهد متنش نمایش داده شود را انتخاب میکند
    //و این فایل به سمت سرور فرستاده میشود و متنش نمایش داده میشود
    @FXML
    void chooseTextFile(ActionEvent event) {
        textFile = selectTextFile(getStage());
    }

    //تابعی که بعد از زدن دکمه تنظیمات در صفحه اصلی
    //اجرا میشود و صفحه تنظیمات باز میشود
    @FXML
    void goToSettings(ActionEvent event) {
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

    @FXML
    void decreaseTextSize(ActionEvent event) {
        int fontSize = 10;
        lblVoiceText.setStyle("-fx-font-size: fon");
    }

    //تابعی که بعد از زدن دکمه pdf در صفحه اصلی اجرا میشود
    //و فایل pdf سند مورد نظر را به ما میدهد
    @FXML
    void exportToPDF(ActionEvent event) {

    }

    //تابعی که بعد از زدن دکمه word در صفحه اصلی اجرا میشود
    //و فایل word سند مورد نظر را به ما میدهد
    @FXML
    void exportToWord(ActionEvent event) {
        try {
            JsonGetDocument jsonGetDocument = new JsonGetDocument(341, lblVoiceText);
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(jsonGetDocument);
            executorService.shutdown();

            JsonGetWord jsonGetWord = new JsonGetWord(341);
            ExecutorService executorService1 = Executors.newFixedThreadPool(1);
            executorService1.execute(jsonGetWord);
            executorService1.shutdown();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void increaseTextSize(ActionEvent event) {

    }

    @FXML
    void addDocument(ActionEvent event) {

    }

    @FXML
    void dontSaveDocument(ActionEvent event) {

    }

    @FXML
    void saveDocument(ActionEvent event) {
        try {
            String documentText = lblVoiceText.getText();
            JsonPostDocument jsonPostDocument = new JsonPostDocument("third doc", documentText);
            ExecutorService executorService1 = Executors.newFixedThreadPool(1);
            executorService1.execute(jsonPostDocument);
            executorService1.shutdown();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void makeBold(ActionEvent event) {
        System.out.println(lblVoiceText.getStyle());
        if(!lblVoiceText.getStyle().contains("-fx-font-weight: bolder")){
            lblVoiceText.setStyle("-fx-font-weight: bolder");
        } else {
            lblVoiceText.setStyle("-fx-font-weight: normal");
        }
    }

    @FXML
    void makeItalic(ActionEvent event) {
        if(!lblVoiceText.getStyle().contains("-fx-font-style: italic")){
            lblVoiceText.setStyle("-fx-font-style: italic");
        } else {
            lblVoiceText.setStyle("-fx-font-style: normal");
        }
    }

    @FXML
    void makeLineThrough(ActionEvent event) {

    }

    @FXML
    void makeNumberedList(ActionEvent event) {

    }

    @FXML
    void makeUnderline(ActionEvent event) {
        if (!lblVoiceText.isUnderline()){
            lblVoiceText.setUnderline(true);
        } else {
            lblVoiceText.setUnderline(false);
        }
    }

    @FXML
    void redo(ActionEvent event) {

    }

    @FXML
    void undo(ActionEvent event) {

    }

}
