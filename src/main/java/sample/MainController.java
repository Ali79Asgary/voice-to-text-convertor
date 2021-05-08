package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {

    public Group decibelSoundMeterGroup;
    public AnchorPane decibelSoundMeterParent;
    public AnchorPane decibelSoundMeterShow;
    public StackPane rootPane;
    public AnchorPane rootAnchorPane;
    public TabPane mainTabPane;
    public Tab bookIssueTab;
    public Button btnRecord;
    public Button btnFileChooser;
    public TextArea lblVoiceText;
    public Label lblTimer;
    Thread recordThread;
    //new recorder fields
    private static final long serialVersionUID = 1L;
    byte[] audioBytes = null;
    float[] audioData = null;
    final int BUFFER_SIZE = 16384;
//    final Recorder recorder = new Recorder(meter);

    //last recorder fields
    File audioFile;
    boolean isRecordPressed = true;
    MainRecorder mainRecorder;
    Stopwatch stopwatch;
    boolean isStripOpen = true;
    //    StripController stripController = new StripController();
    int filesCount = 0;
    String textFieldText = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            decibelSoundMeterShow.setPrefWidth(1);
            btnRecord.setShape(new Circle(100));
            isRecordPressed = true;
            stopwatch = new Stopwatch(lblTimer);
            stopwatch.starter();

            try {
                Font fontIRANSans = Font.loadFont("IRANSans_Bold.ttf", 20);
                lblVoiceText.setFont(fontIRANSans);
                lblTimer.setFont(fontIRANSans);
                btnRecord.setFont(fontIRANSans);
                btnFileChooser.setFont(fontIRANSans);
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

//        mainTabPane.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (newValue) {
//                    System.out.println("on focus tab pane");
//                    loadStrip(isStripOpen);
//                } else {
//                    System.out.println("out focus tab pane");
//                    loadStrip(isStripOpen);
//                }
//            }
//        });
    }

    @FXML
    void loadFileChooser(ActionEvent event) {
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
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void startRecording(ActionEvent event) {
        try {
            decibelSoundMeterShow.setPrefWidth(1);
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
                btnRecord.setText("پایان ظبط");
                System.out.println("شروع ظبط");

                mainRecorder = new MainRecorder(false, decibelSoundMeterShow);
                Thread thread = new Thread(mainRecorder);
                thread.start();

                stopwatch.play();

                isRecordPressed = false;
            } else {
                btnRecord.setText("شروع ظبط");
                System.out.println("پایان ظبط");
                mainRecorder.finish();
                mainRecorder.cancel();
                audioFile = new File("./AudioFiles/RecordAudio"+fileNumber+".wav");
                stopwatch.reset(lblTimer);
                decibelSoundMeterShow.setPrefWidth(1);

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
                isRecordPressed = true;
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public File selectFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("فایل صوتی خود را انتخاب نمایید!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("wav files", "*.wav"));
        return fileChooser.showOpenDialog(stage);
    }

    public void loadStrip(boolean isStripOpen) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/strip.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            if (!isStripOpen){
                stage.setTitle("Strip");
                stage.setScene(new Scene(parent));
                stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
                this.isStripOpen = true;
            } else {
                stage.close();
                this.isStripOpen = false;
            }

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void closeStrip() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/strip/strip.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Strip");
            stage.setScene(new Scene(parent));
            stage.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    //unusable methods.
    @FXML
    void handleAboutMenu(ActionEvent event) {

    }

    @FXML
    void handleIssuedList(ActionEvent event) {

    }

    @FXML
    void handleMenuAddBook(ActionEvent event) {

    }

    @FXML
    void handleMenuAddMember(ActionEvent event) {

    }

    @FXML
    void handleMenuClose(ActionEvent event) {

    }

    @FXML
    void handleMenuFullScreen(ActionEvent event) {

    }

    @FXML
    void handleMenuOverdueNotification(ActionEvent event) {

    }

    @FXML
    void handleMenuSettings(ActionEvent event) {

    }

    @FXML
    void handleMenuViewBook(ActionEvent event) {

    }

    @FXML
    void handleMenuViewMemberList(ActionEvent event) {

    }
}
