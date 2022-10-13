package com.silva.lab8

import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.silva.lab8.LoginFragment.Companion.dataStore
import com.silva.lab8.adapters.CharacterAdapter
import com.silva.lab8.datasource.api.RetrofitInstance
import com.silva.lab8.datasource.model.CharacterDTO
import com.silva.lab8.datasource.model.CharacterResponse
import com.silva.lab8.entities.Character
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerViewCharacterClickHandler {
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList: MutableList<Character>
    private lateinit var toolbar: MaterialToolbar
    private lateinit var loginFragment: LoginFragment
    private lateinit var database: com.silva.lab8.datasource.local_source.Database

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.characters_recyclerView)
        toolbar = (activity as MainActivity).getToolbar()
        characterList = mutableListOf()
        loginFragment = LoginFragment()
        database = Room.databaseBuilder(
            requireContext(),
            com.silva.lab8.datasource.local_source.Database::class.java,
            "dataBaseCharacters"
        ).build()

        setupInit()
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
                   CoroutineScope(Dispatchers.IO).launch{
                       requireActivity().dataStore.edit { settings -> settings.remove(
                           stringPreferencesKey("mail")
                       )}
                   }
                    requireView().findNavController().navigate(R.id.action_charactersFragment_to_loginFragment)
                    true
                }
                R.id.menu_sync->{
                    consume(0)
                    true
                }
                else -> true
            }
        }
    }

    private fun refreshRecycler() {
        var listenerInstance = this
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<CharacterResponse>{
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if(response.isSuccessful){
                    println(response.body())
                    characterList = dtoToDao(response.body()?.results as MutableList<CharacterDTO>)
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

    override fun onCharacterClicked(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
            character.id
        )
        requireView().findNavController().navigate(action)
    }

    fun setUpRecycler(listenerInstance: CharactersFragment){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(characterList, listenerInstance)
    }

    fun insert(characterList: MutableList<Character>){
        for (character in characterList) {
            CoroutineScope(Dispatchers.IO).launch {
                database.characterDao().insert(character)
            }
        }
    }

    fun typeConverter(character: CharacterDTO): Character{
        return Character(
            character.id,
            character.name,
            character.species,
            character.status,
            character.gender,
            character.origin.name,
            character.episode.size,
            character.image
        )
    }

    fun dtoToDao(list: MutableList<CharacterDTO>): MutableList<Character>{
        var result = mutableListOf<Character>()
        for (characterDTO in list) {
            result.add(typeConverter(characterDTO))
        }
        return result
    }


    fun consume(flag: Int){
        var listenerInstance = this
        characterList.clear()
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<CharacterResponse>{
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if(response.isSuccessful){
                    if(flag == 1){
                        characterList = dtoToDao(response.body()?.results as MutableList<CharacterDTO>)
                        for (character in characterList) {
                            CoroutineScope(Dispatchers.IO).launch {
                                database.characterDao().insert(character)
                            }
                        }
                        setUpRecycler(listenerInstance)
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            database.characterDao().removeDB()
                        }
                        CoroutineScope(Dispatchers.Main).launch{
                            characterList.clear()
                            characterList = dtoToDao(response.body()?.results as MutableList<CharacterDTO>)
                            insert(characterList)
                            setUpRecycler(listenerInstance)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                println("Error")
            }

        })

    }

    fun setupInit(){
        var listenerInstance = this
        CoroutineScope(Dispatchers.IO).launch {
            val charactersList = database.characterDao().getAllCharacters()
            if(charactersList.isEmpty()){
                consume(1)
            }else{
                characterList.clear()
                characterList.addAll(charactersList)
                CoroutineScope(Dispatchers.Main).launch {
                    setUpRecycler(listenerInstance)
                }
            }
        }
    }

}