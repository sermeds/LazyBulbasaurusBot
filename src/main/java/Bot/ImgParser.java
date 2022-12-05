package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImgParser {
    public static InputFile imageParser(){
        Document document;
        File file;

        try {
            document = Jsoup.connect("https://www.reddit.com/r/memes/").get();
            Elements img = document.getElementsByClass("_2_tDEnGMLxpM6uOa2kaDB3");

            try (InputStream in = new URL(img.first().attr("src")).openStream()) {
                Files.copy(in, Paths.get("D:\\MemeParser"));
                return new InputFile("D:\\MemeParser");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Image not found...");
            e.printStackTrace();
        }

        return null;
    }
}
