package sample;

import javafx.concurrent.Task;
import netscape.javascript.JSException;
import okhttp3.*;
import org.json.JSONObject;

import java.net.CookieStore;

//کلاسی که برای ما یک سند ایجاد میکند
public class JsonPostDocument extends Task {

    String documentTitle = "";
    String documentText = "";
    int responseCode = 0;

    public JsonPostDocument(String documentTitle, String documentText) {
        this.documentTitle = documentTitle;
        this.documentText = documentText;
    }

    @Override
    protected Object call() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", documentTitle);
                jsonObject.put("content", documentText);
            } catch (JSException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            CookieStore cookieStore;
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
            Request request = new Request.Builder()
                    .url("http://deepmine.ir:8890/api/document/")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Token "+UtilAccessToken.accessToken)
                    .addHeader("X-CSRFToken", "dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .addHeader("Cookie", "csrftoken=dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);
            responseCode = response.code();
            System.out.println(responseCode);
            String result = response.body().string();
            System.out.println(result);
            JSONObject resultJsonObj = new JSONObject(result);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
