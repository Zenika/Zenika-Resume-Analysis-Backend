#!/bin/bash

set -e

curl -u elastic:changeme -H 'Content-Type: application/json' -X PUT \
-d @/elasticsearch-data/mapping.json http://elasticsearch:9200/formation-user


curl -u elastic:changeme -H 'Content-Type: application/json' -X POST \
-d @/elasticsearch-data/alias.json http://elasticsearch:9200/_aliases