package cn.edu.scau.acm.acmer.config;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.URI;

@Configuration
public class RestTemplateConfig {

    @Bean
    @ConditionalOnMissingBean({ RestOperations.class, RestTemplate.class })
    //Spring Boot的自动配置机制依靠@ConditionalOnMissingBean注解判断是否执行初始化代码，
    // 即如果用户已经创建了bean，则相关的初始化代码不再执行。
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
//        restTemplate.setRequestFactory(new HttpComponentsClientRestfulHttpRequestFactory());
         return new RestTemplate(factory);
    }

    @Bean
    @ConditionalOnMissingBean({ClientHttpRequestFactory.class})
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(15000);// ms
        factory.setConnectTimeout(15000);// ms
        factory.setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)));
        return factory;
    }

//    private static final class HttpComponentsClientRestfulHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
//        @Override
//        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
//            if (httpMethod == HttpMethod.GET) {
//                return new HttpGetRequestWithEntity(uri);
//            }
//            return super.createHttpUriRequest(httpMethod, uri);
//        }
//    }
//
//    private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
//        public HttpGetRequestWithEntity(final URI uri) {
//            super.setURI(uri);
//        }
//
//        @Override
//        public String getMethod() {
//            return HttpMethod.GET.name();
//        }
//    }

}
