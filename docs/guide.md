# Basic guideline

## Connect to H2 database
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

## Connect to MySQL database
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

## A note on hibernate dialect
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

## Bean validation with spring boot
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