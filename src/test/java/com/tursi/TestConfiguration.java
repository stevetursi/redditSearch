/********************************************************************
 * Created by prc0499 on 3/14/2016.
 *********************************************************************/
package com.tursi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class TestConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        Properties props = new Properties();
        props.setProperty("reddit.searchuri", "https://www.reddit.com/search.json?q=%s");

        PropertySourcesPlaceholderConfigurer source = new PropertySourcesPlaceholderConfigurer();
        source.setProperties(props);

        return source;
    }

    @Bean
    public RedditApiClientService getRedditApiClientService() {
        return new RedditApiClientService();
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

}
