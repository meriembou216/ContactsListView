package com.example.contactslistview

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var btnAjouter: Button
    private lateinit var btnSupprimer: Button
    private lateinit var lvContacts: ListView

    // Position de l'élément sélectionné (-1 = aucune sélection)
    private var positionSelectionnee = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupération des vues déclarées dans activity_main.xml
        etContact = findViewById(R.id.etContact)
        btnModifier = findViewById(R.id.btnModifier)
        btnAjouter = findViewById(R.id.btnAjouter)
        btnSupprimer = findViewById(R.id.btnSupprimer)
        lvContacts = findViewById(R.id.lvContacts)

        // Liste triée dès le départ, sans tenir compte des majuscules
        contacts.sortWith(String.CASE_INSENSITIVE_ORDER)

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

        // Appui long sur un élément : suppression avec confirmation
        lvContacts.setOnItemLongClickListener { _, _, position, _ ->
            confirmerSuppression(position)
            true
        }

        // Boutons
        btnModifier.setOnClickListener { modifierContact() }
        btnAjouter.setOnClickListener { ajouterContact() }
        btnSupprimer.setOnClickListener {
            if (positionSelectionnee == -1) {
                Toast.makeText(this, R.string.msg_aucune_selection, Toast.LENGTH_SHORT).show()
            } else {
                confirmerSuppression(positionSelectionnee)
            }
        }

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

        // Contrôle 2 : nom non vide et sans doublon (on ignore le contact modifié lui-même)
        val nouveauNom = lireNomValide(positionAIgnorer = positionSelectionnee) ?: return

        contacts[positionSelectionnee] = nouveauNom
        rafraichirListe()
        Toast.makeText(this, getString(R.string.msg_contact_modifie, nouveauNom), Toast.LENGTH_SHORT).show()
    }

    /** Ajoute le texte de l'EditText comme nouveau contact. */
    private fun ajouterContact() {
        val nouveauNom = lireNomValide(positionAIgnorer = -1) ?: return

        contacts.add(nouveauNom)
        rafraichirListe()
        Toast.makeText(this, getString(R.string.msg_contact_ajoute, nouveauNom), Toast.LENGTH_SHORT).show()
    }

    /** Demande confirmation, puis supprime le contact. */
    private fun confirmerSuppression(position: Int) {
        val nom = contacts[position]
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_titre)
            .setMessage(getString(R.string.dialog_message, nom))
            .setPositiveButton(R.string.btn_oui) { _, _ ->
                contacts.removeAt(position)
                rafraichirListe()
                Toast.makeText(this, getString(R.string.msg_contact_supprime, nom), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.btn_non, null)
            .show()
    }

    /**
     * Lit le texte saisi et le valide : non vide, et pas déjà dans la liste.
     * Retourne null (avec un message d'erreur) si le nom est refusé.
     * positionAIgnorer : position à ne pas comparer (le contact en cours de modification).
     */
    private fun lireNomValide(positionAIgnorer: Int): String? {
        val nom = etContact.text.toString().trim()
        if (nom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return null
        }
        val existeDeja = contacts.withIndex().any { (index, contact) ->
            index != positionAIgnorer && contact.equals(nom, ignoreCase = true)
        }
        if (existeDeja) {
            etContact.error = getString(R.string.msg_doublon)
            return null
        }
        return nom
    }

    /** Trie la liste, prévient l'adaptateur et remet la sélection à zéro. */
    private fun rafraichirListe() {
        contacts.sortWith(String.CASE_INSENSITIVE_ORDER)
        adapter.notifyDataSetChanged()
        // Après un tri, les positions changent : on désélectionne et on vide le champ
        positionSelectionnee = -1
        lvContacts.clearChoices()
        etContact.text.clear()
    }
}