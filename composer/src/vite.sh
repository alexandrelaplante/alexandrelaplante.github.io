#!/bin/bash

npm run build
while true; do

inotifywait -e modify,create,delete -r ./ && \
npm run build

done
