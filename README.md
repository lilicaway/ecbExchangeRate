#Exchange Rate application

This application reads the ECB rates daily and offers an API to get a euro rate for a date and a currencyCode.

Uses these endpoints as datasources:

* http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml
* http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml

To run the application locally with maven you can run it:

```
mvn spring-boot:run
```

Once it is running you can test it with urls such as this one:

http://localhost:8081/api/exchangeRate?date=2016-02-10&currencyCode=USD

The currencyCode must be the international currencyCode in uppercase and the date format is YYYY-MM-DD.

Note that you need Java 8 and maven 3. This was tested with maven 3.3.3.
