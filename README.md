# SimulationEgoisme

Ce projet se veux être une simulation du comportement humain et de l'égoïsme. Il est base sur une vidéo dont voici le lien : https://youtu.be/qVOjXQUzOJw?si=E1AG_QaKDbYOKP6U

Toutes les fonctinnalitées ne sont pas encore implémenté et arriverons le plus vite possible.

Cette simulation modélise un écosystème évolutif où des agents intelligents doivent survivre en cherchant de la nourriture dans un jardin. C'est une simulation d'évolution comportementale avec sélection naturelle.

## Actions pour lancer la simulation

Cloner le dépot git

### Compilation :

En invite de commande Linux :

- chmod +x compile.sh
- ./compile.sh

### Execution :

En invite de commande Linux :

- chmod +x run.sh
- ./run.sh

Il est possible que la fenètre s'ouvre en petit en haut il suffit juste de l'agrandir pour que cela fonctionne

## Contenu actuel de la simulation

### Le Jardin (Grille 30x25)
Une grille verte représentant le jardin
On alterne entre des periodes de jour (période active) et de nuit (période passive)
Les agents se déplacent dans le jardin afin de récupérer des carottes lors de la journée
Le soir la sélection se fait en fonction du nombre de carottes récupérées par les agents

### Maisons (Carrés Marrons)
Les maisons permettent d'abriter les agents la nuit
Chaque maison a une capacité de 10 agents, elles sont répartient sur le bord du jardin
Des maisons supplémentaires sont créées automatiquement pour correspondre au nombre d'agent

### Carottes (Triangles Oranges)
Les carottes doivent être récoltées par les agents
Elles sont générées aléatoirement pendant la journée
La probabilité d'apparition est réglable dans le panel des paramètres
Elles disparaissent du jardin quand elles sont mangées (2 agents ne peuvent pas manger la même carotte)
Le nombre maximal de carottes en simultanée est réglable dans le panel des paramètres

### Agents

 * **Règles de survie et d'évolution** : Si en 1 min l'agent obtient au moins 5 carottes alors il passe à la journée suivante, l'agent pourra se reproduire 1 fois pour chaque 5 carottes supplémentaire. S'il obtient moi de 5 carottes alors il meurt.
 * **Vision** : Les agents peuvent voir de 2 à 15 cases (configurable). Les agents repèrnent et se dirignent automatiquement vers les carottes dans ce champs de vision.

 * **Code couleur** :
    * **Rouge** : En danger (0 point) - risque de mort
    * **Orange** : Quelques points (1-2 points) - situation précaire
    * **Jaune** : En bonne voie (3-4 points) - proche de la survie
    * **Vert** : Survie assurée (5+ points) - reproduction possible
    * **Bleu** : À la maison (période de repos)

### Contrôles Interactifs

 * **Boutons Principaux** :
    * **Commencer/Continuer** : Démarrer ou reprendre la simulation
    * **Pause** : Arrêter temporairement
    * **Un Step** : Exécuter une seule étape (utile pour déboguer)  
    * **Nouveau Jardin** : Reset complet avec nouveaux paramètres

### Paramètres Ajustables
 * Vitesse de simulation
    * 20ms à 500ms entre les étapes
    * Contrôle la rapidité de l'animation
 * Gestion des Carottes
    * 20-150 carottes simultanées
    * 10%-80% d'apparition par étape
 * Population
    * 10-100 agents au démarrage
    * Nécessite un reset pour s'appliquer
 * Seuil de Survie
    * 2-8 carottes

## Contenu à rajouter pour la version final

### Ajout des vaches et du concepte d'égoïsme
Chaque jours un agents pourra s'allier à un autre pour aller chasser une vache et gagner plus de points (carottes)

Une vache vaut 10 points. 

La partie interessante est la manière de se partager ces 10 points.

Chaque agent aura une part idéal de la vache en mémoire entre 10% et 90%. 

Si les deux agent ont une part complémentaire alors chaqu'un récupère ce qu'il avaient en mémoire, si les deux parts font moins de 100% alors ce qu'il reste est séparé entre les deux, si le reste est impair alors c'est le plus égoïste celui qui en voulais le plus qui récupère la plus grosse moitier. Si les deux parts font plus que 100% aucun des deux n'obtient l'interssection et ils sont pénalisés de 1 point. 
### Ajout des mutations
Quand un agent va se reproduire il aura 80% de chance créer un copie avec la même stratégie de partage et il aura 10% de chance de créer une copie plus généreuse et 10% de chance de créer une copie plus égoïste.
### Ajout de paramètre réglable
Paramètre de répartition au lancement de la simulation

Paramètre de réglage de la taille du jardin
### Ajout de graphiques et de statistiques pour chaques générations
Ajout d'un graphique du nombre total d'habitant par génération pour voir l'évolution de la population globale

Ajout d'un graphique du nombre d'habitant par catégorie de partage pour voir comment évolue le nombre d'agent par catégorie et comment la sélection des catégorie se fait.
