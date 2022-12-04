package Bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Quote extends Expression {

    public String create() {
        System.setProperty("webdriver.chrome.driver","selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://randstuff.ru/saying/");
        text = null;
        try {

            WebElement button = webDriver.findElement(By.xpath("//*[@id=\"button\"]"));
            button.click();
            WebElement quote = webDriver.findElement(By.xpath("//*[@id=\"saying\"]/table/tbody/tr/td/text()"));
            WebElement author = webDriver.findElement(By.xpath("//*[@id=\"saying\"]/table/tbody/tr/td/span"));
            text = quote.getText() + "\n" + author.getText();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public String send() {
        return create();
    }
}
