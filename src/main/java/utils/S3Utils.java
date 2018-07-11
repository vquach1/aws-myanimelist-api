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

/*
 * A wrapper around the AmazonS3 client. Abstracts away the
 * details of working with the client.
 */
public class S3Utils {
    /*
     * The bucket that will be used for any S3 queries. For
     * this project, it is appropriate to specify a main bucket,
     * as all of the MyAnimeList pages will be stored in it
     */
    private String bucket;

    /*
     * The maximum number of days for an S3 object to be considered
     * up-to-date. Beyond the threshold, the S3 object is outdated
     */
    private int outdatedThreshold;

    private AmazonS3 s3;

    public S3Utils(String bucket, int outdatedThreshold) {
        this.bucket = bucket;
        this.outdatedThreshold = outdatedThreshold;
        s3 = AmazonS3ClientBuilder.defaultClient();
    }

    /*
     * Reads the contents of the object at the specfied key
     * in the bucket. If the object does not exist, an
     * empty string is returned.
     */
    public String readObject(String key) {
        String objectStr = null;

        //System.out.println("Attempting to get key " + key);

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
        } catch (AmazonS3Exception e) {
            System.out.println("Attempted to grab " + key + " which does not exist");
            e.printStackTrace();
        }

        return objectStr;
    }

    /*
     * Returns a list of all the keys in the bucket that begin
     * with the specified prefix.
     */
    public List<String> getKeys(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucket)
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

    /*
     * Renames an object from 'oldKey' to 'newKey' in the bucket
     */
    public void renameObject(String oldKey, String newKey) {
        CopyObjectRequest req = new CopyObjectRequest(bucket, oldKey, bucket, newKey);
        s3.copyObject(req);
        s3.deleteObject(new DeleteObjectRequest(bucket, oldKey));
    }

    /*
     * Returns true if the key is not present in the bucket,
     * or if the object's age has crossed the outdated threshold
     */
    public boolean objectMissingOrOutdated(String key) {
        try {
            ObjectMetadata metadata = s3.getObjectMetadata(bucket, key);
            Date fileDate = metadata.getLastModified();
            Date currDate = new java.util.Date();
            long diffMillis = currDate.getTime() - fileDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

            return diffDays > outdatedThreshold;

            //return diffDays > 7; // Anything over a week old is outdated
        } catch (AmazonS3Exception exception) {
            return true;
        }
    }

    /*
     * Inserts content into the bucket at the specified key
     */
    public void putObject(String key, String content) {
        s3.putObject(bucket, key, content);
    }
}
