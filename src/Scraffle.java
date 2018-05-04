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
        String raffleURL;

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
        System.out.print("Please enter your cfduid cookie: ");
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

        // Enter all current raffles
        enterRaffles(webDriver);

        // See if the user has won anything, and let them know if they have
        try{
            WebElement unclaimedRaffles = ((ChromeDriver) webDriver).findElementByXPath("/html/body/aside/div/a/i18n[1]");
            System.out.println("You currently have unclaimed raffle winnings!");
        }catch (Exception e){
            // No winnings :(
        }

        // Monitor further raffles every 5 minutes
        monitorRaffles(webDriver);
    }

    public static void enterRaffles(WebDriver webDriver){
        webDriver.get("https://scrap.tf/raffles");

        if(!webDriver.getTitle().equalsIgnoreCase("Raffles - Scrap.TF")){
            System.exit(0);
        }

        // Keep collecting more raffle URLs until there are no more to collect
        boolean moreRaffles = true;

        while(moreRaffles){
            ((ChromeDriver) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

            if(webDriver.getPageSource().contains("That's all, no more!")){
                moreRaffles = false;
            }
        }

        // Make a list of every raffle we haven't already entered
        // I use an ArrayList to avoid stale elements in Selenium
        List<WebElement> Raffles = ((ChromeDriver) webDriver).findElementsByXPath("//*[contains(@class, \"panel-raffle\") and not(contains(@class,'raffle-entered'))]/div[1]/div/a");
        int rafflesEntered = 0;

        ArrayList<Raffle> raffleList = new ArrayList<>();

        for(WebElement raffle : Raffles){
            String raffleURL = raffle.getAttribute("href");
            raffleList.add(new Raffle(raffleURL));
        }

        //  Proceed to go to every raffle and enter them if we can
        for(Raffle raffle : raffleList){
            String raffleURL = raffle.getURL();
            webDriver.get(raffleURL);

            try{
                if(webDriver.getPageSource().contains("Withdraw Items")){
                    System.out.println("Already won raffle. Skipping");
                    continue;
                }

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

        System.out.println(rafflesEntered + " raffle(s) entered");
        webDriver.get("https://scrap.tf/raffles");
    }

    public static void monitorRaffles(WebDriver webDriver){
        // Once we're done entering all current raffles, we need to monitor for further raffles
        // See if we haven't entered everything, and if we haven't then enter anything we haven't already entered.
        // After each check, we sleep for 5 minutes.
        while(true){
            System.out.println("Checking for open raffles");
            webDriver.get("https://scrap.tf/raffles");
            WebElement enteredRaffles = ((ChromeDriver) webDriver).findElementByXPath("/html/body/div[3]/div[4]/div[2]/div/i18n/var");
            String[] raffleCountString = enteredRaffles.getText().split("/");

            if(Integer.parseInt(raffleCountString[0]) < Integer.parseInt(raffleCountString[1])){
                System.out.println("New raffles added! You've currently entered only "
                        + Integer.parseInt(raffleCountString[0]) + " out of " + Integer.parseInt(raffleCountString[1])
                        + " raffles!");
                enterRaffles(webDriver);
            }else{
                System.out.println("No open raffles");
            }

            try{
                System.out.println("Sleeping for 5 minutes");
                Thread.sleep(300000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}