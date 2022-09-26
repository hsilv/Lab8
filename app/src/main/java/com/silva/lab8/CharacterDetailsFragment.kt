package com.silva.lab8

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.silva.lab8.adapters.CharacterAdapter
import com.silva.lab8.datasource.api.RetrofitInstance
import com.silva.lab8.datasource.model.CharacterDTO
import com.silva.lab8.datasource.model.CharacterResponse
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {
    private lateinit var characterPicture: CircleImageView
    private lateinit var name: TextView
    private lateinit var species: TextVie
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private lateinit var origin: TextView
    private lateinit var appearances: TextView
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterPicture = view.findViewById(R.id.profile_image)
        name = view.findViewById(R.id.character_name)
        species = view.findViewById(R.id.species_details)
        status = view.findViewById(R.id.status_details)
        gender = view.findViewById(R.id.gender_details)
        origin = view.findViewById(R.id.origin_details)
        appearances = view.findViewById(R.id.episode_details)

        RetrofitInstance.api.getCharacterById(args.id.toString())
            .enqueue(object : Callback<CharacterDTO> {
                override fun onResponse(
                    call: Call<CharacterDTO>,
                    response: Response<CharacterDTO>
                ) {
                    name.text = response.body()?.name
                    species.text = response.body()?.species
                    status.text = response.body()?.status
                    gender.text = response.body()?.gender
                    origin.text = response.body()?.origin?.name
                    appearances.text = response.body()?.episode?.size.toString()
                    characterPicture.load(response.body()?.image) {
                        crossfade(450)
                        crossfade(true)
                        placeholder(R.drawable.downloading_icon)
                        error(R.drawable.error_icon)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
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
                    name.text = ""
                    species.text = ""
                    status.text = ""
                    gender.text = ""
                    origin.text = ""
                    appearances.text = ""
                }
            })


    }
}
