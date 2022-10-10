package com.silva.lab8

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment: Fragment(R.layout.login_fragment) {
    private lateinit var emailInput: TextInputLayout
    private lateinit var passInput: TextInputLayout
    private lateinit var loginButton: Button

    companion object {
        public val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            emailInput = findViewById(R.id.email_input_layout)
            passInput = findViewById(R.id.password_input_layout)
            loginButton = findViewById(R.id.login_button)
        }
        isLogged()
        setListeners()
    }

    private fun setListeners() {
        loginButton.setOnClickListener{
            logInUser(
                email = emailInput.editText!!.text.toString(),
                pass = passInput.editText!!.text.toString()
            )
        }
    }

    private fun logInUser(email: String, pass: String) {
        if((email == getString(R.string.email_input)) && pass == email){
            CoroutineScope(Dispatchers.IO).launch{
                saveLog("mail", email)
            }
            navigateToCharactersList()
        }else{
            Toast.makeText(requireContext(), "Contraseña o Correo incorrectos", Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun saveLog(key:String, value:String){
        val dataStoreKey = stringPreferencesKey(key)
        requireActivity().dataStore.edit { settings -> settings[dataStoreKey] = value }
    }


    private fun isLogged(){
        val dataStoreKey = stringPreferencesKey("mail")
        CoroutineScope(Dispatchers.IO).launch {
            val preferences = requireActivity().dataStore.data.first()
            if(preferences[dataStoreKey] != null){
                navigateToCharactersList()
            }
        }
    }

    private fun navigateToCharactersList() {
        CoroutineScope(Dispatchers.Main).launch {
            requireView().findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToCharactersFragment()
            )
        }
    }
}