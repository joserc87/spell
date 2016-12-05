package nl.thedocumentwizard.spell;

import nl.thedocumentwizard.wizardconfiguration.MyObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfSteptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * Created by jose on 04/12/2016.
 */
public class Main {
    public static void main(String args[]) {
        ObjectFactory factory = new MyObjectFactory();
        WizardConfiguration wizard = (WizardConfiguration) factory.createWizard();
        wizard.setName("My wizard");
        wizard.setSteps(factory.createArrayOfSteptype());

        wizard.getSteps().getStep().add(factory.createSteptype());
        wizard.getSteps().getStep().add(factory.createSteptype());
        System.out.println("Marshalling Wizard");
        wizard.marshall(new File("output.xml"));
        System.out.println("Finished");
    }
}
