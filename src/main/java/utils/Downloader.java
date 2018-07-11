package utils;

import utils.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/*
 * A thread-safe object that performs HTTP Get requests on
 * specified URLs. When creating the object, the user passes
 * minimum number of milliseconds that must elapse between
 * each download
 */
public class Downloader {
    @Autowired
    private S3Utils s3Utils;

    /*
     * Stores the time at which the last HTTP Get request occurred
     */
    private Date lastDownloadTime;

    /*
     * The minimum number of milliseconds that must elapse between requests
     */
    private long timeWaitMillis;

    /*
     * The domain of the site that will be queried
     */
    private String domain;

    public Downloader(long timeWaitMillis, String domain) {
        this.timeWaitMillis = timeWaitMillis;
        this.domain = domain;

        // We do this so that we do not have to wait out
        // the timeWaitMillis for the first download
        lastDownloadTime = new Date(new Date().getTime() - timeWaitMillis);
    }

    // TODO: Implement exponential backoff for download()

    /*
     * Downloads a page from the specified path for the domain,
     * and inserts it into S3. If fewer than lastDownloadTime
     * milliseconds have elapsed, sleep until lastDownloadTime
     * seconds have passed - this is to avoid spamming the web
     * server with requests
     */
    public synchronized void download(String path) {
        System.out.println("    Received request to download " + path);

        Date currTime = new Date();
        long timeWait = timeWaitMillis - (currTime.getTime() - lastDownloadTime.getTime());

        if (timeWait > 0) {
            try {
                Thread.sleep(timeWait);
            } catch (InterruptedException e) {}
        }

        String urlStr = domain + path;
        StringBuilder content = new StringBuilder();
        BufferedReader reader;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Ocarina MAL Scrape 1.0");
            connection.setRequestProperty("From", "vitungquach1494@gmail.com");
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            if (connection.getResponseCode() == 404) {
                System.out.println("404 for " + path);
            }

            lastDownloadTime = new Date();

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }

            reader.close();
        } catch (IOException e) {}

        s3Utils.putObject(path, content.toString());
        System.out.println("    Finished downloading " + path);
    }
}
