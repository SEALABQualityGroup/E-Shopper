#!/usr/bin/env python
# coding: utf-8

from ruamel.yaml import RoundTripDumper, RoundTripLoader
import numpy as np
import random
import ruamel.yaml
import sys

num_patterns =int(sys.argv[1])
num_req_classes = 10
sync_rpcs = [('categories-server', 'getCategory'),
             ('items-server', 'findItemsRandomByIdProduct'),
             ('products-server', 'findProduct'),
             ('products-server', 'findProductRandom'),
             ('web-server', 'home')]

async_rpcs = [('items-server', 'findItemRandom'),
              ('items-server', 'findFeaturesItemRandom')]

num_sub_ops = len(sync_rpcs)

def createPattern(num_sub_ops, k):
    bag = set()
    for i in  random.sample(range(num_sub_ops), k=k):
        bag.add(i)
    return [50 if i in bag else 0 for i in range(num_sub_ops)]



patterns = []
sizes = random.sample([1, 2, 3], k=2)
for k in sizes:
    pattern = createPattern(num_sub_ops, k)
    str_pat = ''.join([str(x) for x in pattern])
    patterns.append(pattern)

zeros = np.zeros((num_req_classes - num_patterns ,num_sub_ops), dtype=np.int)


latencyInjections = np.concatenate((patterns, zeros), axis=0).transpose()

for li, so in zip(latencyInjections,  sync_rpcs):
    cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
    method=so[1]
    doc = open(cfgFile ,mode='r')
    ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
    doc.close()
    ymlDict['experiment'][method] = ','.join(str(x) for x in li)
    ymlDict['noise'][method] = ','.join(['0' for _ in range(num_req_classes)])
    doc = open(cfgFile ,mode='w')
    ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)

for so in async_rpcs:
    cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
    method=so[1]
    doc = open(cfgFile ,mode='r')
    ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
    doc.close()
    ymlDict['noise'][method] = ','.join(['0' for _ in range(num_req_classes)])
    doc = open(cfgFile ,mode='w')
    ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)