package com.silva.lab8

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private lateinit var button: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.Boton_de_prueba)
        setListeners()
    }

    private fun setListeners() {
        button.setOnClickListener{
            val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment("","","","","")
            requireView().findNavController().navigate(action)
        }
    }
}