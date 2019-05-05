package com.intilery.kajero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
@ConditionalOnEnabledEndpoint(endpoint = DocumentEndpoint.class)
public class DocumentEndpointConfiguration {

    private final ResourceLoader resourceLoader;

    @Value("classpath:/kajero.blank.md.tmpl")
    Resource blankDocumentTemplateResource;

    @Value("classpath:/kajero.html.tmpl")
    Resource documentTemplateResource;

    @Autowired
    DocumentEndpointConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @ConditionalOnMissingBean
    public DocumentEndpoint documentEndpoint() throws IOException {
        String blankTemplate = StreamUtils.copyToString(blankDocumentTemplateResource.getInputStream(), Charset.defaultCharset());
        String documentTemplate = StreamUtils.copyToString(documentTemplateResource.getInputStream(), Charset.defaultCharset());
        return new DocumentEndpoint(resourceLoader, blankTemplate, documentTemplate);
    }
}

