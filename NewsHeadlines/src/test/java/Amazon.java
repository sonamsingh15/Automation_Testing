import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Amazon {
ChromeDriver driver=null;

@BeforeClass
    public void before(){
    WebDriverManager.chromedriver().setup();
    driver=new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);
    driver.get("http://amazon.com");

}

    @Test
    public void testProductsearch(){
       WebElement search= driver.findElementById("twotabsearchtextbox");
        search.sendKeys("William Shakespeare");
        search.submit();

    }

    @AfterClass
    public void after(){
    //driver.quit();
}
}
