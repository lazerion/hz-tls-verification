#!/bin/bash

# This script generates keystores which are used in TLS host verification tests
# Once we move out from Java 6 support, we can generate the key material by using a Maven keytool plugin.
# KeyTool in Java 6 doesn't support the SAN extenxion.

KEYSTORE_PASSWORD=123456

# key material for TlsHostVerifierTest.java

function createKeystore
{
  if [ $# -gt 2 ]; then
    keytool -genkeypair -keystore $1 -storepass $KEYSTORE_PASSWORD -keypass $KEYSTORE_PASSWORD -storetype JKS -validity 7300 -keyalg RSA -keysize 1024 \
      -alias test -dname "$2" -ext "$3"
  else
    keytool -genkeypair -keystore $1 -storepass $KEYSTORE_PASSWORD -keypass $KEYSTORE_PASSWORD -storetype JKS -validity 7300 -keyalg RSA -keysize 1024 \
      -alias test -dname "$2"
  fi
  keytool -export -alias test -keystore $1 -storepass $KEYSTORE_PASSWORD -file $1.crt
  keytool -importcert -noprompt -alias $1 -keystore truststore.jks -storepass $KEYSTORE_PASSWORD -file $1.crt
  rm $1.crt
}

createKeystore keystore.jks "cn=172.20.0.1/255.255.0.0"
