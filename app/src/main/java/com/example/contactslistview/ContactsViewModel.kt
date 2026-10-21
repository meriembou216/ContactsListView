package com.example.contactslistview

import androidx.lifecycle.ViewModel

/** Garde la liste des contacts et la sélection : le ViewModel survit à la rotation de l'écran. */
class ContactsViewModel : ViewModel() {

    val contacts = mutableListOf(
        "Ahmed Ben Ali",
        "Sarra Trabelsi",
        "Mohamed Gharbi",
        "Ines Jaziri",
        "Youssef Mansouri",
        "Amira Chaabane",
        "Karim Bouazizi",
        "Nour Hammami"
    ).apply { sortWith(String.CASE_INSENSITIVE_ORDER) }

    // Position de l'élément sélectionné (-1 = aucune sélection)
    var positionSelectionnee = -1
}