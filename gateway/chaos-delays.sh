#!/bin/sh

echo "💥 Simulating traffic delays"

pumba netem \
    --duration 30s \
    --tc-image gaiadocker/iproute2 \
    delay --distribution pareto --time 3000 --jitter 500 \
    $1

echo "🏁 Done with traffic delays"