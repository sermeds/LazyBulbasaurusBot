package Bot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(WeatherModel model) throws IOException {

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Saratov&units=metric&appid=6fff53a641b9b9a799cfd6b079f5cd4e");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        return  Icon.OFFICE.get() + " Город: " + model.getName() + "\n" +
                Icon.SUN.get() + " Температура: " + model.getTemp() + "C" + "\n" +
                Icon.DROP.get() + " Влажность:" + model.getHumidity() + "%" + "\n";
    }
}
