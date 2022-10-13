package com.silva.lab8

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import coil.load
import coil.request.CachePolicy
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.silva.lab8.datasource.api.RetrofitInstance
import com.silva.lab8.datasource.local_source.Database
import com.silva.lab8.datasource.model.CharacterDTO
import com.silva.lab8.entities.Character
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {
    private lateinit var characterPicture: CircleImageView
    private lateinit var name: TextInputLayout
    private lateinit var species: TextInputLayout
    private lateinit var status: TextInputLayout
    private lateinit var gender: TextInputLayout
    private lateinit var origin: TextInputLayout
    private lateinit var appearances: TextInputLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database
    private lateinit var saveButton: Button
    private lateinit var charInstance: Character
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterPicture = view.findViewById(R.id.profile_image)
        name = view.findViewById(R.id.name_input_layout)
        species = view.findViewById(R.id.species_input_layout)
        status = view.findViewById(R.id.status_input_layout)
        gender = view.findViewById(R.id.gender_input_layout)
        origin = view.findViewById(R.id.origin_input_layout)
        appearances = view.findViewById(R.id.episodes_input_layout)
        toolbar = (activity as MainActivity).getToolbar()
        saveButton = view.findViewById(R.id.save_button)
        database = Room.databaseBuilder(
            requireContext(),
            com.silva.lab8.datasource.local_source.Database::class.java,
            "dataBaseCharacters"
        ).build()
        setChar()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_sync -> {
                    refreshCharacter()
                    true
                }
                R.id.menu_details_delete -> {
                    deleteChar()
                    true
                }
                else -> true
            }
        }
        saveButton.setOnClickListener {
            updateChar()
        }
    }

    private fun deleteChar() {
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().delete(charInstance)
        }
        val action = CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToCharactersFragment()
        requireView().findNavController().navigate(action)
    }

    private fun updateChar() {
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().update(
                charInstance.copy(
                    id = charInstance.id,
                    name = name.editText!!.text.toString(),
                    species = species.editText!!.text.toString(),
                    status = status.editText!!.text.toString(),
                    gender = gender.editText!!.text.toString(),
                    origin = origin.editText!!.text.toString(),
                    episodes = appearances.editText!!.text.toString().toInt(),
                    imageURL = charInstance.imageURL
                )
            )
        }
    }

    private fun setChar() {
        CoroutineScope(Dispatchers.IO).launch {
            charInstance = database.characterDao().getCharacterById(id = args.id)
            CoroutineScope(Dispatchers.Main).launch {
                name.editText!!.setText(charInstance.name)
                species.editText!!.setText(charInstance.species)
                status.editText!!.setText(charInstance.status)
                gender.editText!!.setText(charInstance.gender)
                origin.editText!!.setText(charInstance.origin)
                appearances.editText!!.setText(charInstance.episodes.toString())
                characterPicture.load(charInstance.imageURL) {
                    crossfade(450)
                    crossfade(true)
                    placeholder(R.drawable.downloading_icon)
                    error(R.drawable.error_icon)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
            }
        }
    }

    fun refreshCharacter() {
        RetrofitInstance.api.getCharacterById(args.id.toString())
            .enqueue(object : Callback<CharacterDTO> {
                override fun onResponse(
                    call: Call<CharacterDTO>,
                    response: Response<CharacterDTO>
                ) {
                    name.editText!!.setText(response.body()?.name)
                    species.editText!!.setText(response.body()?.species)
                    status.editText!!.setText(response.body()?.status)
                    gender.editText!!.setText(response.body()?.gender)
                    origin.editText!!.setText(response.body()?.origin!!.name)
                    appearances.editText!!.setText(response.body()?.episode?.size.toString())
                    characterPicture.load(response.body()?.image) {
                        crossfade(450)
                        crossfade(true)
                        placeholder(R.drawable.downloading_icon)
                        error(R.drawable.error_icon)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
                    updateChar()
                    setChar()
                }

                override fun onFailure(call: Call<CharacterDTO>, t: Throwable) {
                    characterPicture.load("") {
                        crossfade(450)
                        crossfade(true)
                        placeholder(R.drawable.downloading_icon)
                        error(R.drawable.error_icon)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
                    name.editText!!.setText("")
                    species.editText!!.setText("")
                    status.editText!!.setText("")
                    gender.editText!!.setText("")
                    origin.editText!!.setText("")
                    appearances.editText!!.setText("")
                }
            })
    }
}
