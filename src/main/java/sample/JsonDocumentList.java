package sample;

import javafx.concurrent.Task;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

//کلاسی که لیست تمام اسناد را از سرور دریافت میکند
public class JsonDocumentList extends Task {

    int responseCode = 0;
    @Override
    protected Object call() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sort_type", 0);
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(jsonObject);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
            Request request = new Request.Builder()
                    .url("http://deepmine.ir:8890/api/document_list/")
                    .method("GET", null)
                    .addHeader("Authorization", "Token "+UtilAccessToken.accessToken)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-CSRFToken", "dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .addHeader("Cookie", "csrftoken=dTESXmXRrcnvU5pM8QXOum3XXAgh2soUPZLpGyKk2we6p8M8C4gIhP0hThm3JraF")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response);
            responseCode = response.code();
            System.out.println("JsonDocumentList response code: "+responseCode);
            String result = response.body().string();
            System.out.println("JsonDocumentList result: "+result);
            JSONObject jsonObjectResult = new JSONObject(result);
            try {
                JSONArray dataArray = jsonObjectResult.getJSONArray("data");
                System.out.println(dataArray);
                System.out.println(dataArray.length());
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
