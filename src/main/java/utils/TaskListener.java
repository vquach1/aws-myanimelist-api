package utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import com.fasterxml.jackson.core.JsonParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import scrapers.animes.AnimeCharacterStaffScraper;
import scrapers.animes.AnimeScraper;

import javax.annotation.PostConstruct;
import java.util.List;

public class TaskListener {
    private final String QUEUE_URL;
    private final AmazonSQS sqs;
    private final JSONParser parser;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private AnimeScraper animeScraper;

    @Autowired
    private AnimeCharacterStaffScraper animeCharacterStaffScraper;

    public TaskListener() {
        // Temporary
        String queueUrl = System.getenv("MALSCRAPE_SQS_URL");
        if (queueUrl == null) {
            QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/096162967289/malscrape_api_request_queue";
        } else {
            QUEUE_URL = queueUrl;
        }

        sqs = AmazonSQSClientBuilder.defaultClient();
        parser = new JSONParser();
    }

    @PostConstruct
    private void listen() {
        System.out.println("Starting to listen for messages...");
        System.out.println(QUEUE_URL);

        while (true) {
            ReceiveMessageRequest receiveReq = new ReceiveMessageRequest()
                    .withQueueUrl(QUEUE_URL)
                    .withWaitTimeSeconds(20);
            ReceiveMessageResult receiveRes = sqs.receiveMessage(receiveReq);

            List<Message> messages = receiveRes.getMessages();
            for (Message message : messages) {
                try {
                    JSONObject messageJson = (JSONObject)parser.parse(message.getBody());
                    JSONObject req = (JSONObject)parser.parse((String)messageJson.get("Message"));
                    String category = (String)req.get("category");
                    String pageType = (String)req.get("pageType");
                    int id = Math.toIntExact((long)req.get("id"));

                    sendRequest(category, pageType, id);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sqs.deleteMessage(QUEUE_URL, message.getReceiptHandle());
            }
        }
    }

    private Runnable getRequestAnime(String pageType, int id) {
        switch (pageType) {
            case "details":
                return () -> animeScraper.convert(id);
            case "charStaff":
                return () ->  animeCharacterStaffScraper.convert(id);
            default:
                throw new IllegalArgumentException(
                        "PageType: " + pageType + " does not conform to a handled Anime request type"
                );
        }
    }

    private void sendRequest(String category, String pageType, int id) {
        System.out.println(category + "    " + pageType + "   " + id);
        Runnable task;

        switch (category) {
            case "anime":
                task = getRequestAnime(pageType, id);
                break;
            default:
                throw new IllegalArgumentException(
                        "Category: " + category + " and PageType: " + pageType +
                                " do not conform to a handled request type");
        }

        Runnable wrappedTask = () -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        taskExecutor.execute(task);
    }
}
