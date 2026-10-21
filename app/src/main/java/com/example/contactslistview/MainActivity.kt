package com.example.contactslistview

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 1) Les données : une liste MODIFIABLE de chaînes
    private val contacts = mutableListOf(
        "Ahmed Ben Ali",
        "Sarra Trabelsi",
        "Mohamed Gharbi",
        "Ines Jaziri",
        "Youssef Mansouri",
        "Amira Chaabane",
        "Karim Bouazizi",
        "Nour Hammami"
    )

    // 2) L'adaptateur fait le lien entre les données et la ListView
    private lateinit var adapter: ArrayAdapter<String>

    // 3) Les vues
    private lateinit var etContact: EditText
    private lateinit var btnModifier: Button
    private lateinit var lvContacts: ListView

    // Position de l'élément sélectionné (-1 = aucune sélection)
    private var positionSelectionnee = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupération des vues déclarées dans activity_main.xml
        etContact = findViewById(R.id.etContact)
        btnModifier = findViewById(R.id.btnModifier)
        lvContacts = findViewById(R.id.lvContacts)

        // simple_list_item_activated_1 met en surbrillance l'élément sélectionné
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_activated_1,
            contacts
        )
        lvContacts.adapter = adapter

        // Clic sur un élément : on copie son texte dans l'EditText
        lvContacts.setOnItemClickListener { _, _, position, _ ->
            positionSelectionnee = position
            lvContacts.setItemChecked(position, true)
            etContact.setText(contacts[position])
            etContact.setSelection(etContact.text.length) // curseur à la fin
            etContact.requestFocus()
        }

        // Clic sur le bouton "Modifier"
        btnModifier.setOnClickListener { modifierContact() }

        // La touche "Terminé" du clavier valide aussi la modification
        etContact.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                modifierContact()
                true
            } else {
                false
            }
        }
    }

    /** Enregistre le texte de l'EditText dans la liste, à la position sélectionnée. */
    private fun modifierContact() {
        // Contrôle 1 : un élément doit être sélectionné
        if (positionSelectionnee == -1) {
            Toast.makeText(this, R.string.msg_aucune_selection, Toast.LENGTH_SHORT).show()
            return
        }

        // Contrôle 2 : le texte ne doit pas être vide
        val nouveauNom = etContact.text.toString().trim()
        if (nouveauNom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return
        }

        // Mise à jour de la donnée...
        contacts[positionSelectionnee] = nouveauNom
        // ...puis on prévient l'adaptateur pour qu'il redessine la liste
        adapter.notifyDataSetChanged()

        Toast.makeText(this, getString(R.string.msg_contact_modifie, nouveauNom), Toast.LENGTH_SHORT).show()
    }
}