package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.config.properties.EmailConfigProperties.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.com.sw2you.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2you.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.email.TemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Test;

public class TemplateConfigPropertiesIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private TemplateConfigProperties templateConfigProperties;

    @Value("${realmmeet.email.templates.allocationCreated.subject}")
    private String allocationCreatedSubject;

    @Value("${realmmeet.email.templates.allocationCreated.templateName}")
    private String allocationCreatedTemplateName;

    @Value("${realmmeet.email.templates.allocationUpdated.subject}")
    private String allocationUpdatedSubject;

    @Value("${realmmeet.email.templates.allocationUpdated.templateName}")
    private String allocationUpdatedTemplateName;

    @Value("${realmmeet.email.templates.allocationDeleted.subject}")
    private String allocationDeletedSubject;

    @Value("${realmmeet.email.templates.allocationDeleted.templateName}")
    private String allocationDeletedTemplateName;

    @Test
    void testLoadConfigProperties() {
        assertNotNull(allocationCreatedSubject);
        assertEquals(allocationCreatedSubject, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_CREATED).getSubject());
        assertNotNull(allocationCreatedTemplateName);
        assertEquals(allocationCreatedTemplateName, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_CREATED).getTemplateName());

        assertNotNull(allocationUpdatedSubject);
        assertEquals(allocationUpdatedSubject, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_UPDATED).getSubject());
        assertNotNull(allocationUpdatedTemplateName);
        assertEquals(allocationUpdatedTemplateName, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_UPDATED).getTemplateName());

        assertNotNull(allocationDeletedSubject);
        assertEquals(allocationDeletedSubject, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_DELETED).getSubject());
        assertNotNull(allocationDeletedSubject);
        assertEquals(allocationDeletedTemplateName, templateConfigProperties.getEmailTemplate(TemplateType.ALLOCATION_DELETED).getTemplateName());
    }
}