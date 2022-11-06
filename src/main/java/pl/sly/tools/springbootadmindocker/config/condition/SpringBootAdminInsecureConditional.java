package pl.sly.tools.springbootadmindocker.config.condition;

import static java.lang.Boolean.FALSE;
import static pl.sly.tools.springbootadmindocker.config.condition.Constants.SPRING_BOOT_ADMIN_SECURITY_ENABLED;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Matches true if {spring.boot.admin.security.enabled} property is defined and value is false.
 */
public class SpringBootAdminInsecureConditional implements Condition {

  @Override
  public boolean matches(ConditionContext conditionContext,
                         AnnotatedTypeMetadata annotatedTypeMetadata) {
    Environment environment = conditionContext.getEnvironment();
    return FALSE.toString()
                .equalsIgnoreCase(environment.getProperty(SPRING_BOOT_ADMIN_SECURITY_ENABLED));
  }
}
