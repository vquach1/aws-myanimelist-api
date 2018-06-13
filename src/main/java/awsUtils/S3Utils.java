package awsUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class S3Utils {
    public static final String MAL_SCRAPE_PERSONS = "mal-scrape-persons";
    public static final String MAL_SCRAPE_CHARACTERS = "mal-scrape-characters";
    public static final String MAL_SCRAPE_ANIME = "mal-scrape-anime";
    public static final String MAL_SCRAPE_ANIME_STATISTICS = "mal-scrape-anime-statistics";
    public static final String MAL_SCRAPE_PRODUCERS = "mal-scrape-producers";

    private AmazonS3 s3;
    private int requestCount = 0; // TODO: Request count is really janky. This needs to be refactored

    public S3Utils() {
        s3 = AmazonS3ClientBuilder.defaultClient();
    }

    public String readObject(String bucket, String key) {
        if (requestCount == 50) {
            s3 = AmazonS3ClientBuilder.defaultClient();
            requestCount = 0;
        }

        requestCount++;

        String objectStr = null;

        try {
            StringBuilder objectSB = new StringBuilder();
            String line;

            S3Object s3Obj = s3.getObject(bucket, key);
            S3ObjectInputStream s3Stream = s3Obj.getObjectContent();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Stream, "UTF-8"))) {
                while ((line = reader.readLine()) != null) {
                    objectSB.append(line);
                }
            }

            objectStr = objectSB.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectStr;
    }

    public List<String> getKeys(String bucket) {
        if (requestCount == 50) {
            s3 = AmazonS3ClientBuilder.defaultClient();
            requestCount = 0;
        }

        requestCount++;

        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucket).withMaxKeys(1000);
        ListObjectsV2Result result;
        List<String> keys = new ArrayList<String>();

        do {
            result = s3.listObjectsV2(req);

            List<S3ObjectSummary> summaries = result.getObjectSummaries();
            List<String> newKeys = summaries.stream()
                    .map(summ -> summ.getKey())
                    .collect(Collectors.toList());
            keys.addAll(newKeys);

            req.setContinuationToken(result.getNextContinuationToken());
        } while (result != null && result.isTruncated());

        return keys;
    }
}
