import itertools
import nltk
from nltk.corpus import wordnet
from nltk.corpus import stopwords
from nltk.stem.porter import *


def run(queryList):

    stemmer = PorterStemmer()
    f = open("data/expanded.txt", "w+")
    for query in queryList:
        querySplitted = query.split(",")

        # tokenizing the query
        tokens = nltk.word_tokenize(querySplitted[1])

        # stemming the tokens in the query
        stemmed_tokens = [stemmer.stem(i) for i in tokens]

        # removing stop words in the query
        filtered_words = [word for word in stemmed_tokens if word not in stopwords.words('english')]

        # pos tagging of tokens
        pos = nltk.pos_tag(filtered_words)

        synonyms = []  # synonyms of all the tokens

        index = 0
        # iterating through the tokens
        for item in filtered_words:
            synsets = wordnet.synsets(item)

            # synonyms of the current token
            currentSynonyms = []
            currentPOS = get_wordnet_pos(pos[index])

            # iterating through the synsets
            for i in synsets:
                # first we check if token and synset have the same part of speech
                if str(i.pos()) == str(currentPOS):
                    for j in i.lemmas():
                        if j.name() not in currentSynonyms:  # if we have not
                            currentSynonyms.append(j.name().replace("_", " "))
                synonyms.append(currentSynonyms)
            index += 1

        f.write(querySplitted[0] + ", " + querySplitted[1] + ", ")

        # removing duplicate lists in the synonyms list
        tmp = []
        for elem in synonyms:
            if elem and elem not in tmp:
                tmp.append(elem)
        synonyms = tmp

        # now that we have all the synonyms
        for x in itertools.product(*synonyms):
            current = ""
            for item in x:
                current += item
                current += " "
            current += ", "
            f.write(current)
        f.write("\n")


def get_wordnet_pos(treebank_tag):

    if treebank_tag[1].startswith('J'):
        return wordnet.ADJ
    elif treebank_tag[1].startswith('V'):
        return wordnet.VERB
    elif treebank_tag[1].startswith('N'):
        return wordnet.NOUN
    elif treebank_tag[1].startswith('R'):
        return wordnet.ADV
    else:
        return ''