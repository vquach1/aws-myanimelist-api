# Welcome
This web API scrapes information from MyAnimeList (https://myanimelist.net/). Though MyAnimeList has its own API, its range of capabilities is limited and requires authentication through injection of usernames and passwords, presenting security vulnerabilities. Though there are other APIs that scrape MyAnimeList, they have traditionally been limited by a number of factors:

- Entirely reliant on a cache. The data is not stored in a database, so if the cache goes down, it will have to be rebuilt
- Restricted by MyAnimeList's rate limiting. The API cannot handle requests at any arbitrary speed, as it always has to perform HTTP requests to MyAnimeList if the info is not present in its cache
- Limited to what a single page on MyAnimeList can present. There is no logic that lets users perform HTTP requests involving data aggregation (e.g. "search for all animes with Naruto as a character")

This API was created to address each one of those issues. It employs a scraper that incrementally downloads pages from MyAnimeList and pulls the information into a database. Because the data is permanently stored, this gives the API a lot more flexibility. Though it can only update its information as fast as MyAnimeList rate limiting allows, it can handle requests at an arbitrary rate. In the future, it will also have custom searches such as the example listed above.

# Using the API
This repository only stores the contents of the backend scraper (see Architecture section). Due to the scraper's reliance on AWS components, at this time it is not currently possible to run the scraper yourself. However, you can test out the API commands through the URL provided below:

https://edru1ll5kg.execute-api.us-east-1.amazonaws.com/v1/

For example, you would pull the information for My Hero Academy through

https://edru1ll5kg.execute-api.us-east-1.amazonaws.com/v1/anime/31964, which uses the anime ID from the link
https://myanimelist.net/anime/31964/Boku_no_Hero_Academia

# Resources

Most of the data from the main anime, manga, character, and person pages from MyAnimeList have already been bulk processed into the database. The API currently exposes only a resource for anime. Howver, manga, character, and person pages will be exposed in August 2018. For the paths listed below, append them onto the end of the URL to test out some queries.

As this API is a work-in-progress, you may occasionally run into empty responses or errors. In such cases...
- Make sure that the ID of your request refers to an actual ID from MyAnimeList
- Retry the request. There could be a timeout issue involved with API Gateway or Lambda
- If still not working, then the page may not have been scraped into the database yet. However, sending the request will notify the backend scraper to download the page
- If none of the above, then it may just be a bug!

## Anime
| Path | Usage | Example |
| ---- | ----- | ------- |
| anime/{id} | Displays general info from the anime with the MyAnimeList ID | anime/20 refers to https://myanimelist.net/anime/20 |
| anime/{id}/charstaff | Displays the characters and staff information for the anime | anime/527/charstaff refers to https://myanimelist.net/anime/527/Pokemon/characters | 

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
