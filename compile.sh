#!/bin/bash

echo "🎛️ Compilation de la simulation jardin avancée..."

mkdir -p build

echo "Compilation en cours..."
javac -d build src/Position.java src/Carotte.java src/GardenStatistics.java src/Maison.java src/GardenAgent.java src/GardenEnvironmentConfigurable.java src/GardenPanel.java src/GardenControlPanel.java src/GardenParameterPanel.java src/GardenSimulationAdvanced.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie!"
    echo ""
    echo "🚀 Pour lancer la simulation avancée:"
    echo "   export DISPLAY=localhost:0"
    echo "   java -cp build GardenSimulationAdvanced"
    echo ""
    echo "📋 Classes compilées:"
    ls build/*.class | grep -E "(Garden|Carotte|Maison|Position)" | sed 's/build\//  - /'
else
    echo "❌ Erreur de compilation"
    echo ""
    echo "💡 Essayez d'abord:"
    echo "   ./fix-garden-files.sh"
    exit 1
fi
