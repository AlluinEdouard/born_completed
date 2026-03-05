# Architecture Technique

L'interface principale de la borne d'arcade est développée en **Java** et s'appuie sur le moteur graphique maison **MG2D**.

## 🏗️ Structure du Projet
* `MG2D/` : Code source de la bibliothèque graphique et audio (gestion de l'affichage, des formes, capture clavier, et lecture audio via JLayer).
* `projet/` : Contient les répertoires des différents jeux de la borne.
* `docs/` : Documentation "Docs-as-Code" (générée via MkDocs et des LLM locaux).
* `sound/bg/` : Répertoire contenant les musiques de fond jouées aléatoirement par le menu.

## ⚙️ Classes Principales
* **`Graphique.java`** : Cœur de l'affichage du menu. Gère le carrousel, la musique de fond, et l'interface globale.
* **`Bouton.java`** : Parcours dynamique du dossier `projet/` pour instancier les éléments du menu.
* **`Pointeur.java`** : Gère la logique de lancement en exécutant les processus système externes (`ProcessBuilder("bash", script)`).
* **Les Boîtes (`Boite.java`)** : 
  * `BoiteSelection.java` : Gère l'animation de translation (défilement) du carrousel.
  * `BoiteImage.java` : Charge l'image `photo_small.png` du jeu sélectionné.
  * `BoiteDescription.java` : Parse les fichiers `.txt` locaux du jeu pour afficher les contrôles et la description.
* **`HighScore.java` & `LigneHighScore.java`** : Moteur de sauvegarde et d'affichage des 10 meilleurs scores, incluant la logique d'édition (saisie à 3 lettres au joystick).

## 📚 Documentation de l'API (Javadoc)
Le détail exhaustif de toutes les classes, méthodes et attributs du code Java est généré automatiquement lors du build.
👉 [Consulter la Javadoc technique du code](api/index.html)