# SPELL Language Compiller

A compiler to generate wizard configuration XMLs from the much simpler SPELL
langauge, written in Java. It uses [antrl](http://www.antlr.org) to parse the
input file and JAXB to generate the output XMLs and to parse the documenttype
xml documents.

## Build:

To build the project just run the gradle project:

In a Unix like system (Mac OS X or linux):

```
 $ ./gradlew jar
```

In windows:
```
 > gradlew.bat
```

Or open the IntelliJ Idea project and build it from there.

## Syntax:

### Example

```python
step "This is the first step":
    # By default, questions are of type string
    "First question"

    # Controls can have a default value
    date "When is your birthdate?" = "7/7/87"

    # The default value can be a metadata too
    label "Metadata value:" = $myMetadata1

    # Controls will write the value into metadatas when the step is submitted
    email "Email saved in metadata" -> $myMetadata2

    # You have access to all usual attributes
    string "Enter at least 5 letters", format="[a-zA-Z]*", minLength=5, maxLength=400

    # Questions can be required, and controls can have an alias
    required number "What's the answer to life, the universe and everything else?" as answerToLife

    # Conditions and Advanced Rules are similar:
    when answerToLife == 42:
        # This is an advanced rule
        $filmMetadata = "You have a good taste in movies"
        # This is a condition, which will be triggered only if the advanced
        # rule is triggered
        goto correctAnswer

    # This is like an "else"
    when answerToLife != 42:
        $filmMetadata = "You should watch 'The Hitch Hiker's Guide to the Galaxy (1979)"
        goto incorrectAnswer

# Steps can have alias too.
# Thanks to the alias, it doesn't matter the order of the steps!
step "That was the correct answer", "Answer evaluation group" as correctAnswer:
    label "Congratulations!"
    label = $filmMetadata
    text "Suggest us a movie", minLength=50, maxLength=400 -> $otherFilms
    goto RateStep

step "Incorrect answer", "Answer evaluation group" as incorrectAnswer:
    label "Whoops! That was not the answer!"
    label "" = $filmMetadata
    goto RateStep

# The wizards can be modularized, importing steps from other files
import myStep

step "Rate this language" as RateStep:
    # You can import also files to compose steps with reusable controls
    import myReusableControls

    # Default value can be the actual value (int/string) or an alias
    list "How do you like this language?" = "like" -> $metadata as languageRate:
        item "I like it" = "like"
        item "I love it!" = "love"
        item "OMFGASDF#T&*^)!!!!" = "omg"
        item $metadataDisplay = $metadataValue

    radio "Do you like radios?" = no as radioRate:
        label "yes" as yes
        label "no" as no
        multi as kinda:
            label "yes, but..."
            string -> $comments

    
    # These complex Advanced Rules would take dozens of lines in an XML:
    when languageRate == "omg":                        # OMG -> 5stars
        $rate = "☆☆☆☆☆"
    when languageRate == "love" and radioRate == yes:  # love language + like radios -> 4 stars
        $rate = "☆☆☆☆"
    when (languageRate == "love" and radioRate != yes) # love langauge but not the radio, or just like the language but likes the radios\
        or (languageRate == "like" and radioRate == yes):
        $rate = "☆☆☆"
    when languageRate == "like" and radioRate == kinda: # Kinda likes the radio
        $rate = "☆☆"
    when languageRate == "like" and radioRate == no:    # Don't really like it
        $rate = "☆"
```

### Notes:

- The group name of the step is optional.
- The default value should have the same type as the control:
  - `String`, `Email`, `Text` and `Date` controls accept a string (e.g. `"a
    value"`) as the default value;
  - `Number` controls accept integers or floats (`1` or `1.0`).
  - `Checkbox` controls accept only `true`/`selected` or `false`/`unselected`
    (they are equivalent)
  - `List` controls accept string value as the default value. That default
    value should be the value of one of the items.
  - `Radio` controls accept an alias as the default value (the alias of one of
    the controls directly under the `Radio`, that is).
- Metadatas are used by name, and accessed using the dollar character `$`, e.g.
  `$myMetadata` will be translated into the metadata with name _myMetadata_.
  Metadatas are looked up in the _documentTypes.xml_ file, so they will have to
  be referenced with the same exact name.
- Steps and controls can have an alias to be referenced. For example, you can
  use the alias of a step to `goto` that step (condition), or to access the
  value of a control in a previous step: 

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
- The next step will be the one writen inmediately after, by default. To go to
  another step, use the `goto` (without a condition).
- A file imported outside any step shoud contain one or more steps. A file
  imported within a step (i.e. indented inside the step definition), should
  contain only controls, advanced rules or conditions, and should not contain
  other steps.
