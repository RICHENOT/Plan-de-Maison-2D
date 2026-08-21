# Plan de Maison

Application Java (Swing/AWT) de conception et de visualisation de plans de maison en 2D. Elle permet de définir un terrain, d'y positionner une maison, d'ajouter des pièces avec portes et fenêtres, puis de générer, ajuster et exporter le plan.

## Fonctionnalités

- **Terrain & maison** : dimensions du terrain, dimensions et position (X/Y) de la maison sur le terrain
- **Pièces** : ajout / modification / suppression, type (Chambre, Salon, Cuisine, Salle de bain, Couloir), dimensions, position (X/Y libre ou paroi de référence), rotation par pas de 90°
- **Portes** : par pièce, avec mur, position, direction (intérieure/extérieure), type (simple/double) et sens d'ouverture — rendu graphique avec arc de battant
- **Fenêtres** : par pièce, avec mur, position et largeur
- **Placement automatique** des pièces sur une grille (algorithme glouton par taille décroissante, `RoomPlacer`)
- **Détection et correction des débordements** (pièce hors maison, maison hors terrain, porte/fenêtre hors mur), avec ajustement automatique proposé à l'utilisateur
- **Visualisation** du plan avec cotations, zoom avant/arrière (boutons ou `Ctrl` + molette) et défilement
- **Export** du plan affiché en image PNG
- **Sauvegarde / chargement** d'un projet (sérialisation Java native)

## Aperçu de l'interface

La fenêtre principale (`Main.java`) contient :

- Un onglet **Configuration** (`OngletPieces`) : saisie du terrain, de la maison et des pièces (sous-onglets « Pièces » et « Terrain/Maison »)
- Un onglet **Visualisation** (`MaisonPanel`, dans un `JScrollPane`) : le plan généré
- Une barre du haut : export PNG
- Une barre du bas : contrôles de zoom, sauvegarde/chargement de projet, bouton « Afficher le plan »

## Prérequis

- JDK 8 ou supérieur (Swing/AWT uniquement, aucune dépendance externe)

## Compilation et exécution

Depuis le dossier contenant les fichiers `.java` et le dossier `icons/` :

```bash
javac *.java
java Main
```

> Les icônes sont chargées via `getClass().getResource("/icons/...")` : le dossier `icons/` doit rester à la racine du classpath (à côté des `.class`).

## Structure du projet

Toutes les classes sont dans le **package par défaut** (aucun `package ...;`), sans outil de build (pas de Maven/Gradle).

| Fichier | Rôle |
|---|---|
| `Main.java` | Point d'entrée, fenêtre principale, barres d'outils (zoom, export, sauvegarde) |
| `OngletPieces.java` | Panneau de configuration : formulaires terrain/maison/pièces, gestion des portes et fenêtres, liste des pièces |
| `MaisonPanel.java` | Rendu 2D du plan (`paintComponent`) : murs, cotations, portes, fenêtres, zoom |
| `Piece.java` | Modèle d'une pièce (dimensions, position, rotation, portes, fenêtres) — `Serializable` |
| `Porte.java` | Modèle et rendu graphique d'une porte (simple/double, sens d'ouverture) — `Serializable` |
| `Fenetre.java` | Modèle et rendu graphique d'une fenêtre — `Serializable` |
| `RoomPlacer.java` | Algorithme de placement automatique des pièces sur une grille |
| `ExportUtils.java` | Export d'un composant Swing en image PNG |
| `ProjectSerializer.java` | Sauvegarde/chargement d'un projet via `ObjectOutputStream`/`ObjectInputStream` |
| `icons/` | Icônes des boutons de la barre d'outils |

## Persistance des projets

`ProjectSerializer` utilise la sérialisation Java native (pas de format JSON/XML portable) : le fichier généré n'est lisible que par cette application, et sa compatibilité peut casser si la structure des classes change. `Piece` n'a pas de `serialVersionUID` explicite (contrairement à `Porte` et `Fenetre`), ce qui accentue ce risque en cas de recompilation.

## Notes / limitations connues

- Trois classes présentes dans le dépôt ne sont pas branchées à l'IHM actuelle (aucune référence depuis `Main.java` ou `OngletPieces.java`) : `PieceForm.java`, `DragAndDropPanel.java`, `HouseLayoutValidator.java`.
- Deux icônes ne correspondent à aucune fonctionnalité implémentée : `pdf.png` (pas d'export PDF) et `partager-limage.png` (pas de partage d'image).
- Le calcul de la taille de maison requise (`HouseLayoutValidator`, `calculerTailleMaisonRequise`) est simplifié : il additionne les dimensions des pièces par côté (gauche/droite/haut/bas) sans réel algorithme de disposition.
