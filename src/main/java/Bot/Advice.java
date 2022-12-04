package Bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Advice extends Expression {

    public String create() {
        System.setProperty("webdriver.chrome.driver","selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://generatom.com/custom/generator_sovetov");
        text = null;
        try {
            WebElement button = webDriver.findElement(By.xpath("//*[@id=\"generate\"]"));
            button.click();
            Thread.sleep(1000);
            WebElement quote = webDriver.findElement(By.xpath("/html/body/main/div/section/div[1]/div[2]/div/div[1]/div/p"));
            text = quote.getText();
            webDriver.quit();
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