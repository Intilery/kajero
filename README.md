# Kajero Docs for Spring Boot

Makes Kajero Notebooks available for use as part of `Spring Actuator` end points `/actuator/docs`

Put your docs into `src/main/resources/docs`, with the root document called `index.md`

## Adding to a Maven Project

### `pom.xml` Configuration

Add the Repository:
```xml
<repositories>
    <repository>
        <id>public-mvn-repo</id>
        <url>https://rawgit.com/Intilery/artifacts-public/master</url>
        <releases>
            <enabled>true</enabled>
        </releases>
    </repository>
</repositories>
```

Add the Dependency:
```xml
<build>
    <dependencies>
        <dependency>
            <groupId>com.intilery</groupId>
            <artifactId>kajero</artifactId>
            <version>1.0.0.</version>
        </dependency>
    </dependencies>
</build>
```

### Spring Boot Configuration

Configure the endpoint to be on as part of Spring `application.properties`:
```
management.endpoints.web.exposure.include=health,info,docs
```

Ensure that the `DocumentEndpointConguration` is included in the _ComponentScan_ on the _SpringBootApplication_:
```java
@SpringBootApplication
@ComponentScan("com.intilery")
public class Application {

    @Autowired
    private ApplicationConfig config;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Adding Docs

Add a `docs` folder to the `src/main/resources`. Root document is called `index.md`.

See the Kajero example doc as served from _karjero-ui_ @ `/webjars/kajero/example.html`.

Whilst working on the project the documents are loaded from the project directories, once deployed, the files are loaded from the classpath.
