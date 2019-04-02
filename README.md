Application works with Java 1.11 (was required for BigDecimal.sqrt()).

Application requires at least 3 params: 
1. Currency code (one of "USD", "EUR", "CHF", "GBP")
2. Begin date (from this date application starts calculation of currency)
3. End date (to this date application starts calculation of currency)

Application is using concurrency. 

For bigger amount of data NBP site has limitations which significantly influence application performance 
(and it sometimes can be source of an exception as resource is blocked). To deal with this problem I 
added simple, retry mechanism which is configurable via two extra, optional application parameters:
4. Retry attempt interval (default: 500) - times thread sleeps after an attempt of getting NBP xml file.
With short distance of time can be significantly lowered.
5. Max retry attempt (default: 10) - max number of application attempts to get an xml resource.