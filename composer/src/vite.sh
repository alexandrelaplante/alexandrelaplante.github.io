#!/bin/sh

sigint_handler()
{
  npx kill-port 5173
  exit
}

trap sigint_handler SIGINT

while true; do
  npm run dev &
  sleep 2
  inotifywait -e modify,create,delete -r ./
  npx kill-port 5173
done
