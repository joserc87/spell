#!/usr/bin/env bash
# Generates the Java classes for the grammar
antlr4='java -jar /usr/local/lib/antlr-4.5.3-complete.jar'
cd src/main/antlr
pwd
rm -f ../java/nl/thedocumentwizard/spell/parser/antlr/*
$antlr4 Spell.g4 -o ../java/nl/thedocumentwizard/spell/parser/antlr #-package nl.thedocumentwizard.spell.antlr
cd ../../..
