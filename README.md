# Coin Desk Application

An application which integrates with coin desk APIs to get coin informations like current rate, bpi etc.

## Getting Started

### Dependencies

* Requires JDK 11 for compiling and running the application

### Executing program

* Running the application
```
mvn clean compile exec:java
```
Upon executing the above, the program will ask you for a currency code, start date and duration in days.

Sample output
```
    mvn clean compile exec:java

    INFO: Enter a currency code (E.g. USD): 
    eur
    Oct 24, 2022 2:29:11 AM com.kuehne.nagel.coin.desk.CoinDeskApplication getStartDate
    INFO: Enter start date (yyyy-MM-dd): 
    2020-09-09
    Oct 24, 2022 2:29:16 AM com.kuehne.nagel.coin.desk.CoinDeskApplication getDuration
    INFO: Enter duration in days ( E.g. 90): 
    10
    Oct 24, 2022 2:29:23 AM com.kuehne.nagel.coin.desk.service.BitCoinServiceImpl printMessage
    INFO:  -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# COIN DESK RESULT #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-
    
    ::Inputted Values::
    Currency: EUR
    Start Date: 2020-09-09 
    End Date: 2020-09-19
    
    The CURRENT Bitcoin rate: 18993.7106 
    The LOWEST Bitcoin rate in last 10 day(s): 8498.1997
    The HIGHEST Bitcoin rate in last 10 day(s): 9359.141
    
    -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-
```
* Running the Test
```
mvn test
```
## Author

[Monisha Gupta](guptamonisha07@gmail.com)
