#!/bin/bash
set -e

if [ -f .wheelrc ]; then
  source .wheelrc
fi

DIST_DIR="${PWD}/resources/prod"
lein do clean, cljsbuild once prod
turbolift -u ${RACKSPACE_USER} -a ${RACKSPACE_API_KEY} \
  --os-auth-url "https://identity.api.rackspacecloud.com/v2.0/" \
  --os-region iad upload --sync -s ${DIST_DIR} -c ${RACKSPACE_CONTAINER}
