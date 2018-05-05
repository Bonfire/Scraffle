# Scraffle
A Scrap.TF raffle-entering bot made in Java

## Description
Scraffle is a program written in Java made to automatically enter all open raffles on https://scrap.tf/

This project is currently in early development

## Requirements
  - Selenium Chromedriver (Provided at https://github.com/Bonfire/Scraffle/releases)
  - Java
  - A Steam account
  - A https://scrap.tf/ account
  - Windows, MacOS, or Linux
  
## Instructions (Jar/Artifact)
  1. Navigate to the releases page (https://github.com/Bonfire/Scraffle/releases)
  2. Download the latest ```Scraffle.jar``` file and the chromedriver built for your operating system
      - ```chromedriver.exe``` for Windows
      - ```chromedriver.Linux``` for Linux
      - ```chromedriver.Mac``` for MacOSX
  3. Place both ```Scraffle.jar``` and your chromedriver in the same folder
  4. Rename your chromedriver to ```chromedriver```, on Windows, make sure it's ```chromedriver.exe```
  5. Run the jar file through your terminal by running ```java -jar Scraffle.jar```
  6. Navigate to http://scrap.tf/ and log in using Steam
  7. Paste the required cookie values from your current http://scrap.tf/ session into the program
  8. All set! Let the program do its thing

## Planned Features
  - Raffle item value checking
  - Make sure raffles are actually entered before continuing (for slower connections)

## Issues
  - Please report any issues through the repository's issue tracker
  
## Notes
  - You may get banned for using programs like this
  - You may get hit with a Google reCaptcha. If you do, stop the program, fill it out on your normal browser, and restart the program.
