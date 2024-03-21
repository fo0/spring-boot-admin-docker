package pl.sly.tools.springbootadmindocker.config;

import static reactor.netty.http.client.HttpClient.create;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.netty.http.client.HttpClient;

/**
 * @author fo0
 */
@ConditionalOnProperty(name = "webclient.ssl.insecure", havingValue = "true")
public class WebClientConfig {

  @Primary
  @Bean
  public Builder createInsecureClient() throws SSLException {
    HttpClient httpClient = create();
    httpClient = configure(httpClient);

    final Builder builder = WebClient.builder();
    return builder.clientConnector(new ReactorClientHttpConnector(httpClient));
  }

  public HttpClient configure(HttpClient httpClient) throws SSLException {
    // Disable SSL Checks
    SslContext sslContext =
        SslContextBuilder
            //
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();

    // httpclient must be reassigned, else the ssl configuration is ignored
    return httpClient.secure(
        //
        ssl ->
            ssl.sslContext(sslContext)
                .handlerConfigurator(
                    handler -> {
                      SSLEngine engine = handler.engine();
                      // engine.setNeedClientAuth(true);
                      SSLParameters params = new SSLParameters();
                      List<SNIMatcher> matchers = new LinkedList<>();

                      SNIMatcher matcher =
                          new SNIMatcher(0) {
                            @Override
                            public boolean matches(SNIServerName serverName) {
                              return true;
                            }
                          };

                      matchers.add(matcher);
                      params.setSNIMatchers(matchers);
                      engine.setSSLParameters(params);
                    }));
  }
}
