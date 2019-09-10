#!/usr/bin/env python
# coding: utf-8

from ruamel.yaml import RoundTripDumper, RoundTripLoader
import numpy as np
import random
import ruamel.yaml
import sys

num_patterns =int(sys.argv[1])
num_req_classes = 10
subops = [('categories-server', 'getCategory'),
          ('items-server', 'findItemsRandomByIdProduct'),
          ('items-server', 'findItemRandom'),
          ('items-server', 'findFeaturesItemRandom'),
          ('products-server', 'findProduct'),
          ('products-server', 'findProductRandom'),
          ('web-server', 'home')]
num_sub_ops = len(subops)

def createPattern(num_sub_ops):
    bag = set()
    k = random.choice(range(1,5))
    while len(bag) < k:
        bag.add(random.choice(range(num_sub_ops)))

    return [random.choice([50, 100,150])
            if i in bag
            else 0
            for i in range(num_sub_ops)]


patterns = np.array([createPattern(num_sub_ops)
                     for _ in range(num_patterns)])


zeros = np.zeros((num_req_classes - num_patterns ,num_sub_ops), dtype=np.int)


latencyInjections = np.concatenate((patterns, zeros), axis=0).transpose()


for li, so in zip(latencyInjections, subops):
    cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
    method=so[1]
    doc = open(cfgFile ,mode='r')
    ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
    doc.close()
    ymlDict['experiment'][method] = ','.join(str(x) for x in li)
    doc = open(cfgFile ,mode='w')
    ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)
