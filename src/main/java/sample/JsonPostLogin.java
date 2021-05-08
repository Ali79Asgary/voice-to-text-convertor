package sample;

import javafx.concurrent.Task;
import netscape.javascript.JSException;
import okhttp3.*;
import okhttp3.MediaType;
import org.json.JSONObject;

import java.net.CookieStore;

//کلاس مربوط به لاگین کردن کاربر
//که نام کاربری و رمز عبور را دریافت میکند و برای سرور میفرستد
//و پاسخ را دریافت و اعتبارسنجی میکند
public class JsonPostLogin extends Task<Integer> {

//    public static final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    final String SITE_ADDR = "deepmine.ir:8890/";
    String username = "";
    String password = "";
    int responseCode = 0;
    String accessTokenLogin = "";
    String refreshToken = "";
    int userId = 0;

    public JsonPostLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            System.out.println("Hello Ali");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            } catch (JSException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            System.out.println(jsonObject);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            CookieStore cookieStore;
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
            Request request = new Request.Builder()
                    .url("http://deepmine.ir:8890/api/token/")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            CookieJar cookieJar = client.cookieJar();
            System.out.println(cookieJar);
            System.out.println(response);
            responseCode = response.code();
            System.out.println(responseCode);
            String result = response.body().string();
            JSONObject resultJsonObj = new JSONObject(result);
            UtilAccessToken.accessToken = resultJsonObj.getString("access_token");
            System.out.println(result);
            System.out.println(UtilAccessToken.accessToken);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return responseCode;
    }
}
