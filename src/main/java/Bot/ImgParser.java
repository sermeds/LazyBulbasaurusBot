package Bot;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImgParser {
    public static InputFile imageParser() {
        Document document;
        File file = null;

        try {
            document = Jsoup.connect("https://www.reddit.com/r/memes/").get();
            Elements img = document.getElementsByClass("_2_tDEnGMLxpM6uOa2kaDB3");
//            FileUtils.copy
            File file1 = new File("src/main/resources/test3.jpeg");
            try (InputStream in = new URL(img.first().attr("src")).openStream()) {
                FileUtils.copyURLToFile(new URL(img.first().attr("src")), file1);
                return new InputFile(file1);
            } catch (IOException e) {
                System.out.println("Сукаааа, опять тупой копи не сработал");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Image not found...");
            e.printStackTrace();
        }

        return null;
    }
}
