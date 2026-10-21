# ContactsListView

Application Android (Kotlin) qui affiche une liste de contacts modifiable dans une `ListView`.
TP réalisé avec Android Studio : `ListView`, `ArrayAdapter`, `EditText`, `notifyDataSetChanged()`.

## Fonctionnalités

- Affichage de 8 contacts dans une `ListView`, triés par ordre alphabétique (sans tenir compte des majuscules)
- **Sélection** : un clic sur un contact le surligne et copie son nom dans le champ de saisie
- **Modifier** : enregistre le nouveau nom dans la liste (bouton ou touche « Terminé » du clavier)
- **Ajouter** : ajoute le texte saisi comme nouveau contact
- **Supprimer** : supprime le contact sélectionné après confirmation (bouton, ou appui long sur un contact)
- **Contrôles** : message d'erreur si le nom est vide, refus des doublons, Toast si aucun contact n'est sélectionné

## Notions abordées

| Notion | Rôle dans l'application |
|---|---|
| `mutableListOf` | liste de contacts modifiable |
| `ArrayAdapter` | fait le lien entre la liste Kotlin et la `ListView` |
| `setOnItemClickListener` | réagit au clic sur un contact |
| `setOnItemLongClickListener` | suppression par appui long |
| `notifyDataSetChanged()` | redessine la liste après chaque changement |
| `AlertDialog` | confirmation avant suppression |
| `Toast` / `EditText.error` | messages à l'utilisateur |

## Structure du projet

```
ContactsListView/
├── app/src/main/
│   ├── java/com/example/contactslistview/MainActivity.kt
│   ├── res/layout/activity_main.xml
│   └── res/values/strings.xml
├── screenshots/
└── README.md
```

## Lancer le projet

1. Ouvrir le dossier dans Android Studio
2. Synchroniser Gradle (**File → Sync Project with Gradle Files**)
3. Lancer sur un émulateur ou un téléphone (API 24 minimum) avec le bouton ▶

## Captures d'écran

| Écran au démarrage | Contact sélectionné | Modification |
|---|---|---|
| ![Démarrage](screenshots/demarrage.png) | ![Sélection](screenshots/selection.png) | ![Modification](screenshots/modification.png) |

| Ajout | Confirmation de suppression | Erreur de saisie |
|---|---|---|
| ![Ajout](screenshots/ajout.png) | ![Suppression](screenshots/suppression.png) | ![Erreur](screenshots/erreur.png) |

## Limite connue

Quand l'écran est tourné, Android recrée l'activité et la liste revient à ses valeurs de départ : les modifications sont perdues. Une amélioration possible est de placer la liste dans un `ViewModel`, qui survit à la rotation.

