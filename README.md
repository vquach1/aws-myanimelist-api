# Welcome
This web API scrapes information from MyAnimeList. Though MyAnimeList has its own API, its range of capabilities is limited and requires authentication through injection of usernames and passwords, presenting security vulnerabilities. Though there are other APIs that scrape MyAnimeList, they have traditionally been limited by a number of factors:

- Entirely reliant on a cache. The data is not stored in a database, so if the cache goes down, it will have to be rebuilt
- Restricted by MyAnimeList's rate limiting. The API cannot handle requests at any arbitrary speed, as it always has to perform HTTP requests to MyAnimeList if the info is not present in its cache
- Limited to what a single page on MyAnimeList can present. There is no logic that lets users perform HTTP requests involving data aggregation (e.g. "search for all animes with Naruto as a character")

This API was created to address each one of those issues. It employs a scraper that incrementally downloads pages from MyAnimeList and pulls the information into a database. Because the data is permanently stored, this gives the API a lot more flexibility. Though it can only update its information as fast as MyAnimeList rate limiting allows, it can handle requests at an arbitrary rate. In the future, it will also have custom searches such as the example listed above.

# Architecture

## Amazon Web Services
Amazon Web Services (AWS) powers most of the components in the API. 
- API Gateway and Lambda handle the actual API requests
- SQS bridges the gap between API Gateway / Lambda and the backend scraper. When Lambda triggers in response to an API request, it will attempt to pull the information from RDS. If the necessary information is outdated or missing, Lambda will pipe the request to SQS, which is polled by the backend scraper. The scraper will then download the necessary page(s) and update RDS with the information.
- S3 stores the downloaded pages from MyAnimeList
- RDS hosts an instance of PostgreSQL
- EC2 will eventually host the backend scraper

## Redis
- Redis Cloud (see https://redislabs.com/blog/redis-cloud-30mb-ram-30-connections-for-free/) is currently used for caching, with the intent of eventually switching over to ElastiCache. ElastiCache is currently not being used, as there are some issues with accessing it from Lambda. 

## Backend Scraper
- Java
- Spring is used for the convenience that it brings, such as beans, task executors, and Hibernate support
- Hibernate is used to provide an OOP approach for interacting with AWS RDS
