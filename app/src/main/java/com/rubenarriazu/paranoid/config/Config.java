package com.rubenarriazu.paranoid.config;

public class Config {
    // If you change WEB_SERVER you must changed res/xml/network_security_config.xml as well
    public static final String WEB_SERVER = "https://paranoid.rubenarriazu.com/";

    // This is the server used by the unitary tests
    public static final String TESTING_WEB_SERVER = "http://localhost:8000/";

    // Encrypted shared preferences to store a username, password and token
    public static final String CREDENTIALS = "credentials.xml";
}
