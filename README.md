This is a very simple reddit search service, written in Java and using Spring Boot.

You can view the running app at `https://stevetursi-redditsearch.herokuapp.com/search/{searchTerm}`

For exmaple: https://stevetursi-redditsearch.herokuapp.com/search/star%20trek

##Building

Clone the repo and run `./mvnw clean install`

##Running

Typing `java -jar target/redditsearch-0.0.1-SNAPSHOT.jar` at the command line will start the app. It should then be reachable at `http://localhost:8080/search/{searchTerm}`
