import itertools
import nltk
import expansion
from nltk.corpus import wordnet


def run(query):
    # tokenizing the query
    tokens = nltk.word_tokenize(query)

    synonyms = []  # synonyms of all the tokens

    # iterating through the tokens
    for item in tokens:
        synsets = wordnet.synsets(item)

        # synonyms of the current token
        currentSynonyms = []

        # iterating through the synsets
        for i in synsets:
            if i.lemmas()[0].name() not in currentSynonyms:  # if we have not
                # print(i.lemmas()[0].name().replace("_", " "))
                currentSynonyms.append(i.lemmas()[0].name().replace("_", " "))

        synonyms.append(currentSynonyms)

    f = open("data/expanded.txt", "w+")
    f.write(query)
    f.write(", ")
    # now that we have all the synonyms
    for x in itertools.product(*synonyms):
        current = ""
        for item in x:
            current += item
            current += " "
        current += ","
        f.write(current)

