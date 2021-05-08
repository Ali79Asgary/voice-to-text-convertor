package sample;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import okhttp3.*;
import org.json.JSONObject;
import java.io.File;

//کلاس مربوط به فرستادن فایل ضبط شده به سرور از طریق RestFull Api
//و دریافت متن از طریق سرور
public class JsonPostVoiceFile extends Task {

    String accessToken = "";
    File wavFile;
    public int responseCode = 0;
    public int resultStatus = 0;
    public String text = "";
    TextArea textArea;
    Label label;

    public JsonPostVoiceFile(String accessToken, File wavFile) {
        this.accessToken = accessToken;
        this.wavFile = wavFile;
    }

    public JsonPostVoiceFile(String accessToken, File wavFile, TextArea textArea) {
        this.accessToken = accessToken;
        this.wavFile = wavFile;
        this.textArea = textArea;
    }

    public JsonPostVoiceFile(String accessToken, File wavFile, Label label) {
        this.accessToken = accessToken;
        this.wavFile = wavFile;
        this.label = label;
    }

    @Override
    protected Object call() throws Exception {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().
                    setType(MultipartBody.FORM).
                    addFormDataPart("file", wavFile.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), wavFile))
                    .build();
            Request request = new Request.Builder().
                    url("http://deepmine.ir:8890/api/speech2text/").method("POST", body).
                    addHeader("Authorization", "Token "+accessToken).
                    addHeader("X-CSRFToken", "dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF").
                    addHeader("Cookie", "csrftoken=dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF").build();
            Response response = client.newCall(request).execute();
            responseCode = response.code();
            String result = response.body().string();
            System.out.println("speech2text: "+result);
            JSONObject jsonObject = new JSONObject(result);
            resultStatus = jsonObject.getInt("status");
            if (resultStatus == 200){
                text = jsonObject.getString("data");
                System.out.println("text of speech: "+text);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
