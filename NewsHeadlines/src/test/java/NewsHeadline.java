import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
public class NewsHeadline {
    private static WebDriver driver = null;

       static List<String> listOfWord(List<String> word) {
        List<String> wordsList = new ArrayList<>();
        for (String s : word) {
            String[] arrOfString = s.split(" ");
            List<String> l1 = Arrays.asList(arrOfString);
            wordsList.addAll(l1);
        }
        System.out.println(wordsList);
        return wordsList;
        }

         static String getWordCountList(List<String> arr) {
            Map<String, Integer> newsMap = new HashMap();

            for (int i = 0; i < arr.size(); i++) {
                if (newsMap.containsKey(arr.get(i))) {
                    newsMap.put(arr.get(i), newsMap.get(arr.get(i)) + 1);
                } else {
                    newsMap.put(arr.get(i), 1);
                }
            }
            String highestOccuringWord = getHighestOccuringWord(newsMap);
            return highestOccuringWord;
         }

         static String getHighestOccuringWord(Map<String, Integer> newsMap) {
           String key = "";
           Integer value = 0;

            for (Map.Entry<String, Integer> val : newsMap.entrySet()) {
            if (val.getValue() > value) {
                value = val.getValue();
                key = val.getKey();
            }
            System.out.println(val.getKey() + " "
                    + ": " + val.getValue() + " times");
            }
             return key;
         }
         static String getMostPopularNewsHeading(Map<String, Integer> newsMap, String maxWords) {
        Integer value = 0;
        String popularWord = "";

        for (Map.Entry<String, Integer> newsHeadingMap : newsMap.entrySet()) {
            if (newsHeadingMap.getKey().contains(maxWords)) {

                if (newsHeadingMap.getValue() > value) {
                    value = newsHeadingMap.getValue();
                    popularWord = newsHeadingMap.getKey();
                }
            }
        }
        return popularWord;
    }

    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();


        driver.get("https://news.ycombinator.com");
        driver.findElement(By.xpath("//a[@class='morelink' and @rel='next']")).click();
        driver.findElement(By.xpath("//a[@class='morelink' and @rel='next']")).click();
        driver.findElement(By.xpath("//a[@class='morelink' and @rel='next']")).click();

        List<WebElement> newsElements = driver.findElements(By.cssSelector("a.storylink"));
        List<WebElement> scorePoints = driver.findElements(By.cssSelector("span.score"));

        List<String> newsHeading = new ArrayList();
        List<String> newsPoints = new ArrayList();

        Map<String, Integer> newsMap = new HashMap();

        for (WebElement webElement : newsElements) {
            System.out.println(webElement.getText());
            newsHeading.add(webElement.getText());
        }

        for (WebElement webElement : scorePoints) {
            System.out.println(webElement.getText().substring(0, webElement.getText().length() - 7));
            newsPoints.add(webElement.getText().substring(0, webElement.getText().length() - 7));
        }
        for (int i = 0; i < newsHeading.size(); i++) {
            System.out.println(newsHeading.get(i));
            System.out.println(Integer.parseInt(newsPoints.get(i)));
            newsMap.put(newsHeading.get(i), Integer.parseInt(newsPoints.get(i)));
        }

        List<String> listOfWord = listOfWord(newsHeading);
        String maxWords = getWordCountList(listOfWord);
        System.out.println("The most common words : " + maxWords);
        String mostPopularNews = getMostPopularNewsHeading(newsMap, maxWords);
        System.out.println("the most popular news heading :" + mostPopularNews);
    }
}
