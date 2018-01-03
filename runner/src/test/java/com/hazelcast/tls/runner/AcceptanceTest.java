package com.hazelcast.tls.runner;


import com.hazelcast.tls.utils.ClientContainer;
import com.hazelcast.tls.utils.ComposeCli;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class AcceptanceTest {
    private final Logger logger = LoggerFactory.getLogger(AcceptanceTest.class);

    private ComposeCli cli;

    @Before
    public void before() {
        cli = new ComposeCli();
    }

    @After
    public void after(){
        try {
            cli.down();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void shouldWorkWithDefaultSSLSettings() throws IOException, InterruptedException {
        cli.up("deployment-1.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        TimeUnit.SECONDS.sleep(10);
        assertTrue(client.memberSize() == 3);
        TimeUnit.SECONDS.sleep(10);
        assertTrue(client.statistics() > 0);
    }

    @Test
    public void shouldWorkWithSSLFalseSettings() throws IOException, InterruptedException {
        cli.up("deployment-2.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        TimeUnit.SECONDS.sleep(10);
        assertTrue(client.memberSize() == 3);
        TimeUnit.SECONDS.sleep(10);
        assertTrue(client.statistics() > 0);
    }
}
