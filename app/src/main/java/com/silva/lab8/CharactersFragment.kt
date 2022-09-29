package com.silva.lab8

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.silva.lab8.adapters.CharacterAdapter
import com.silva.lab8.datasource.api.RetrofitInstance
import com.silva.lab8.datasource.model.CharacterDTO
import com.silva.lab8.datasource.model.CharacterResponse
import com.silva.lab8.db.CharacterRM
import com.silva.lab8.db.RickAndMortyDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerViewCharacterClickHandler {
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList: MutableList<CharacterDTO>
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
                R.id.menu_logout -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        requireContext().dataStore.removePreferencesValue(NAME)
                        CoroutineScope(Dispatchers.Main).launch {
                            requireView().findNavController().navigate(
                                CharactersFragmentDirections.actionCharactersFragmentToLoginFragment()
                            )
                        }
                    }
                    true
                }
                else -> true
            }
        }
    }

    private fun setUpRecycler() {
        var listenerInstance = this
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<CharacterResponse>{
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if(response.isSuccessful){
                    println(response.body())
                    characterList = response.body()?.results as MutableList<CharacterDTO>
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = CharacterAdapter(characterList, listenerInstance)
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                println("Error")
            }

        })
    }

    override fun onCharacterClicked(character: CharacterDTO) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
            character.id
        )
        requireView().findNavController().navigate(action)
    }

}