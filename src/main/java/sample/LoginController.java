package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//کنترلر صفحه لاگین
public class LoginController implements Initializable {

//    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    boolean isStripOpen = false;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblErrorSignUp;

    @FXML
    private Button btnSignUp;

    @FXML
    private Button btnForgetPassword;

    //تابعی که موقع اجرای صفحه ، قبل از بقیه توابع اجرا میشود
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Font fontIRANSans = Font.loadFont("IRANSans_Bold.ttf", 20);
            username.setFont(fontIRANSans);
            password.setFont(fontIRANSans);
            btnLogin.setFont(fontIRANSans);
            btnForgetPassword.setFont(fontIRANSans);
            btnSignUp.setFont(fontIRANSans);
            lblErrorSignUp.setFont(fontIRANSans);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابع مربوط به دکمه لاگین که بعد از زدن دکمه لاگین اجرا میشود.
    // نام کاربری و رمز عبور را برای سرور میفرستد و اگر صحیح بود به صفحه بعدی میرود.
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        System.out.println("hello");
        try {
            String uname = username.getText();
            String pword = password.getText();

            if (username.getText().equals("") || password.getText().equals("")){
                lblErrorSignUp.setText("لطفا تمام فیلدها را تکمیل نمایید!");
            } else {
                try {
                    //کلاسی که نام کاربری و رمز عبوری که کاربر وارد کرده است
                    //را برای سرور میفرستد و پاسخ را دریافت میکند و اعتبارسنجی میکند
                    JsonPostLogin jsonPostLogin = new JsonPostLogin(uname, pword);
                    jsonPostLogin.setOnSucceeded((succeededEvent) ->{
                        if (jsonPostLogin.responseCode == 200){
                            System.out.println("On Succeeded");
                            closeStage();
                            loadStripSwing(isStripOpen);
                        } else {
                            System.out.println("Login failed!");
                            lblErrorSignUp.setText("اطلاعات وارد شده صحیح نمیباشد!");
                        }
                    });
                    jsonPostLogin.setOnFailed((failedEvent) -> {
                        System.out.println("On Failed");
                    });
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(jsonPostLogin);
                    executorService.shutdown();
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    //تابعی برای بستن برنامه
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    //تابع مربوط به دکمه ثبت نام
    //سایت مربوطه برای ثبت نام را در مرورگر پیشفرض کاربر باز میکند
    @FXML
    public void goToSignUp(ActionEvent mouseEvent) {
        openLink(URI.create("https://google.com/"));
    }

    //تابعی برای باز کردن یک لینک در مرورگر پیشفرض کاربر
    public static boolean openLink(URI uri){
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
            try {
                desktop.browse(uri);
                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return false;
    }

    //تابعی برای باز کردن یک لینک در مرورگر پیشفرض کاربر
    public static boolean openLink(URL url){
        try {
            return openLink(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    //تابعی برای بستن استیج صفحه لاگین
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    //تابعی برای اجرای صفحه اصلی برنامه
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

    //تابعی برای اجرای صفحه نوار برنامه
    public void loadStrip(boolean isStripOpen) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/strip.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            if (!isStripOpen){
                Scene scene = new Scene(parent);
                scene.setFill(Color.TRANSPARENT);
                stage.setTitle("Strip");
                stage.setScene(scene);
                stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
                stage.setX(610);
                stage.setY(0.0);
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

    //تابعی برای اجرای صفحه نوار برنامه
    public void loadStripSwing(boolean isStripOpen){
        try {
            if (!isStripOpen){
                Test.startTest();
                this.isStripOpen = true;
            } else {
                this.isStripOpen = false;
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    //تابعی که بعد از زدن دکمه فراموشی رمز عبور اجرا میشود
    @FXML
    public void handleForgetPassword(ActionEvent actionEvent) {
    }
}
