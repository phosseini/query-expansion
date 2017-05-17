import sys
import numpy as np
import re

import json
from watson_developer_cloud import RetrieveAndRankV1

retrieve_and_rank = RetrieveAndRankV1(
    username='47af141b-71df-48dd-8ffa-90d57343d944',
    password='fFpOoEmQDT1p')

solr_clusters = retrieve_and_rank.list_solr_clusters()
print(json.dumps(solr_clusters, indent=2))

solr_cluster_id = 'sc8cceb75a_943a_4fdf_8a9e_bfaa1ddc9f70'

status = retrieve_and_rank.get_solr_cluster_status(solr_cluster_id=solr_cluster_id)
print(json.dumps(status, indent=2))

configs = retrieve_and_rank.list_configs(solr_cluster_id=solr_cluster_id)
print(json.dumps(configs, indent=2))


from xlrd import open_workbook
wb = open_workbook('/Users/vincent/Downloads/Mapping.xls')
for s in wb.sheets():
    values = []
    for row in range(s.nrows):
        col_value = []
        for col in range(s.ncols):
            value  = (s.cell(row,col).value)
            try : value = str(int(value))
            except : pass
            col_value.append(value)
        values.append(col_value)

temp = ""
relevance = {}
key = 0
for n in range(1, len(values)):
    x = values[n][1]
    if temp != x:
        key += 1
        temp = values[n][1]
        relevance.setdefault(key,{"topic":values[n][1],"docs":[]})
    relevance[key].get("docs").append(values[n][4])
print(relevance)
pysolr_client = retrieve_and_rank.get_pysolr_client(solr_cluster_id,"default2_collection")
# Can also refer to config by name


wb2 = open_workbook('/Users/vincent/Downloads/top_queries.xls')
for s in wb2.sheets():
    values = []
    for row in range(s.nrows):
        col_value = []
        for col in range(s.ncols):
            value  = (s.cell(row,col).value)
            try : value = str(int(value))
            except : pass
            col_value.append(value)
        values.append(col_value)

queries = []
key = 0
for n in range(1, len(values)):
    x = values[n][1]
    key += 1
    queries.append([values[n][0],values[n][1],values[n][2],values[n][3],values[n][4],values[n][5]])

res = {}
for i in range(len(queries)):
    res.setdefault(i+1, [])
    for j in range(len(queries[i])):
        results = pysolr_client.search(queries[i][j],**{
                    "ranker_id":'81aacex30-rank-5457',
                    "fl": "id,ranker.confidence"
                },search_handler="/fcselect")
        for doc in results.docs:
            if doc.get("ranker.confidence") >= 0.3:
                if doc.get("id") not in res[i+1]:
                    res[i+1].append(doc.get("id"))

scores = {}
for i in res.keys():
    TP = 0.0
    FP = 0.0
    for id in res[i]:
        if id in relevance[i].get("docs"):
            TP += 1.0
        else:
            FP += 1.0
    FN = len(relevance[i].get("docs"))-TP
    P = TP/(TP+FP)
    R = TP/(TP+FN)
    F1 = 1/(1/P+1/R)
    scores[i] = [P,R,F1]
print(scores)
overall_scores = [sum(score)/len(score) for score in list(zip(*scores.values()))]
print(overall_scores)


