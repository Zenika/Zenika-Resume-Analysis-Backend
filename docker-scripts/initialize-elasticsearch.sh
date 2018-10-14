#!/bin/bash


/docker-scripts/wait-until-elasticsearch-started.sh


echo "============== INIT MAPPING ==================="

/docker-scripts/init-mapping.sh