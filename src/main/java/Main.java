/**
 * Created by bahadirkirdan on 4/16/16.
 */

import org.openrdf.model.vocabulary.OWL;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.File;


public class Main {

    public static void main(String arg[]) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Client client = ClientBuilder.newClient();
        Response response = client.target("http://api.themoviedb.org/3/discover/movie?with_genres=18&api_key=ca23b5865fc1d0df85240e748a65150f")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("Accept", "application/json")
                .get();

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));


        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = load(ontologyManager);

       // Alttaki iki satırı ontology'yi sorunsuz load ediyor mu testi için koydum, kullanmayacagiz
       // IRI destination = IRI.create(new File("/Users/burakatalay/Desktop/movieontology2.owl"));
       // ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), destination);


    }

    static OWLOntology load(OWLOntologyManager manager) throws OWLOntologyCreationException {
        return manager.loadOntologyFromOntologyDocument(new File("/Users/burakatalay/Desktop/movieontology.owl"));

    }
}


