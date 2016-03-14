package com.tursi;

import com.tursi.domain.RedditArticle;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class RedditApiClientServiceTest {

    private String fixture = "fixture.json";
    private String jsonContent;

    private MockRestServiceServer mockServer;


    @Autowired
    @InjectMocks
    RedditApiClientService cut;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

     @Before
    public void setup() throws IOException, URISyntaxException{
        MockitoAnnotations.initMocks(this);

        URI uri = ClassLoader.getSystemResource(fixture).toURI();
        jsonContent = new String(Files.readAllBytes(Paths.get(uri)));

        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        cut.restTemplate = restTemplate;

    }

    @Test
    public void searchApiReturnsResult() throws Exception {
        mockServer.expect(
                requestTo("https://www.reddit.com/search.json?q=sample%20search"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body(jsonContent).contentType(MediaType.APPLICATION_JSON));

        Collection<RedditArticle> result = cut.searchApi("sample search");
        mockServer.verify();
        assertFalse(result.isEmpty());
    }

    @Test
    public void searchApiProcessesResultSuccessfully() throws Exception {

        mockServer.expect(
                requestTo("https://www.reddit.com/search.json?q=whatever"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body(jsonContent).contentType(MediaType.APPLICATION_JSON));

        Collection<RedditArticle> result = cut.searchApi("whatever");
        mockServer.verify();

        assertEquals(3, result.size());

        Iterator<RedditArticle> i = result.iterator();

        RedditArticle article = i.next();
        assertEquals(article.getAuthor(), "Gameofbands");
        assertEquals(article.getScore(), "7");
        assertEquals(article.getSelfText(), "data is often output without line breaks to save space, it is extremely difficult to actually read and make sense of it. This little tool hoped to solve the problem by formatting the JSON data so that it is easy to read and debug by human beings.");
        assertEquals(article.getSubReddit(), "this is a subreddit");
        assertEquals(article.getTitle(), "Signups for Game of Bands Round 71: Pop Chart Hits");
        assertEquals(article.getUrl(), "https://www.reddit.com/r/Gameofbands/comments/380azy/signups_for_game_of_bands_round_71_pop_chart_hits/");

        // verify sort
        article = i.next();
        assertEquals(article.getAuthor(), "Swazi666");
        article = i.next();
        assertEquals(article.getAuthor(), "TNMagician");
    }

    @Test
    public void searchApiThrowsExceptionOn500 () throws Exception {
        mockServer.expect(
                requestTo("https://www.reddit.com/search.json?q=sample%20search"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        exception.expect(HttpServerErrorException.class);

        cut.searchApi("sample search");
        mockServer.verify();

    }

}

