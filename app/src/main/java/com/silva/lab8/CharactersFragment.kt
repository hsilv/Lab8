package com.silva.lab8

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.silva.lab8.adapters.CharacterAdapter
import com.silva.lab8.db.CharacterRM
import com.silva.lab8.db.RickAndMortyDB

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerViewCharacterClickHandler {
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList: MutableList<CharacterRM>
    private lateinit var toolbar: MaterialToolbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.characters_recyclerView)
        toolbar = (activity as MainActivity).getToolbar()
        setUpRecycler()
        setListeners()

    }

    private fun setListeners() {
        toolbar.setOnMenuItemClickListener{menuItem->
            when(menuItem.itemId){
                R.id.menu_character_sort_aToZ ->{
                    characterList.sortBy { characterRM -> characterRM.name }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                R.id.menu_character_sort_zToA ->{
                    characterList.sortByDescending { characterRM -> characterRM.name  }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                else -> true
            }
        }
    }

    private fun setUpRecycler() {
        characterList = RickAndMortyDB.getCharacters()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(characterList, this)
    }

    override fun onCharacterClicked(character: CharacterRM) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
            character.species, character.status, character.gender, character.name, character.image
        )
        requireView().findNavController().navigate(action)
    }

}