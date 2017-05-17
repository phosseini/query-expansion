import sys
import numpy as np
import re

from xlrd import open_workbook
wb = open_workbook('/Mapping.xls')
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
print (values)


f2 = open('/Data.json', 'a')
f2.write('{ \n')

n=1
for i in range(len(values)):
    words = values[n][3].split()
    x = ""
    for word in words:
        if word == '&':
            word = 'and'
        if word == 'Mיrida':
            word = "Me'rida"
        if word == 'Evil?':
            word = "Evil"
        x = x + word + '_'
    x=x[:-1]
    file_name = '/Datasets/CE-ACL-14/wiki12_articles/'+x
    print(file_name)
    f = open(file_name, 'r')
    if i >= 1:
        f2.write(',\n\n\n\n')
    f2.write('"doc" : { \n')
    f2.write('"id" : "' + values[n][0] +'",\n')
    f2.write('"body" : " ')
    f2.write(re.sub('[^A-Za-z0-9 .,]+', '', f.read(-1)))
    #f2.write(f.read(-1))
    f2.write('"\n')
    f2.write('}')
    n = n + 1
    if n == 327:
        break

f2.write('\n}')

f.close()
f2.close()
