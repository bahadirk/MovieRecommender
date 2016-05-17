import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by bahadirkirdan on 5/4/16.
 */
public class Movie {

    Client client;
    private final String  API_KEY = "ca23b5865fc1d0df85240e748a65150f";

    public Movie() {

    }

    public String getMovies(){

        client = ClientBuilder.newClient();
        Response response = client.target("http://api.themoviedb.org/3/discover/movie?&api_key=ca23b5865fc1d0df85240e748a65150f")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("Accept", "application/json")
                .get();

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());

        String movieJson = response.readEntity(String.class);

        return movieJson;
    }

}
