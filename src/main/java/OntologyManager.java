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

    OWLOntologyManager ontologyManager;
    OWLOntology ontology;
    IRI ontologyIRI;

    public OntologyManager() throws OWLOntologyCreationException {

        ontologyManager = OWLManager.createOWLOntologyManager();
        ontologyIRI = IRI.create( new File(ONTOLOGY_PATH_SAVE) );
        ontology = loadOntology(ontologyManager);

    }

    public OWLOntology loadOntology(OWLOntologyManager ontologyManager) throws OWLOntologyCreationException {

        File file = new File(ONTOLOGY_PATH_LOAD);
        return ontologyManager.loadOntologyFromOntologyDocument( file );

    }

    public void addIndividualToOntology(String individual) throws OWLOntologyStorageException {

        OWLDataFactory factory = ontologyManager.getOWLDataFactory();

        OWLClass movieClass = factory.getOWLClass(IRI.create("http://www.semanticweb.org/burakatalay/ontologies/2016/3/movieontology.owl" + "#Movie"));
        OWLIndividual movieIndividual = factory.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/burakatalay/ontologies/2016/3/movieontology.owl" + individual));

        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(movieClass, movieIndividual);

        ontologyManager.addAxiom(ontology, axiom);

        saveOntology();

/*
        OWLObjectProperty hasWife = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasWife"));

        OWLObjectPropertyAssertionAxiom axiom1 = factory.getOWLObjectPropertyAssertionAxiom(hasWife, john, mary);

        AddAxiom addAxiom1 = new AddAxiom(ontology, axiom1);

        ontologyManager.applyChange(addAxiom1);
*/
        /*
        for(String individual : individuals){
            OWLIndividual john = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + individual));
        }*/

    }

    public void saveOntology() throws OWLOntologyStorageException {
        ontologyManager.saveOntology(ontology, new OWLXMLDocumentFormat(), ontologyIRI);
    }
}
