# SPELL Language Compiler

[![Build Status](https://travis-ci.org/joserc87/spell.svg?branch=master)](https://travis-ci.org/joserc87/spell)
[![Coverage Status](https://coveralls.io/repos/github/joserc87/spell/badge.svg?branch=travis)](https://coveralls.io/github/joserc87/spell?branch=travis)
[![Github All Releases](https://img.shields.io/github/downloads/joserc87/spell/total.svg)](https://github.com/joserc87/spell/releases)
[![GitHub Release](https://img.shields.io/github/release/joserc87/spell.svg)](https://github.com/joserc87/spell/releases/latest)

A compiler to generate wizard configuration XMLs from the much simpler SPELL
langauge, written in Java. It uses [antrl](http://www.antlr.org) to parse the
input file and JAXB to generate the output XMLs and to parse the documenttype
xml documents.

## Build:

To build the project you need gradle installed, as well as the JDK:

- To run the test, invoke:

  ```
   $ gradle test
  ```

- To compile the project and generate scripts to run the program:

  ```
   $ gradle installDist
  ```

  The binaries and scripst will be created in `./build/install/spell`.

- Finally, to generate a Zip or Tar package, run:

  ```
   $ gradle distZip
   or
   $ gradle distTar
  ```

  This will create a package in `./build/distributions/spell-VERSION.zip` or
  `./build/distributions/spell-VERSION.tar` where _VERSION_ depends on your
  current version version.

- It should be possible to build the project using _IntelliJ Idea_ too.

## Install:

To install the package, copy the binary folder (containing the script file and
the jar file) to somewhere in your drive and update the PATH variable to point
to the folder where the `spell` and `spell.bat` are.

You can test that the script are accessible by opening a terminal and typing
```
spell -h
```

## Learning by example:

Because sometimes a piece of code is more informative than a thorough
description, here is an example.

```python
step "This is the first step":
    # By default, questions are of type string
    "First question"

    # Controls can have a default value
    date "What is your birthdate?" = "7/7/87"

    # The default value can be a metadata too
    label "Metadata value:" = $myMetadata1

    # Although you usually write labels like this:
    label '' = 'Label text aligned to the left'

    # Controls will write the value into metadatas when the step is submitted
    email "Email saved in metadata" -> $myMetadata2

    # You have access to all usual attributes
    string(format='[a-zA-Z]*', minLength=5, maxLength=400) "Enter at least 5 letters"

    # Questions can be required, and controls can have an alias
    required number "What's the answer to life, the universe and everything else?" as answerToLife

    # Conditions and Advanced Rules are similar:
    when answerToLife == 42:
        # This is an advanced rule
        $filmMetadata = "You have a good taste in movies"
        # This is a condition, which will be triggered only if the advanced
        # rule is triggered
        goto correctAnswer

    # Questions and conditions can be mixed
    'Last question'

    # This is like an "else"
    when answerToLife != 42:
        $filmMetadata = "You should watch 'The Hitch Hiker's Guide to the Galaxy (1979)"
        goto incorrectAnswer

# Steps can have alias too.
# Thanks to the alias, it doesn't matter the order of the steps!
step "That was the correct answer", "Answer evaluation group" as correctAnswer:
    label "Congratulations!"
    label = $filmMetadata
    text(minLength=50, maxLength=400) "Suggest us a movie" -> $otherFilms
    checkbox 'Do you want a price?' as priceCheckbox

    when priceCheckbox is selected:
        $price = 'yes'

    goto RateStep # Default next step

step "Incorrect answer", "Answer evaluation group" as incorrectAnswer:
    label "Whoops! That was not the answer!"
    label "" = $filmMetadata
    goto RateStep

step "Rate this language" as RateStep:
    label '' = \
        '''
        We hope you liked the language.
        Please, let us know what you think about it in he following questions.
        Thank you!
        '''

    # Default value can be the value of the item to select
    list "How do you like this language?" = "like" -> $metadata as languageRate:
        # Display value = value, where the value is optional:
        "Meh"
        "I like it" = "like"
        "I love it!" = "love"
        "OMFGASDF#T&*^)!!!!" = "omg"
        $metadataDisplay = $metadataValue

    radio "Do you like radios?" = 'no' as radioRate:
        label "yes" as yes
        label "no" as no
        multi as kinda:
            label "yes, but..."
            string -> $comments


    # These complex Advanced Rules would take dozens of lines in an XML:
    when languageRate == "omg":                        # OMG -> 5stars
        $rate = "☆☆☆☆☆"
    when languageRate == "love" and radioRate == 'yes':  # love language + like radios -> 4 stars
        $rate = "☆☆☆☆"
    when (languageRate == "love" and radioRate != 'yes') \
        or (languageRate == "like" and radioRate == 'yes'): # love langauge but not the radio, or just like the language but likes the radios
        $rate = "☆☆☆"
    when languageRate == "like" and radioRate == kinda: # Kinda likes the radio
        $rate = "☆☆"
    when languageRate == "like" and radioRate == no:    # Don't really like it
        $rate = "☆"
```

## Why?

The Spell was created for the purpose of having a language more natural and
with more expresiveness than XML. The biggest advantages of using Spell instead
of writing a wizard configuration in XML are:

- **MetadataID inference** In spell you don't need to write a single metadata
  ID, only the metadata names. The metadata IDs will be calculated at compile
  time based on the `documenttypes.xml`. That not only means that you don't
  have to type the GUIDs, saving yourself from typos,but it also means that you
  can have multiple wizard configurations using different metadata sets, so
  long the metadata names are the same in both sets.
- **ID calculations** You don't need to write IDs anymore. When a spell file
  is compiled, it will assign the step IDs in order in which they are written
  in the file, starting from 1 and ending in the number of steps in the spell,
  so, if you add an intermediate step the IDs will be rearranged next time you
  compile the file. In a similar manner, the questions and controls IDs will be
  automatically assigned for you to `QUESTION_X` and `CONTROL_Y`.
- **Alias** Referencing controls and steps by their ID can be tricky at times.
  Spell allows you to set logical names on them so that you don't need to know
  their ID. This is specially useful for jumping to steps (e.g. `goto myStep`
  where my step would be the logical name of a step, with the ID to be
  determined) and to write conditions and advanced rules (e.g. you can write
  something like `when myStep.myControl == $myMetadata: goto myOtherStep`).
- **Programming-like language** In spell everything is shorter to write, but
  specially things that are not efficient to write in XML, like conditions.
  E.g. `when control1 == 'value' and step2.control1 > $metadata:` would take
  more than 10 lines of XML to write. Also, because we remove all the "xml
  noise", the file is much easier to read and maintain, being the language very
  close to plain english.

## Syntax:

The language is inspired in python, where the blocks are defined by their level
of indentation. Like in python, the suggested indentation is four spaces.

### General

#### Comments

Like in python, any caracter followed (and including) a _#_ will be considered
a commend and will be discarded:

```python
# This is a comment

step 'asfd':  # This is an embedded comment

# This is
# a multi line
# comment
```

Although comments were certainly possible in
XML, it is much easier to create comments in Spell, since only one extra
character is needed to create a comment. It is encouraged that you write
comments to explain the most complex parts of the file.

#### Metadatas

Metdatas are referenced by their metadata name and represented with the dolar
sign: `$METADATA_NAME`. For example, `list -> $myMetadata` would a control that
will save its value into the metadata with name `myMetadata`. The metadata will
be looked in the _documenttypes.xml_ file during the final stage of the
compilation to get the ID, so they will have to be referenced with the same
exact name.

#### Alias

As said before, steps and control can an alias, set with `as ALIAS_NAME`
where the `ALIAS\_NAME` should start with a letter and should contain any
combination of letters, numbers and underscores, but should not be one of the
reserved words like control types (_string_, _number_, etc) or other words like
_as_. If you need to create an alias with a reserved word, you can make use of
the _at_ (_@_) sign. For example:

```python
step 's1' as step1     # ok
number 'q1' as number  # WRONG!
step 's2' as @step     # ok
number 'q2' as @number # ok
```

Later on, you can use those alias to link steps, while using the `goto`
statement, and controls to test the value of a certain control:

```python
step "Step 1" as previousStep:
"some question" as stringControl

step "Step 2":
...
when previousStep.stringControl == $metadata:
    goto nextStep

step "Step 3" as nextStep:
...
```

#### Values

The values can be:

- Strings, written with single quotes (`'a string'`), double quotes (`"a
  string"`) or triple quotes (`'''a string''' or """a string"""`)
- Numbers
- Booleans: [yes, true, selected] for True values and [no, false, unselected]
  for False values (they are equivalent).

### Steps

A spell file is a set of steps. Each step should start in a new line and not
have any indentation:

```python
step 'STEP NAME'[, 'STEP GROUP NAME'] [as STEP_ALIAS]:
    # CONTENT HERE
    # CONTENT HERE
    # CONTENT HERE
```

The content of the step (controls, conditions and advanced rules) must be
indented. Example:

```python
step 'First step', 'Group' as firstStep:
    'question'
step 'Second step', 'Group':
    'question'
step 'Final step' as finalStep:
    'question'
```

By default, the next step will be the step inmediately after it. In the
previous example, _Second step_ will be the next step of _First step_ and
_Final step_ will be the next step of _Second step_. To define a different
_next step_ you have to make use of the `goto` statement:

```python
step 'First step', 'Group' as firstStep:
    'question'
    goto finalStep
step 'Second step', 'Group':  # This step will never be visited
    'question'
step 'Final step' as finalStep:
    'question'
```

### Questions / Controls

Steps ~~can~~ should have controls. Like in the wizard configuration XML
format, controls can reside inside other controls (like multis or radios). When
the control is directly inside a step, it represents a question, and can have
the same attributes as a question, i.e. question name and whether the question
is required. Sub controls won't have those attributes. Also, the root control
will have one level of indentation and subcontrols will be indented
respectively:

```python
    # For most simple controls (label, string, email etc
    [required ][CONTROL_TYPE ][(ATTRIBUTES)]'QUESTION_NAME'[ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]
    # For container controls (radio and multi)
    [required ][CONTROL_TYPE ][(ATTRIBUTES)]'QUESTION_NAME'[ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]:
        [CONTROL_TYPE][(ATTRIBUTES)][ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]
        [CONTROL_TYPE][ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]:
            [CONTROL_TYPE][ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]:
                # ...
    # For list controls:
    [required ]CONTROL_TYPE ][(ATTRIBUTES)]'QUESTION_NAME'[ = DEFAULT_VALUE][ -> METADATA][ as ALIAS]:
        ITEM_DISPLAY_TEXT[ = ITEM_VALUE]
        ITEM_DISPLAY_TEXT[ = ITEM_VALUE]
        ITEM_DISPLAY_TEXT[ = ITEM_VALUE]
        # ...
```

Where:

1. **required** is optional and only applicable to questions, i.e. controls
   directly inside the step, not to subcontrols. When the `required` tag is not
   present, the question will not be required.
2. **CONTROL\_TYPE** is optional. When it's not specified, the `string` control
   is assumed. The list of control types are:
   - label
   - string
   - email
   - text
   - date
   - number
   - checkbox
   - list
   - radio
   - attachment
   - image
   - multi

3. **ATTRIBUTES** is a list of attributes separated by commas:

   ```python
   ATTRIBUTE=VALUE[, ATTRIBUTE=VALUE[, ATTRIBUTE=VALUE]]
   ```
   It is meant for thosefor those attributes that are not embedded and
   expressed inside the language. The available attributes depend also in the
   type of the control. For example:

   ```python
   number(allowDecimals=true, format='XXX')
   checkbox(@label="checkbox' label")
   ```

   Note how we had to preppend `@label` with an _@_ sign, since _label_ is a
   reserved word.

4. **DEFAULT\_VALUE** can be either a const value (string, number or boolean,
   depending on the control type) or a metadata. When it's a const value, the
   value should have the same type as the control:

   - `String`, `Email`, `Text` and `Date` controls accept a string (e.g. `"a
     value"`) as the default value;
   - `Number` controls accept integers or floats (`1` or `1.0`).
   - `Checkbox` controls accept only `true`/`selected` or `false`/`unselected`
     (they are equivalent)
   - `List` controls accept string value as the default value. That default
     value should be the value of one of the items.
   - `Radio` controls accept an alias as the default value (the alias of one of
     the controls directly under the `Radio`, that is).
5. **METADATA** has to be a valid metadata name (starting with _$_ as described
   earlier). It is certainly possible (and common) to use the metadata as a
   default value too.
6. **ITEM\_DISPLAY\_TEXT** and **ITEM\_VALUE** can be either a string or a
   metadata. When the **ITEM\_VALUE* is not present, the **ITEM\_DISPLAY\_TEXT**
   will also be the value. For exampe:

   ```python
   list 'myList':
       'item1' = 1
       'item2' = $metadata
       'item'
       $metadata
       $metadata1 = $metadata2
   ```

### Advanved Rules and Conditions

In Spell, _advanced rules_ and _conditions_ are a very similar thing and
usually written together inside a `when`, i.e., a conditional statement
containing a boolean expresion which will be evaluated to see whether the
_advanced rule_ or _condition_ should be triggered. Inside that conditional
`when`, an advanced rule would be written as a metadata assignment and a
condition would be written as a jump with a `goto` statement:

```python
when CONDITION:
    # For a conditional jump:
    goto STEP_ALIAS  # This is a conditional jump

    # Or for an advanced rule:
    $METADATA = VALUE
    $METADATA = VALUE

    # Nested 'when'
    when CONDITION:
        # ...
```

This _when_ statement can be written anywhere inside the step, even between
questions. Inside a _when_ you can place as many metadata assignments and jumps
as you want, being possible to nest _when_ statements inside other _when_
statements, for simplicity.

The conditional part of the _when_ will be a test over control and metadata
values. The control values will be referenced by the step alias (if the control
is in a different step), plus the control alias. The values to compare control
values to can be string, numbers and booleans, while the metadata values should
be compared against strings:

```python
when (step1.stringControl == 'value' or step1.checkboxControl is selected) \
    and not (numberControl = 1 or $metadata1 == 'val'):
    # ...
```

### Others

The spell compiler does not support any kind of preprocessing at this stage,
but things like including files, variable data and loops can be achieved using
templating languages like [Jinja](http://jinja.pocoo.org/docs/2.9/).

### TODO:

- [ ] Implement XPath input data in the list control.
- [ ] Validate the type of the default values depending on the type of the
  control.
- [ ] Add a configuration statement to set the wizard name.
- [ ] Add default attributes for controls in that configuration statement. E.g.:

  ```python
  number(allowDecimals=true, decimalPrecision=3, step=0.2, decimalSeparator=".")
  date(format="MMMM dd, yyyy")
  ```

  More advanced macros can be achieved with the preprocessor (templating
  language):

  ```python
  # Define custom controls with macros:
  {% macro int() -%}
      number(allowDecimals=false, step=5)
  {%- endmacro %}
  {% macro float(precision=2) -%}
      number(allowDecimals=true, step=0.1, decimalPrecision={{ precision }})
  {%- endmacro %}

  # Later in the spell, we use those types:

  {{ int() }} 'my integer control'
  {{ float(7) }} 'my float control'
  ```
- [ ] Make the question name optional.
- [x] **BUG** The _label_ attribute in checkbox is interpreted by the lexer as
  the _label_ control, so it creates a checkbox and a label.
- [ ] Simplify the syntax. Types should have one common rule and the content
  should be checked semantically instead of sintactically. Also, maybe the
  control types should not be tokens? That would help defining new types.
- [ ] Add a '<-> $metadata' token, which means '= $metadata -> $metadata'
