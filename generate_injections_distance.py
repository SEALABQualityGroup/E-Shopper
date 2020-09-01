from ruamel.yaml import RoundTripDumper, RoundTripLoader
import numpy as np
import random
import ruamel.yaml
import sys
from math import ceil

REQ_MEAN = 393
NOISE_INTESITY= 0.3
DISTANCE = float(sys.argv[1])

num_patterns =2
num_req_classes = 10
sync_rpcs = [('categories-server', 'getCategory'),
             ('items-server', 'findItemsRandomByIdProduct'),
             ('products-server', 'findProduct'),
             ('products-server', 'findProductRandom'),
             ('items-server', 'findFeaturesItemRandom')
             #('web-server', 'home')
             ]
sync_rpcs_num = [1, 19, 1, 1, 1]

async_rpcs = [('items-server', 'findItemRandom')]

num_sub_ops = len(sync_rpcs)

def createPattern(num_sub_ops, k, factor):
    bag = set()

    delay = (REQ_MEAN*factor)/k
    for i in  random.sample(range(num_sub_ops), k=k):
        bag.add(i)
    return [int(ceil(delay/sync_rpcs_num[i])) if i in bag
            else 0 for i in range(num_sub_ops)]

def createSyncNoise(pattern):
    noise = np.zeros(len(pattern), dtype=np.int)
    index = random.choice([i for i, p in enumerate(pattern) if p])
    noise[index] = 0
    return noise

patterns = []
sizes = random.choices([1, 2, 3], k=2)

pattern1 = createPattern(num_sub_ops, random.choice([1, 2, 3]), 0.3 - DISTANCE/2)
pattern2 = createPattern(num_sub_ops, random.choice([1, 2, 3]), 0.3 + DISTANCE/2)

patterns.append(pattern1)
patterns.append(pattern2)

syncNoise = np.array([createSyncNoise(p) for p in patterns])

zeros = np.zeros((num_req_classes - num_patterns ,num_sub_ops), dtype=np.int)


latencyInjections = np.concatenate((patterns, zeros), axis=0).transpose()
syncNoiseInjections = np.concatenate((syncNoise, zeros), axis=0).transpose()

for li, sni, so in zip(latencyInjections, syncNoiseInjections,  sync_rpcs):
    cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
    method=so[1]
    doc = open(cfgFile ,mode='r')
    ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
    doc.close()
    ymlDict['experiment'][method] = ','.join(str(x) for x in li)
    ymlDict['noise'][method] = ','.join(str(x) for x in sni)
    doc = open(cfgFile ,mode='w')
    ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)

so = random.choice(async_rpcs)
cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
method=so[1]
doc = open(cfgFile ,mode='r')
ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
doc.close()
ymlDict['noise'][method] = ','.join([str(int(ceil(REQ_MEAN*NOISE_INTESITY))) for _ in range(num_req_classes)] + ['0' for _ in range(num_req_classes - num_patterns)])
doc = open(cfgFile ,mode='w')
ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)

for so in [x for x in async_rpcs if x!=so]:
    cfgFile= './config/src/main/resources/shared/' + so[0] + '.yml'
    method=so[1]
    doc = open(cfgFile ,mode='r')
    ymlDict = ruamel.yaml.load(doc, Loader=RoundTripLoader)
    doc.close()
    ymlDict['noise'][method] = ','.join(['0' for _ in range(num_req_classes)])
    doc = open(cfgFile ,mode='w')
    ruamel.yaml.dump(ymlDict, doc, Dumper=RoundTripDumper)