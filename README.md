# query-expansion

This repository is dedicated to the final project of the DeepQA course at GWU. 

# Abstract
In this project, we outline the whole process of developing a query expansion platform using different similarity measures. Query expansion is the process of reformulating a seed query to improve retrieval performance in information retrieval operations. In our platform, we used different techniques based on a synonym-based method for expanding topics and queries. In addition, we used five similarity measures to choose the best expanded query which is more relevant to the original query. We also used IBM Bluemix retrieve and rank service to evaluate our method. Results show that our method significantly improves the recall meaning that it can detect and retrieve more relevant documents from the collection of documents which are related to a specific query or topic.

# Techniques
The main method that we use for query/topic expansion is based on finding synonyms of tokens in the query using [WordNet](https://wordnet.princeton.edu/).
