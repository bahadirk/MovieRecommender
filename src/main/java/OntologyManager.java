import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bahadirkirdan on 4/27/16.
 */
public class OntologyManager {

    private final String ONTOLOGY_PATH_LOAD = "/Users/bahadirkirdan/IdeaProjects/movieontology.owl";
    private final String ONTOLOGY_PATH_SAVE = "/Users/bahadirkirdan/IdeaProjects/movieontology2.owl";
    private final String IRI_URI = "http://www.semanticweb.org/bahadirkirdan/ontologies/2016/4/untitled-ontology-44";

    OWLOntologyManager ontologyManager;
    OWLOntology ontology;
    IRI ontologyIRI;

    public OntologyManager() throws OWLOntologyCreationException {

        ontologyManager = OWLManager.createOWLOntologyManager ();

        ontologyIRI = IRI.create( new File(ONTOLOGY_PATH_LOAD) );
        ontology = loadOntology(ontologyManager);

    }

    public OWLOntology loadOntology(OWLOntologyManager ontologyManager) throws OWLOntologyCreationException {

        File file = new File(ONTOLOGY_PATH_LOAD);
        return ontologyManager.loadOntologyFromOntologyDocument( file );

    }

    public void addIndividualToOntology(String individual) throws OWLOntologyStorageException {

        OWLDataFactory factory = ontologyManager.getOWLDataFactory();
/*
        OWLClass movieClass = factory.getOWLClass(IRI.create(ontologyIRI + "#Movie"));
        OWLIndividual movieIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + individual));

        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(movieClass, movieIndividual);

        ontologyManager.addAxiom(ontology, axiom);

        saveOntology();
*/

        OWLClass movieClass = factory.getOWLClass(IRI.create(IRI_URI + "#Movie"));

        OWLIndividual movieIndividual = factory.getOWLNamedIndividual(IRI.create(IRI_URI + "#" + individual));

        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(movieClass, movieIndividual);

        AddAxiom addAxiom1 = new AddAxiom(ontology, axiom);

        ontologyManager.applyChange(addAxiom1);

        saveOntology();

        /*
        for(String individual : individuals){
            OWLIndividual john = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + individual));
        }*/

    }

    public void saveOntology() throws OWLOntologyStorageException {
        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);
    }
}
