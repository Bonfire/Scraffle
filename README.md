# Scraffle
A Scrap.TF raffle-entering bot made in Java

## Description
Scraffle is a program written in Java made to automatically enter all open raffles on https://scrap.tf/

## Features
- Enters all open raffles
- Checks for captchas
- Monitors for new raffles every 5 minutes

## Requirements
  - Selenium Chromedriver (Provided at https://github.com/Bonfire/Scraffle/releases)
  - Java
  - A Steam account (https://scrap.tf/ uses your Steam OpenID login)
  - Windows, MacOS, or Linux
  
## Instructions (Jar/Artifact)
  1. Navigate to the releases page (https://github.com/Bonfire/Scraffle/releases)
  2. Download the latest ```Scraffle.jar``` file, ```ublock.crx``` file, and the chromedriver built for your operating system
      - ```chromedriver.exe``` for Windows
      - ```chromedriverlinux``` for Linux
      - ```chromedrivermac``` for MacOSX
  3. Place ```Scraffle.jar```, ```ublock.crx```, and your chromedriver in the same folder
  4. Rename your chromedriver to ```chromedriver```. On Windows, make sure it's ```chromedriver.exe```
  5. Navigate to http://scrap.tf/ and log in using Steam
  6. Paste your ```__cfduid```, ```scr_session```, and ```PHPSESSID``` cookie values into a file such as ```cookies.txt```
      - Be sure to put each cookie value on it's own line
      - See ```cookies.txt``` in the repository for an example
  7. Run the jar file by double-clicking on it to execute it. You can also do this through cli using `java -jar Scraffle.jar`
  8. All set! Let the program do its thing

## Planned Features
  - Raffle item value checking
  - Make sure raffles are actually entered before continuing (for slower connections)

## Issues
  - Please report any issues through the repository's issue tracker
  
## Notes
  - You may get banned for using programs like this
  - You may get hit with a Google reCaptcha. If you do, stop the program, fill it out on your normal browser, and restart the program.
