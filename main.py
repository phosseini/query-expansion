import expansion
import xlrd


queryList = []
# we want to keep the list of unique queries
uniqueQuery = []

workbook = xlrd.open_workbook('data/m.xls')
sheet_names = workbook.sheet_names()
xl_sheet = workbook.sheet_by_name(sheet_names[0])

for row_idx in range(0, xl_sheet.nrows):    # Iterate through rows
        cell_id = xl_sheet.cell(row_idx, 0).value  # Get id cell
        cell_topic = xl_sheet.cell(row_idx, 2).value  # Get topic cell
        if str(cell_topic) not in uniqueQuery:
            queryList.append(str(cell_id) + "," + str(cell_topic))
            uniqueQuery.append(str(cell_topic))

# the list of queries that we want to expand
expansion.run(queryList)
