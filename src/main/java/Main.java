/**
 * Created by bahadirkirdan on 4/16/16.
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String arg[]) throws OWLOntologyCreationException, OWLOntologyStorageException, ParseException {


        Movie movie = new Movie();

        String movieJSON = movie.getJSON();

        OntologyManager ontologyManager = new OntologyManager();

        JSONParser parser = new JSONParser();

        Object json = parser.parse(movieJSON);
        JSONObject jsonObject = (JSONObject) json;

        JSONArray movieResults = (JSONArray) jsonObject.get("results");

        for(Object movieResult : movieResults){
            JSONObject movieObj = (JSONObject) movieResult;
            String title = movieObj.get("title").toString();
            ontologyManager.addIndividualToOntology(title);
        }


 /*       OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = load(ontologyManager);


        createNewOnto();

       //Alttaki iki satırı ontology'yi sorunsuz load ediyor mu testi için koydum, kullanmayacagiz
       /* IRI ontologyIRI = IRI.create(new File("/Users/bahadirkirdan/IdeaProjects/movieontology2.owl"));
        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);
*/


    }

    public static OWLOntology load(OWLOntologyManager manager) throws OWLOntologyCreationException {
        return manager.loadOntologyFromOntologyDocument( new File("/Users/bahadirkirdan/IdeaProjects/movieontology.owl") );

    }

    public static void createNewOnto() throws OWLOntologyCreationException, OWLOntologyStorageException {

        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRI = IRI.create( new File("/Users/bahadirkirdan/IdeaProjects/movieontology2.owl") );
        OWLOntology ontology = load(ontologyManager);
        OWLDataFactory factory = ontologyManager.getOWLDataFactory();

        OWLIndividual john = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#John"));
        OWLIndividual mary = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Mary"));
        OWLIndividual susan = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Susan"));
        OWLIndividual bill = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Bill"));

        OWLObjectProperty hasWife = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasWife"));

        OWLObjectPropertyAssertionAxiom axiom1 = factory.getOWLObjectPropertyAssertionAxiom(hasWife, john, mary);

        AddAxiom addAxiom1 = new AddAxiom(ontology, axiom1);

        ontologyManager.applyChange(addAxiom1);

        System.out.println("RDF/XML: ");
        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);

    }
}


