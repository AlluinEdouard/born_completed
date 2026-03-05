# Ajouter un nouveau jeu sur la borne

Le menu de la borne est dynamique. Il n'est pas nécessaire de modifier le code source Java du menu pour ajouter un jeu ! Le système scanne automatiquement le répertoire `projet/` au démarrage.

## Étape 1 : Créer le répertoire du jeu
Tous les jeux doivent se trouver dans le dossier `projet/` à la racine de la borne.
1. Créez un nouveau dossier portant le nom exact de votre jeu (sans espaces).
   * *Exemple : `projet/MonNouveauJeu/`*

## Étape 2 : Le script de lancement
Le menu utilise un script Bash pour lancer le jeu et suspendre la musique de fond.
1. Dans votre nouveau dossier, créez un script `.sh` portant **exactement le même nom** que le dossier.
   * *Exemple : `projet/MonNouveauJeu/MonNouveauJeu.sh`*
2. Rendez ce script exécutable (`chmod +x MonNouveauJeu.sh`).
3. Ce script doit contenir la commande pour lancer votre jeu. Une fois le jeu terminé, le script se ferme et le menu reprend le contrôle automatiquement.

## Étape 3 : Les assets visuels et textuels
Pour que le jeu s'affiche correctement dans le menu, ajoutez les fichiers suivants dans le dossier de votre jeu :

* **L'image d'aperçu (`photo_small.png`)** : Une image qui s'affichera à droite de l'écran. Si elle est absente, l'image par défaut `img/bouton2.png` sera utilisée.
* **La description (`description.txt`)** : Un fichier texte contenant le résumé du jeu (le menu lira les 10 premières lignes maximum).
* **Les contrôles (`bouton.txt`)** : Un fichier définissant le texte affiché pour le joystick et les 6 boutons. Il doit contenir une seule ligne séparée par des deux-points (`:`). 
  * *Format : `ActionJoystick:ActionBtn1:ActionBtn2:ActionBtn3:ActionBtn4:ActionBtn5:ActionBtn6`*
* **Le fichier des scores (`highscore`)** : Ce fichier texte sera automatiquement généré lors de la première partie si un record est battu.