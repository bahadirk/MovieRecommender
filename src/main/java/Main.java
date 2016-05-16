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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

public class Main {

    private static final String ONTOLOGY_PATH_LOAD = "/Users/bahadirkirdan/IdeaProjects/movieontology.owl";
    private static final String ONTOLOGY_PATH_SAVE = "/Users/bahadirkirdan/IdeaProjects/movieontology2.owl";
    private static final String IRI_URI = "http://www.semanticweb.org/bahadirkirdan/ontologies/2016/4/untitled-ontology-44";
    private static final String  API_KEY = "ca23b5865fc1d0df85240e748a65150f";

    public static void main(String arg[]) throws OWLOntologyCreationException, OWLOntologyStorageException, ParseException {


        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument( new File(ONTOLOGY_PATH_LOAD) );

        IRI ontologyIRI = IRI.create( new File(ONTOLOGY_PATH_SAVE) );


        OWLDataFactory factory = ontologyManager.getOWLDataFactory();

        for(int i = 1; i < 8; i++){

            Client client = ClientBuilder.newClient();

            Response response = client.target("http://api.themoviedb.org/3/discover/movie?&api_key="  + API_KEY + "&page=" + Integer.toString(i) )
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Accept", "application/json")
                    .get();


            String movieJson = response.readEntity(String.class);

            JSONParser parser = new JSONParser();

            Object json = parser.parse(movieJson);
            JSONObject jsonObject = (JSONObject) json;

            JSONArray movieResults = (JSONArray) jsonObject.get("results");

            for(Object movieResult : movieResults){

                JSONObject movieObj = (JSONObject) movieResult;

                String title = movieObj.get("title").toString();
                String releaseDate = movieObj.get("release_date").toString();
                String language = movieObj.get("original_language").toString();
                JSONArray genreIdArrays = (JSONArray) movieObj.get("genre_ids");

                title = title.replaceAll("\\s+", "");
                releaseDate = releaseDate.replaceAll("\\s+", "");


                /* Class Movie */
                OWLClass movieClass = factory.getOWLClass(IRI.create(IRI_URI + "#Movie"));

                OWLIndividual movieIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + title));

                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(movieClass, movieIndividual);

                AddAxiom addAxiom = new AddAxiom(ontology, axiom);

                ontologyManager.applyChange(addAxiom);


                /* Class Release Date */
                OWLClass releaseDateClass = factory.getOWLClass(IRI.create(IRI_URI + "#ReleaseDate"));

                OWLIndividual releaseDateIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + releaseDate));

                OWLAxiom axiom1 = factory.getOWLClassAssertionAxiom(releaseDateClass, releaseDateIndividual);

                AddAxiom addAxiom1 = new AddAxiom(ontology, axiom1);

                ontologyManager.applyChange(addAxiom1);


                /* Object Property hasReleaseDate */
                OWLObjectProperty hasReleaseDate = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasReleaseDate"));

                OWLObjectPropertyAssertionAxiom axiom2 = factory.getOWLObjectPropertyAssertionAxiom(hasReleaseDate, movieIndividual, releaseDateIndividual);

                AddAxiom addAxiom2 = new AddAxiom(ontology, axiom2);

                ontologyManager.applyChange(addAxiom2);




                for(Object genreIdObj : genreIdArrays){

                    String genreId = genreIdObj.toString();

                    /* Class Genre */
                    OWLClass genreClass = factory.getOWLClass(IRI.create(IRI_URI + "#Genre"));

                    OWLIndividual genreIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + genreId));

                    OWLAxiom axiom3 = factory.getOWLClassAssertionAxiom(genreClass, genreIndividual);

                    AddAxiom addAxiom3 = new AddAxiom(ontology, axiom3);

                    ontologyManager.applyChange(addAxiom3);


                    /* Object Property hasGenre */
                    OWLObjectProperty hasGenre = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasGenre"));

                    OWLObjectPropertyAssertionAxiom axiom4 = factory.getOWLObjectPropertyAssertionAxiom(hasGenre, movieIndividual, genreIndividual);

                    AddAxiom addAxiom4 = new AddAxiom(ontology, axiom4);

                    ontologyManager.applyChange(addAxiom4);

                }


                 /* Class Language Date */
                OWLClass languageClass = factory.getOWLClass(IRI.create(IRI_URI + "#Language"));

                OWLIndividual languageIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + language));

                OWLAxiom axiom5 = factory.getOWLClassAssertionAxiom(languageClass, languageIndividual);

                AddAxiom addAxiom5 = new AddAxiom(ontology, axiom5);

                ontologyManager.applyChange(addAxiom5);


                /* Object Property hasLanguage */
                OWLObjectProperty hasLanguage = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasLanguage"));

                OWLObjectPropertyAssertionAxiom axiom6 = factory.getOWLObjectPropertyAssertionAxiom(hasLanguage, movieIndividual, languageIndividual);

                AddAxiom addAxiom6 = new AddAxiom(ontology, axiom6);

                ontologyManager.applyChange(addAxiom6);

            }


        }


        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);


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


