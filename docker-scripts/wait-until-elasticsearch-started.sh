#!/bin/bash

echo "Waiting for Elasticsearch to start."

until curl -u elastic:changeme http://elasticsearch:9200/_cluster/health?pretty 2>&1 | grep status | egrep "(green|yellow)"; do
  sleep 1
done

echo "Elasticsearch started."