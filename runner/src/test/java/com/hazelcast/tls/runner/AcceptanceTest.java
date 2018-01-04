package com.hazelcast.tls.runner;


import com.hazelcast.tls.utils.ClientContainer;
import com.hazelcast.tls.utils.ComposeCli;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcceptanceTest {
    private final Logger logger = LoggerFactory.getLogger(AcceptanceTest.class);

    private ComposeCli cli;

    @Before
    public void before() {
        cli = new ComposeCli();
    }

    @After
    public void after() {
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

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void shouldWorkWithSSLFalseSettings() throws IOException, InterruptedException {
        cli.up("deployment-2.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void sslDisabledMemberShouldBeIsolated() throws IOException, InterruptedException {
        cli.up("deployment-3.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void sslDisabledClientShouldBeFail() throws IOException, InterruptedException {
        cli.up("deployment-4.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        TimeUnit.SECONDS.sleep(20);
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), is(-1)));
    }


    @Test
    public void shouldRunWithCustomValidPolicyImplementations() throws IOException {
        cli.up("deployment-5.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void invalidSSLMemberShouldBeIsolated() throws IOException {
        cli.up("deployment-6.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void onlyMembersVerify() throws IOException {
        cli.up("deployment-8.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

    @Test
    public void onlyMembersVerifyWithTwoWaySSL() throws IOException {
        cli.up("deployment-9.yaml").scale("hazelcast", 3);
        ClientContainer client = new ClientContainer();

        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.memberSize(), is(3)));
        await().atMost(20, SECONDS).untilAsserted(() -> assertThat(client.statistics(), Matchers.greaterThan(0)));
    }

}
