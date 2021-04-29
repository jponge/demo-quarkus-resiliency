#!/bin/sh

echo "💥 Stopping..."

pumba stop \
    --restart \
    --duration 30s \
    $1

echo "🏁 Done, should restart now"