# Basic guideline

## Connect to database
### H2 database
1. Add H2 dependency
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Add properties to configure H2 database
```yaml
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

3. Need to add ``spring-boot-starter-data-jpa`` to auto configure the H2 database (``H2ConsoleAutoConfiguration``)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Read more: https://stackoverflow.com/questions/63587966/spring-boot-h2-database-not-found-not-created-by-liquibase

### MySQL database
1. Add dependency ``mysql-connector-java``
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```
2. Add properties to configure mysql database
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/photo-app
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
```

### A note on Hibernate dialect
I tested with H2, without the below properties, the H2 database still work well
```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

In case mysql, since Hibernate 6, we don't need explicitly declare hibernate dialect like below config:
```yaml
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```
Read more: https://vladmihalcea.com/hibernate-dialect/

## Config server
### Setup cloud bus
In this project, we use [RabbitMQ](https://www.rabbitmq.com/download.html) as message broker for cloud bus. I use docker to start RabbitMQ:
```shell
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management
```
RabbitMQ portal is on port 15672: http://localhost:15672 \
Login username / password: guest / guest

Spring cloud bus automatically configure the RabbitQM, the default config is:
```yaml
spring:
  rabbitmq:
    host: localhost
    username: guest
    password: guest
    port: 5672
```

### Refresh config
When we change the config in config-server, these changes won't be pushed to the client applications.
We need call the `/busrefresh` endpoint from config-server to let it push the changes to the client applications.
```shell
curl --location --request POST 'http://localhost:8888/actuator/busrefresh'
```

In the client applications:
- The property bindings made with the ``@ConfigurationProperties`` and the property readings directly from the ``Environment`` interface
are automatically refreshed.
- The attributes bounded with ``@Value`` (from Spring) in the beans having the annotation ``@RefreshScope`` is refreshed.
- The other property bindings won't be refreshed
Read more: https://soshace.com/spring-cloud-config-refresh-strategies/

### Encrypt/decrypt properties
To keep credentials properties (username, password, ...) more secured in properties file, we should encrypt them instead of storing plain text \
Here are the steps:

1. Add encryption key to bootstrap.properties/.yaml
```yaml
# Symmetric key
encrypt:
  key: 9mlnaslkjzv823jkasdiofahjs91aslfasjf1j
```
```yaml
# Asymmetric key
encrypt:
  key-store:
    location: apikey.jks
    alias: apikey
    password: test123456
```

**Note:** You can use ``keytool`` to generate keystore.
```shell
keytool -genkeypair -alias apikey -keyalg RSA -keystore apikey.jks
```

**Note:** Since spring cloud 2020, they bootstrap is not enabled by default, we have to add ``spring-cloud-starter-bootstrap``
to make bootstrap.properties loaded.
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

2. Make a ``POST`` request to config server to get the encrypted value
```shell
curl --location --request POST 'http://localhost:8888/encrypt' \
--header 'Content-Type: text/plain' \
--data-raw 'root'
```
3. Change the plain text to encrypted value with prefix ``{cipher}``
```yaml
token:
  signingKey: '{cipher}9a641a553fb4fed2ac5c1eba32a04d4766d14c95dcdff470cadbb496e35ed163'
```

**Note:** With yaml file, you have to put your encrypted value in ``'{cipher}...'`` to make it work. You don't need to do it with ``.properties`` file

## Bean validation
Starting with Boot 2.3, we also need to explicitly add the ``spring-boot-starter-validation`` dependency to have bean validation ``@NotNull``, ``@NotBlank``, etc
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## Configuration properties
From Spring 2.2+ ``@ConfigurationProperties`` scanning Classes annotated with ``@ConfigurationProperties`` can now be found via
classpath scanning as an alternative to using ``@EnableConfigurationProperties`` or ``@Component`` \
Add ``@ConfigurationPropertiesScan`` to your application to enable scanning \
```java
// Old way
@EnableConfigurationProperties // Add this annotation for every @ConfigurationProperties
@ConfigurationProperties(prefix = "token")
class TokenProperties {
    //
}

// New way
@SpringBootApplication
@ConfigurationPropertiesScan // It will automatically scan all properties bean
class Application {
    //
}

@ConfigurationProperties(prefix = "token")
class TokenProperties {
    //
}
```

## Security configuration
Since spring 2.7, the ``WebSecurityConfigurerAdapter`` is deprecated. We have to build Security configuration by ourselves \
Check this post for more detail: https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter

**Note:** If you use H2 for testing, you need at the following config to make h2-console work properly
```java
http.headers().frameOptions().disable()
```