import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scraffle {
    // Inner Raffle class to combat Selenium's stale element exceptions
    public static class Raffle{
        String raffleURL = "";

        public Raffle(String URL){
            this.raffleURL = URL;
        }

        public String getURL(){
            return raffleURL;
        }
    }


    public static void main(String[] args) {

        // Initialize the webdriver and navigate to Scrap.TF
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://scrap.tf/raffles");

        Scanner stdin = new Scanner(System.in);

        // Create new cookies and set them on our webdriver
        System.out.print("Please enter your scraptf_session cookie: ");
        Cookie cfdUID = new Cookie("scraptf_session", stdin.nextLine());
        System.out.print("Please enter your scr_session cookie: ");
        Cookie scrapTFSession = new Cookie("scr_session", stdin.nextLine());
        System.out.print("Please enter your PHPSESSID cookie: ");
        Cookie scrapPHPSession = new Cookie("PHPSESSID", stdin.nextLine());

        webDriver.manage().addCookie(cfdUID);
        webDriver.manage().addCookie(scrapTFSession);
        webDriver.manage().addCookie(scrapPHPSession);

        // Force a steam login
        WebElement steamLogin = ((ChromeDriver) webDriver).findElementByXPath("//*[@id=\"navbar-main\"]/ul[2]/li/a");
        steamLogin.click();

        webDriver.get("https://scrap.tf/raffles");

        if(!webDriver.getTitle().equalsIgnoreCase("Raffles - Scrap.TF")){
            System.exit(0);
        }

        // Keep collecting more raffle URLs until there are no more to collect
        boolean moreRaffles = true;

        while(moreRaffles){
            ((ChromeDriver) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try{
                ((ChromeDriver) webDriver).findElementByXPath("//*[contains(text(), \"That\'s all, no more!\")]");
            }catch(Exception e){
                moreRaffles = false;
            }
        }

        // Make a list of every raffle we haven't already entered
        // I use an ArrayList to avoid stale elements in Selenium
        List<WebElement> Raffles = ((ChromeDriver) webDriver).findElementsByXPath("//*[contains(@class, \"panel-raffle\") and not(contains(@class,'raffle-entered'))]/div[1]/div/a");
        int rafflesEntered = 0;

        ArrayList<Raffle> raffleList = new ArrayList<Raffle>();

        for(WebElement raffle : Raffles){
            String raffleURL = raffle.getAttribute("href");
            raffleList.add(new Raffle(raffleURL));
        }

        //  Proceed to go to every raffle and enter them if we can
        for(Raffle raffle : raffleList){
            String raffleURL = raffle.getURL();
            webDriver.get(raffleURL);

            try{
                WebElement enterRaffle = ((ChromeDriver) webDriver).findElementByXPath("//*[contains(text(), \"Enter Raffle\")]");
                enterRaffle.click();

                WebElement raffleChance = ((ChromeDriver) webDriver).findElementByXPath("//*[contains(@id, \"raffle-win-chance\")]");
                WebElement numberOfEntries = ((ChromeDriver) webDriver).findElementByXPath("//*[@id=\"raffle-num-entries\"]");

                System.out.println("Entered Raffle! " + numberOfEntries.getAttribute("data-total") + "/" + numberOfEntries.getAttribute("data-max") + " Entries! " + raffleChance.getText() + " Chance!");
                rafflesEntered++;

            }catch(Exception e){
                System.out.println("Something Went Wrong!");
            }

            // Sleep for 3 seconds, or else we might get banned!
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(rafflesEntered + " raffles entered");

    }
}