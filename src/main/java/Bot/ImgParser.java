package Bot;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImgParser {
    public static InputFile imageParser() {
        System.setProperty("webdriver.chrome.driver","selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.generatormix.com/random-memes");
        WebElement img = webDriver.findElement(By.className("aspect-square-contain"));
        String src = img.getAttribute("src");
        webDriver.quit();
        return new InputFile(src);
    }
}
