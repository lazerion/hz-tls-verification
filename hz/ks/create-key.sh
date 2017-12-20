#!/bin/bash

#KEYSTORE_PASSWORD=s3cr3t
#KEYTOOL_CMD="keytool -genkeypair -keystore ks.p12 -storepass $KEYSTORE_PASSWORD -keypass $KEYSTORE_PASSWORD -storetype PKCS12 -validity 7300 -keyalg RSA -keysize 1024"
#
#$KEYTOOL_CMD -alias cn-ip -dname "uid=cn-is-not-first, CN=127.0.0.1, O=Hazelcast"

keytool -genkey -alias hz -keyalg RSA -keystore keystore.jks -validity 360 -dname "CN=172.20.1.1/255.255.0.0"
