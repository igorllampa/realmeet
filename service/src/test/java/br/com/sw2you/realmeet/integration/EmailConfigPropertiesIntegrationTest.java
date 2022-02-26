package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.config.properties.EmailConfigProperties.*;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmailConfigPropertiesIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private EmailConfigProperties emailConfigProperties;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.port}")
    private String port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnabled;

    @Test
    void testLoadConfigProperties(){

        Assertions.assertNotNull(host);
        Assertions.assertEquals(host, emailConfigProperties.getHost());

        Assertions.assertNotNull(username);
        Assertions.assertEquals(username, emailConfigProperties.getUsername());

        Assertions.assertNotNull(password);
        Assertions.assertEquals(password, emailConfigProperties.getPassword());

        Assertions.assertNotNull(from);
        Assertions.assertEquals(from, emailConfigProperties.getFrom());

        Assertions.assertNotNull(protocol);
        Assertions.assertEquals(protocol, emailConfigProperties.getProperty(PROPERTY_TRANSPORT_PROTOCOL));

        Assertions.assertNotNull(port);
        Assertions.assertEquals(port, emailConfigProperties.getProperty(PROPERTY_SMTP_PORT));

        Assertions.assertNotNull(auth);
        Assertions.assertEquals(auth, emailConfigProperties.getProperty(PROPERTY_SMTP_AUTH));

        Assertions.assertNotNull(starttlsEnabled);
        Assertions.assertEquals(starttlsEnabled, emailConfigProperties.getProperty(PROPERTY_SMTP_STARTTLS_ENABLE));
    }
}
