# Coin Desk Application

An application which integrates with coin desk APIs to get coin informations like current rate, bpi etc.

## Getting Started

### Dependencies

* Requires JDK 11 or above for compiling and running the application

### Executing program

* Running the application
```
mvn clean compile exec:java
```
Upon executing the above, the program will ask you for a currency code, start date and duration in days.

Sample output
```
    mvn clean compile exec:java

    Oct 25, 2022 10:42:51 AM com.kuehne.nagel.coin.desk.CoinDeskApplication getCurrency
    INFO: Enter a currency code (E.g. USD): 
    eur
    Oct 25, 2022 10:43:13 AM com.kuehne.nagel.coin.desk.CoinDeskApplication getStartDate
    INFO: Enter start date (yyyy-MM-dd): 
    2020-09-09
    Oct 25, 2022 10:43:19 AM com.kuehne.nagel.coin.desk.CoinDeskApplication getDuration
    INFO: Enter duration in days ( E.g. 90): 
    10   
    Oct 25, 2022 10:43:25 AM com.kuehne.nagel.coin.desk.service.BitCoinServiceImpl printMessage
    INFO:  -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# COIN DESK RESULT #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-
    
    ::Inputted Values::
    Currency: EUR
    Start Date: 2020-09-09 
    End Date: 2020-09-19
    
    The CURRENT Bitcoin rate: 18821.9405 
    The LOWEST Bitcoin rate from 2020-09-09 to 2020-09-19 is : 8498.1997
    The HIGHEST Bitcoin rate from 2020-09-09 to 2020-09-19 is : 9359.141
    
    -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-
```
* Running the Test
```
mvn test
```

### Key Highlights
* The Code could be easily extensible for other coins. E.g. You can write a class implementing CoinService and providing custom implementation of the required methods.
* No Hardcoding of the time duration. E.g. User needs to pass the duration, the program would calculate the endDate based on the startDate and the duration.
* No major Sonar issues.
* More than 70% methods are covered by test cases.
* The project takes into consideration the separation of concerns.

## Author

[Monisha Gupta](guptamonisha07@gmail.com)
