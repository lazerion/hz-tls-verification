package com.hazelcast.tls.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static spark.Spark.*;

public class DummyClient {
    private static Logger logger = LoggerFactory.getLogger(DummyClient.class);
    static HazelcastInstance client;
    static List<Long> statistics = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        startServer();
        startClient();
    }

    private static void startServer() {
        get("/cluster", (req, res) -> {
            logger.info("Querying cluster size...");
            res.body(String.valueOf(client.getCluster().getMembers().size()));
            return String.valueOf(client.getCluster().getMembers().size());
        });

        get("/stats", (req, res) -> {
            logger.info("Querying statistics...");
            res.body(String.valueOf((long) statistics.size()));
            return String.valueOf(client.getCluster().getMembers().size());
        });
    }

    private static void startClient() throws InterruptedException {
        ClientConfig clientConfig = new XmlClientConfigBuilder().build();

        logger.info("Group {}", clientConfig.getGroupConfig().getName());
        logger.info("Password {}", clientConfig.getGroupConfig().getPassword());

        client = HazelcastClient.newHazelcastClient(clientConfig);

        IQueue<String> queue = client.getQueue(RandomStringUtils.randomAlphabetic(42));
        logger.info("Get Queue {}", queue);
        logger.info("Started");

        while (true) {

            IntStream.range(0, 1024).forEach(it -> {
                final String value = RandomStringUtils.randomAlphabetic(1024);
                try {
                    final long s = System.currentTimeMillis();
                    queue.put(value);
                    queue.take();
                    final long e = System.currentTimeMillis() - s;
                    statistics.add(e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            logger.info("Running queue");
            TimeUnit.SECONDS.sleep(1);
            queue.clear();
        }
    }

}
