package pl.sly.tools.springbootadmindocker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sly.tools.springbootadmindocker.config.InsecureConfig;
import pl.sly.tools.springbootadmindocker.config.SecurityConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    properties = "spring.boot.admin.security.enabled=false",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsecureConfigTest {

  private static final String DEFAULT_PATH = "/";
  private static final String HTML_APP_DIV = "<title>Spring Boot Admin Docker</title>";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void whenSecurityDisabled_shouldInjectInsecureConfig() {
    // when
    InsecureConfig insecureConfig = applicationContext.getBean(InsecureConfig.class);

    // then
    Assertions.assertNotNull(insecureConfig);
  }

  @Test
  public void whenSecurityDisabled_shouldNotInjectSecurityConfig() {
    // when
    Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                            () -> applicationContext.getBean(SecurityConfig.class));

    // then
    // NoSuchBeanDefinitionException should be thrown
  }

  @Test
  public void whenSecurityDisabled_shouldReturnHttp200statusCodeAndValidBody() {
    // given & when
    ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(DEFAULT_PATH,
                                                                          String.class);

    // then
    Assertions.assertNotNull(responseEntity);
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertTrue(responseEntity.getBody()
                                        .contains(HTML_APP_DIV));
    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
