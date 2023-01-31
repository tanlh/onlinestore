# Config server note

## Reference
Spring config server: https://docs.spring.io/spring-cloud-config/docs/current/reference/html/

Spring cloud bus: https://docs.spring.io/spring-cloud-bus/docs/current/reference/html/

## Setup RabbitMQ
In this project, we use RabbitMQ as message broker for cloud bus \
Guide download & install RabbitMQ: https://www.rabbitmq.com/download.html
```shell
# latest RabbitMQ 3.11
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

## Refresh config
Send a POST refresh to busrefresh actuator endpoint:
```shell
curl --location --request POST 'http://localhost:8888/actuator/busrefresh'
```

## Config client refresh properties
In the config client, the property bindings made with the ``@ConfigurationProperties`` and the property readings directly from the ``Environment`` interface
are automatically refreshed.

The attributes bounded with ``@Value`` (from Spring) in the beans having the annotation ``@RefreshScope`` is refreshed.

Read more: https://soshace.com/spring-cloud-config-refresh-strategies/

## Encryption and decryption
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