/********************************************************************
 * Created by prc0499 on 3/12/2016.
 *********************************************************************/
package com.tursi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchWebService {

    @Autowired
    private RedditApiClientService apiClient;

    @RequestMapping("/search/{searchText}")
    public Object search(@PathVariable("searchText") String searchText) throws Exception {

        return apiClient.searchApi(searchText);

    }

}
