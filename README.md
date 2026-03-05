IUT du Littoral Côte d'Opale (IUTLCO)
====================================
Projet tutoré 2017-2018- 2ème année
----------------------------------

Groupe projet : Romaric Bougard, Pierre Delobel et Bastien Ducloy


Plus d'infos [ici](http://iut.univ-littoral.fr/gitlab/synave/borne_arcade/wikis/home)

# touche de la borne

Correspondance clavier -> bouton borne

joystick j1
fleches haut bas gauche droite

joystick j2
o l k m

6 touches J1
r t y
f g h

6 touches J2
a z e
q s d 

Attention ! De base, l'encodeur clavier de la borne de l'IUT a té mal relié aux boutons. Ce ne sont donc pas les bonnes lettres qui son identifiées lorsque l'on appuie sur un bouton ou fait bouger un joystick. Voir fichier **borne**


Contrainte matérielle
----
- Raspberry pi model 3 de préférence
- Ecran 4:3 de résolution 1280x1024
- Pour borne 2 joueurs, joystick et 6 boutons par joueur + d'autres boutons inutilisés pour le moment.


Installation du système d'exploitation
----
Installez Raspbian sur votre raspberry

Installation des outils
----

Installez le jdk de java. Dans un terminal :
> sudo apt-get update

> sudo apt-get install openjdk-8-jdk

Installez git. Toujours dans le même terminal :
> sudo apt-get install git

Créez un répertoire git :
> cd ~

> mkdir git

> cd git

On va ensuite télécharger la bibliothèque MG2D et la partie logicielle ici présente. Si vous l'avez déjà téléchargée, vous déplacerez le répertoire dans le répertoire git précédemment créé. Le répertoire doit s'appeler ***borne_arcade***

> git clone http://iut.univ-littoral.fr/gitlab/synave/MG2D.git

> git clone http://iut.univ-littoral.fr/gitlab/synave/borne_arcade.git

Suite à ces téléchargements, vous devez avoir l'arborescence suivante :
- répertoire personnel
- &nbsp; &nbsp; |
- &nbsp; &nbsp; |-> git
- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; |
- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; |-> MG2D
- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; |-> borne_arcade

Lancez le logiciel au démarrage de la borne
----

> mv borne.desktop ~/.config/autostart/

Redémarrez et normalement, lors du démarrage, un terminal va s'ouvrir et quelques secondes après (10-15 secondes), l'interface de la borne va se lancer. Des informations concernant les opérations en cours sont affichées dans le terminal. Soyez patient.

Sélectionnez le jeu avec haut/bas du joystick du joueur 1 et lancez le jeu avec le bouton A du joueur 1.
Quittez le logiciel avec le bouton Z du joueur 1. Une demande de confirmation s'affichera. Validez oui ou non avec le bouton A du joueur 1.

Si vous quittez le menu, vous reviendrez sur le terminal. Attendez 30 secondes pour une extinction totale de la machine.
