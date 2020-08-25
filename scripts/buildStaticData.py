import sys
import json
import requests
import urllib.parse
import urllib.request

if(len(sys.argv) < 2) :
    print('buildStaticData: ')
    print('     usage: python3 buildStaticData.py outputDir')
    exit()
 
outputDir = sys.argv[1]

url = 'http://localhost:8082/c19/data/summary'
req = urllib.request.Request(url)
with urllib.request.urlopen(req) as response:
    summary = json.loads(response.read().decode('UTF-8'))

with open(outputDir + '/summary.json', 'w+') as f:
  json.dump(summary, f, ensure_ascii=False)

url = 'http://localhost:8082/c19/data/summary/regions'
req = urllib.request.Request(url)
with urllib.request.urlopen(req) as response:
    regions = json.loads(response.read().decode('UTF-8'))
with open(outputDir + '/summary/regions.json', 'w+') as f:
  json.dump(regions, f, ensure_ascii=False)

for region in regions:
    for attribute, value in region.items():
        if(attribute == 'name'):
            url = 'http://localhost:8082/c19/data/points/' + urllib.parse.quote_plus(value)
            req = urllib.request.Request(url)
            with urllib.request.urlopen(req) as response:
                regionDatapoints = json.loads(response.read().decode('UTF-8'))
            with open(outputDir + '/points/'+value+'.json', 'w+') as f:
                json.dump(regionDatapoints, f, ensure_ascii=False)
    