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
    private static final String API_KEY = "ca23b5865fc1d0df85240e748a65150f";

    public static void main(String arg[]) throws OWLOntologyCreationException, OWLOntologyStorageException, ParseException {


        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(new File(ONTOLOGY_PATH_LOAD));

        IRI ontologyIRI = IRI.create(new File(ONTOLOGY_PATH_SAVE));


        OWLDataFactory factory = ontologyManager.getOWLDataFactory();

        for (int i = 100; i < 200; i++) {

            Client client = ClientBuilder.newClient();

            String link = "http://api.themoviedb.org/3/movie/" + Integer.toString(i) + "?&api_key=" + API_KEY;

            Response response = client.target(link)
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Accept", "application/json")
                    .get();


            String movieJson = response.readEntity(String.class);

            JSONParser parser = new JSONParser();

            Object json = parser.parse(movieJson);
            JSONObject movieObj = (JSONObject) json;

            if(movieObj.get("title") == null)
                continue;

            String originalTitle = movieObj.get("title").toString();
            String releaseDate = movieObj.get("release_date").toString();
            String language = movieObj.get("original_language").toString();
            String voteAverage = movieObj.get("vote_average").toString();
            String movieId = movieObj.get("id").toString();
            JSONArray genreIdArrays = (JSONArray) movieObj.get("genres");


            String title = originalTitle.replaceAll("\\s+", "");
            releaseDate = releaseDate.replaceAll("\\s+", "");


                /* Class Movie */
            OWLClass movieClass = factory.getOWLClass(IRI.create(IRI_URI + "#Movie"));
            OWLIndividual movieIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + title));
            OWLAxiom axiom = factory.getOWLClassAssertionAxiom(movieClass, movieIndividual);
            AddAxiom addAxiom = new AddAxiom(ontology, axiom);
            ontologyManager.applyChange(addAxiom);


            OWLDataProperty dpMovieId = factory.getOWLDataProperty(IRI.create(IRI_URI + "#id"));
            OWLDataPropertyAssertionAxiom axiom11 = factory.getOWLDataPropertyAssertionAxiom(dpMovieId, movieIndividual, movieId);
            AddAxiom addAxiom11 = new AddAxiom(ontology, axiom11);
            ontologyManager.applyChange(addAxiom11);


            OWLDataProperty dpMovieName = factory.getOWLDataProperty(IRI.create(IRI_URI + "#name"));
            OWLDataPropertyAssertionAxiom axiom13 = factory.getOWLDataPropertyAssertionAxiom(dpMovieName, movieIndividual, originalTitle);
            AddAxiom addAxiom13 = new AddAxiom(ontology, axiom13);
            ontologyManager.applyChange(addAxiom13);


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


            for (Object genre : genreIdArrays) {

                JSONObject genreObj = (JSONObject) genre;

                String genreId = genreObj.get("id").toString();
                String genreName = genreObj.get("name").toString();

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


                OWLDataProperty dpGenreId = factory.getOWLDataProperty(IRI.create(IRI_URI + "#id"));
                OWLDataPropertyAssertionAxiom axiom12 = factory.getOWLDataPropertyAssertionAxiom(dpGenreId, genreIndividual, genreId);
                AddAxiom addAxiom12 = new AddAxiom(ontology, axiom12);
                ontologyManager.applyChange(addAxiom12);


                OWLDataProperty dpGenreName = factory.getOWLDataProperty(IRI.create(IRI_URI + "#name"));
                OWLDataPropertyAssertionAxiom axiom14 = factory.getOWLDataPropertyAssertionAxiom(dpGenreId, genreIndividual, genreName);
                AddAxiom addAxiom14 = new AddAxiom(ontology, axiom14);
                ontologyManager.applyChange(addAxiom14);

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



                /* Vote Average Language Date */
            OWLClass voteAverageClass = factory.getOWLClass(IRI.create(IRI_URI + "#VoteAverage"));
            OWLIndividual voteAverageIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + voteAverage));
            OWLAxiom axiom7 = factory.getOWLClassAssertionAxiom(voteAverageClass, voteAverageIndividual);
            AddAxiom addAxiom7 = new AddAxiom(ontology, axiom7);
            ontologyManager.applyChange(addAxiom7);


                /* Object Property hasLanguage */
            OWLObjectProperty hasVoteAverage = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasVoteAverage"));
            OWLObjectPropertyAssertionAxiom axiom8 = factory.getOWLObjectPropertyAssertionAxiom(hasVoteAverage, movieIndividual, voteAverageIndividual);
            AddAxiom addAxiom8 = new AddAxiom(ontology, axiom8);
            ontologyManager.applyChange(addAxiom8);


            client = ClientBuilder.newClient();


            link = "http://api.themoviedb.org/3/movie/" + movieId + "/casts?&api_key=" + API_KEY;

            response = client.target(link)
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Accept", "application/json")
                    .get();

            String movieCastCrewJSON = response.readEntity(String.class);

            parser = new JSONParser();

            json = parser.parse(movieCastCrewJSON);
            JSONObject movieCastCrewObj = (JSONObject) json;

            JSONArray castResults = (JSONArray) movieCastCrewObj.get("cast");
            JSONArray crewResults = (JSONArray) movieCastCrewObj.get("crew");

            if (castResults != null) {

                for (Object cast : castResults) {

                    JSONObject castObj = (JSONObject) cast;
                    String originalActorName = castObj.get("name").toString();
                    String actorId = castObj.get("id").toString();
                    String actorName = originalActorName.replaceAll("\\s+", "");


                    /* Class Actor */
                    OWLClass actorClass = factory.getOWLClass(IRI.create(IRI_URI + "#Actor"));
                    OWLIndividual actorIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + actorName));
                    OWLAxiom axiom9 = factory.getOWLClassAssertionAxiom(actorClass, actorIndividual);
                    AddAxiom addAxiom9 = new AddAxiom(ontology, axiom9);
                    ontologyManager.applyChange(addAxiom9);


                    /* Object Property hasActor */
                    OWLObjectProperty hasActor = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasActor"));
                    OWLObjectPropertyAssertionAxiom axiom10 = factory.getOWLObjectPropertyAssertionAxiom(hasActor, movieIndividual, actorIndividual);
                    AddAxiom addAxiom10 = new AddAxiom(ontology, axiom10);
                    ontologyManager.applyChange(addAxiom10);


                    OWLDataProperty dpActorName = factory.getOWLDataProperty(IRI.create(IRI_URI + "#name"));
                    OWLDataPropertyAssertionAxiom axiom14 = factory.getOWLDataPropertyAssertionAxiom(dpActorName, actorIndividual, originalActorName);
                    AddAxiom addAxiom14 = new AddAxiom(ontology, axiom14);
                    ontologyManager.applyChange(addAxiom14);


                    OWLDataProperty dpActorId = factory.getOWLDataProperty(IRI.create(IRI_URI + "#id"));
                    OWLDataPropertyAssertionAxiom axiom15 = factory.getOWLDataPropertyAssertionAxiom(dpActorId, actorIndividual, actorId);
                    AddAxiom addAxiom15 = new AddAxiom(ontology, axiom15);
                    ontologyManager.applyChange(addAxiom15);


                }
            }

            if (crewResults != null) {

                for (Object crew : crewResults) {

                    JSONObject crewObj = (JSONObject) crew;
                    String job = crewObj.get("job").toString();

                    if (job.equals("Director")) {

                        String originalDirectorName = crewObj.get("name").toString();
                        String directorId = crewObj.get("id").toString();
                        String directorName = originalDirectorName.replaceAll("\\s+", "");

                            /* Class Director */
                        OWLClass directorClass = factory.getOWLClass(IRI.create(IRI_URI + "#Director"));
                        OWLIndividual directorIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + directorName));
                        OWLAxiom axiom12 = factory.getOWLClassAssertionAxiom(directorClass, directorIndividual);
                        AddAxiom addAxiom12 = new AddAxiom(ontology, axiom12);
                        ontologyManager.applyChange(addAxiom12);


                            /* Object Property hasDirector */
                        OWLObjectProperty hasDirector = factory.getOWLObjectProperty(IRI.create(IRI_URI + "#hasDirector"));
                        OWLObjectPropertyAssertionAxiom axiom16 = factory.getOWLObjectPropertyAssertionAxiom(hasDirector, movieIndividual, directorIndividual);
                        AddAxiom addAxiom16 = new AddAxiom(ontology, axiom16);
                        ontologyManager.applyChange(addAxiom16);



                        OWLDataProperty dpDirectorName = factory.getOWLDataProperty(IRI.create(IRI_URI + "#name"));
                        OWLDataPropertyAssertionAxiom axiom17 = factory.getOWLDataPropertyAssertionAxiom(dpDirectorName, directorIndividual, originalDirectorName);
                        AddAxiom addAxiom17 = new AddAxiom(ontology, axiom17);
                        ontologyManager.applyChange(addAxiom17);


                        OWLDataProperty dpActorId = factory.getOWLDataProperty(IRI.create(IRI_URI + "#id"));
                        OWLDataPropertyAssertionAxiom axiom18 = factory.getOWLDataPropertyAssertionAxiom(dpActorId, directorIndividual, directorId);
                        AddAxiom addAxiom18 = new AddAxiom(ontology, axiom18);
                        ontologyManager.applyChange(addAxiom18);

                    }
                }
            }

        }


        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);


    }

    public static OWLOntology load(OWLOntologyManager manager) throws OWLOntologyCreationException {
        return manager.loadOntologyFromOntologyDocument(new File("/Users/bahadirkirdan/IdeaProjects/movieontology.owl"));

    }


    public static void createNewOnto() throws OWLOntologyCreationException, OWLOntologyStorageException {

        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRI = IRI.create(new File("/Users/bahadirkirdan/IdeaProjects/movieontology2.owl"));
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


