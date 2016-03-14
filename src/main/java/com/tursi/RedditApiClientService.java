package com.tursi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tursi.domain.RedditArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.Iterator;
import java.util.List;

@Service
public class RedditApiClientService {

    @Value("${reddit.searchuri}")
    private String searchUri;

    @Autowired
    RestOperations restTemplate;

    public List<RedditArticle> searchApi(String searchText) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Steve's REST client"); // reddit will place strict rate limits if you don't specify a user-agent
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange (
                String.format(searchUri, searchText),
                HttpMethod.GET,
                entity,
                String.class
        );

        List<RedditArticle> result = mapRedditResponse(responseEntity.getBody());

        result.sort((a,b) -> a.getAuthor().compareTo(b.getAuthor()));

        return result;
    }


    private List<RedditArticle> mapRedditResponse(String jsonTree) throws Exception {
        List<RedditArticle> result = Lists.newArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonTree);
        JsonNode data1Node = rootNode.path("data");
        JsonNode childrenNode = data1Node.path("children");
        Iterator<JsonNode> entries = childrenNode.elements();

        while (entries.hasNext()) {
            JsonNode data2Node = entries.next().path("data");
            RedditArticle article = mapper.treeToValue(data2Node, RedditArticle.class);
            result.add(article);
        }

        return result;

    }


}
