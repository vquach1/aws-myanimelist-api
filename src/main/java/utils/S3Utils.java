package utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class S3Utils {
    public static final String BUCKET = "mal-scrape";

    private AmazonS3 s3;

    public S3Utils() {
        s3 = AmazonS3ClientBuilder.defaultClient();
    }

    public String readObject(String key) {
        String objectStr = null;

        System.out.println("Attempting to get key " + key);

        try {
            StringBuilder objectSB = new StringBuilder();
            String line;

            S3Object s3Obj = s3.getObject(S3Utils.BUCKET, key);
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

    public List<String> getKeys(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(S3Utils.BUCKET)
                .withPrefix(prefix)
                .withMaxKeys(1000);
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

    public void renameObject(String oldKey, String newKey) {
        CopyObjectRequest req = new CopyObjectRequest(S3Utils.BUCKET, oldKey, S3Utils.BUCKET, newKey);
        s3.copyObject(req);
        s3.deleteObject(new DeleteObjectRequest(S3Utils.BUCKET, oldKey));
    }

    public boolean objectMissingOrOutdated(String key) {
        try {
            ObjectMetadata metadata = s3.getObjectMetadata(S3Utils.BUCKET, key);
            Date fileDate = metadata.getLastModified();
            Date currDate = new java.util.Date();
            long diffMillis = currDate.getTime() - fileDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

            return diffDays > 7; // Anything over a week old is outdated
        } catch (AmazonS3Exception exception) {
            return true;
        }
    }

    public void putObject(String key, String content) {
        s3.putObject(S3Utils.BUCKET, key, content);
    }
}
