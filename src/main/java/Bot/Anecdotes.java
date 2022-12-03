package Bot;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Anecdotes {
    public static String sayJoke(){
        OkHttpClient okHttpClient;
        Request request;
        Response response=null;
        JSONObject jsonObject;
        JSONParser parser =  new JSONParser();
        String sendJoke = null;
        try
        {
            okHttpClient = new OkHttpClient();
            request = new Request.Builder()
                    .url("https://official-joke-api.appspot.com/jokes/random")
                    .get()
                    .build();
            response = okHttpClient.newCall(request).execute();
            String data = response.body().string();
            jsonObject = (JSONObject)parser.parse(data);

            JSONObject jokejsonobject = (JSONObject)parser.parse(data);
            sendJoke = jokejsonobject.get("setup") + "\n\n" + jokejsonobject.get("punchline");
        }
        catch (Exception e){ e.printStackTrace();}
        return sendJoke;
    }
}
