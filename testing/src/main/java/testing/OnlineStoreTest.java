package testing;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySource(properties = {
    "spring.cloud.config.import-check.enabled=false",
    "eureka.client.enabled=false",
    "axoniq.axonserver.enabled=false",
})
@SpringJUnitWebConfig
@DataJpaTest
public @interface OnlineStoreTest {
}
