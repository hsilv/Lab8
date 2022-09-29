package com.silva.lab8

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginFragment: Fragment(R.layout.login_fragment) {
    private lateinit var emailInput: TextInputLayout
    private lateinit var passInput: TextInputLayout
    private lateinit var loginButton: Button

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
        if((email == getString(R.string.email_input)) && email == pass){
            logLongUser(email)
        }else{
            Toast.makeText(requireContext(), "Contraseña o Correo incorrectos", Toast.LENGTH_LONG).show()
        }
    }

    private fun logLongUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            requireContext().dataStore.savePreferencesValue(NAME, email)
            navigateToCharactersList()
        }
    }

    private fun isLogged(){
        CoroutineScope(Dispatchers.IO).launch {
            val thisUser = requireContext().dataStore.getPreferencesValue(EMAIL)
            if(thisUser != null){
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