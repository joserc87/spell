#!/usr/bin/env bash
# Generates the Java classes for the grammar
antlr4='java -jar /usr/local/lib/antlr-4.5.3-complete.jar'
$antlr4 Spell.g4 -o src/main/java/nl/thedocumentwizard/spell/parser -package nl.thedocumentwizard.spell.parser
