package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import okhttp3.*;
import org.json.JSONObject;

//کلاسی که آیدی یک سند را دریافت میکند و در پاسخ آن سند را برای ما برمیگرداند.
public class JsonGetDocument extends Task {

    int responseCode = 0;
    int documentID = 0;
    Label lblVoiceToText;

    public JsonGetDocument(int documentID) {
        this.documentID = documentID;
    }

    public JsonGetDocument(int documentID, Label lblVoiceToText) {
        this.documentID = documentID;
        this.lblVoiceToText = lblVoiceToText;
    }

    @Override
    protected Object call() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("document_id", documentID);
            } catch (Exception e){
                e.printStackTrace();
            }
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
            Request request = new Request.Builder()
                    .url("http://deepmine.ir:8890/api/document/")
                    .method("GET", null)
//                    .addHeader("document_id", String.valueOf(documentID))
                    .addHeader("Authorization", "Token "+UtilAccessToken.accessToken)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-CSRFToken", "dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .addHeader("Cookie", "csrftoken=dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response);
            responseCode = response.code();
            System.out.println("Get Document ResponseCode : "+responseCode);
            String result  = response.body().string();
            System.out.println(result);
            JSONObject jsonObjectResult = new JSONObject(result);
            try {
                String detail = jsonObjectResult.getString("detail");
                System.out.println(detail);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lblVoiceToText.setText(detail);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
