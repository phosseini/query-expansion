import nltk
from nltk.corpus import wordnet

# the query that we want to expand
query = "program committee"

# tokenizing the query
tokens = nltk.word_tokenize(query)

allSynonyms = []  # synonyms of all the tokens

# iterating through the tokens
for item in tokens:
    synsets = wordnet.synsets(item)

    # synonyms of the current token
    currentSynonyms = []

    # iterating through the synsets
    for i in synsets:
        if i.lemmas()[0].name() not in currentSynonyms: # if we have not
            print(i.lemmas()[0].name().replace("_", " "))
            currentSynonyms.append(i.lemmas()[0].name().replace("_", " "))
    allSynonyms.append(currentSynonyms)

print(allSynonyms)