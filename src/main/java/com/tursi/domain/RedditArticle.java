/********************************************************************
 * Created by prc0499 on 3/12/2016.
 *********************************************************************/
package com.tursi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditArticle {

    @JsonProperty("subreddit")
    private String subReddit;

    @JsonProperty("selftext")
    private String selfText;

    private String author;
    private String url;
    private String title;
    private String score;

}
