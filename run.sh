#!/bin/bash

echo "🚀 Lancement de la version avancée..."

if [ ! -d "build" ]; then
    echo "❌ Exécutez d'abord: ./compile.sh"
    exit 1
fi

export DISPLAY=localhost:0
java -cp build GardenSimulationAdvanced
