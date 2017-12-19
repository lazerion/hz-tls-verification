package com.hazelcast.tls.verifier;


import com.hazelcast.nio.ssl.TlsHostVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.InetAddress;
import java.security.cert.Certificate;
import java.util.Properties;

public class NoopVerifier implements TlsHostVerifier{
    private static Logger logger = LoggerFactory.getLogger(InvalidVerifier.class);

    @Override
    public void init(Properties properties) {
        logger.info("Initializing...");
    }

    @Override
    public void verifyHost(InetAddress hostAddress, Certificate[] hostCertificates) throws SSLPeerUnverifiedException {
        logger.info("verifying, no-op");
    }
}
