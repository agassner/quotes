#!/usr/bin/env python

from bs4 import BeautifulSoup
from datetime import datetime

import sys
import httplib2
import time as tm
import re
import os

data_dir = "ftse_data"

def get_httplib2(url):
    h = httplib2.Http()
    response, content = h.request(url, "GET")

    return content if response.status != 404 else ""

def get_symbols(url):
    content = get_httplib2(url)
    soup = BeautifulSoup(content)

    table = soup.find("table", "yfnc_tableout1").find("table")
    symbols = table.find_all(text=re.compile("\.L"))

    return symbols

def format_date(date):
    return datetime.strptime(date, "%Y-%m-%d").strftime("%Y%m%d")

def transform_to_metastock(symbol, content):
    # Date,Open,High,Low,Close,Volume,Adj Close
    # 2012-08-17,583.00,592.00,581.00,582.50,2342000,582.50

    # symbol,date,open,high,low,close,volume,trades
    # ARM.L,20060706,112.5,112.5,108.75,109,20852214,0

    lines = [ tuple(line.split(",")) for line in content.split("\n") ][1:-1]

    return [ "%s,%s,%s,%s,%s,%s,%s,0" % (symbol, format_date(line[0]), line[1], line[2], line[3], line[4], line[5]) for line in lines ]

def get_quotes(symbol):
    t = tm.time()

    content = get_httplib2("http://ichart.yahoo.com/table.csv?s=%s" % symbol)

    if content:
        file = open("%s/%s.txt" % (data_dir, symbol), "w")
        [file.write("%s\n" % line) for line in transform_to_metastock(symbol, content)]
        file.close()

    print "Got %s in %.3f" % (symbol, tm.time()-t)

def get_constituents():
    
    t = tm.time()

    ftse_url = "http://finance.yahoo.com/q/cp?s=%5EFTSE"

    # TODO: To be improved
    symbols = \
        get_symbols(ftse_url) + \
        get_symbols("%s&c=1" % ftse_url) + \
        get_symbols("%s&c=2" % ftse_url)
    symbols.sort()
    
    if not os.path.exists(data_dir):
        os.makedirs(data_dir)
    file = open("%s/ftse_constituents.txt" % data_dir, "w")
    [file.write("%s\n" % symbol) for symbol in symbols]
    file.close()

    [get_quotes(symbol) for symbol in symbols]

    print "Found %s symbols" % len(symbols)
    print "Took %.3fs" % (tm.time()-t)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        for symbol in sys.argv[1:]:
            get_quotes(symbol)
    else:
        get_constituents()
