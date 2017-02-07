#!/bin/bash

# Running the image as detached (-d), binding localhost:9090
docker run -d --name tcf_partners -p 9090:9090 petitroll/tcf-ext

# to stop: docker stop tcf_partners
