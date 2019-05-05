package com.intilery.kajero;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerEndpoint(id = "docs")
public class DocumentEndpoint {
    private static final String EMBEDDED_PATH = "classpath:docs/";
    private static final String DEVELOPMENT_PATH = "/src/main/resources/docs/";

    private final ResourceLoader resourceLoader;
    private final String blankTemplate;
    private final String documentTemplate;

    DocumentEndpoint(ResourceLoader resourceLoader, String blankTemplate, String documentTemplate) {
        this.resourceLoader = resourceLoader;
        this.blankTemplate = blankTemplate;
        this.documentTemplate = documentTemplate;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity getIndexDoc() throws IOException {
        return ResponseEntity.ok(mergeDoc("index"));
    }

    @GetMapping(value = "/{name}",  produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity getDoc(@PathVariable("name") String name) throws IOException {
        return ResponseEntity.ok(mergeDoc(name));
    }

    private String mergeDoc(String path) throws IOException {
        Resource documentResource = findDocumentResource(path);
        String document = getDocumentTemplate(documentResource);

        return documentTemplate
                .replace("{{TITLE}}", path)
                .replace("{{MARKDOWN}}", document);
    }

    private String getDocumentTemplate(Resource documentResource) throws IOException {
        String document;
        if (documentResource.exists()) {
            document = StreamUtils.copyToString( documentResource.getInputStream(), Charset.defaultCharset());
        } else {
            document = blankTemplate.replace("{{CREATED}}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        return document;
    }

    private Resource findDocumentResource(String path) {
        Resource documentResource = resourceLoader.getResource("file:" + System.getProperty("user.dir") + DEVELOPMENT_PATH + path + ".md");
        if (!documentResource.exists()) {
            documentResource = resourceLoader.getResource(EMBEDDED_PATH + path + ".md");
        }
        return documentResource;
    }
}
