package com.bridgelabz.moviecatalogservice.Resource;

import com.bridgelabz.moviecatalogservice.Model.CatalogItems;
import com.bridgelabz.moviecatalogservice.Model.Movie;
import com.bridgelabz.moviecatalogservice.Model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping(value ={"","/",},method = RequestMethod.GET)
    public String home(){
        return "Welcome to the movie catalog Service.";
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItems>getCatalogItems(@PathVariable ("userId") String userId){

      //  WebClient.Builder builder = WebClient.builder();

      UserRating userRating = restTemplate.getForObject("http://RATINGS-DATA-SERVICE/ratingsData/users/"+userId, UserRating.class);

        return userRating.getRatings().stream().map(rating -> {
                    Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICES/movies/"+rating.getMovieId(), Movie.class);

                    /* Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/"+rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
                    */

                    return new CatalogItems(movie.getName(), "Desc", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}
