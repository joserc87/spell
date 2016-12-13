#!/usr/bin/env bash
# Generates the Java classes from the XSDs
xjc src/main/resources/WizardConfiguration.xsd -d src/main/java -p nl.thedocumentwizard.wizardconfiguration.jaxb
xjc src/main/resources/documenttype.1.0.xsd -d src/main/java #-p nl.thedocumentwizard.wizardconfiguration.jaxb
