package com.silva.lab8

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import de.hdodenhof.circleimageview.CircleImageView

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {
    private lateinit var characterPicture: CircleImageView
    private lateinit var name: TextView
    private lateinit var species: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterPicture = view.findViewById(R.id.profile_image)
        name = view.findViewById(R.id.character_name)
        species = view.findViewById(R.id.speceies_details)
        status = view.findViewById(R.id.status_details)
        gender = view.findViewById(R.id.gender_details)
        name.text = args.name
        species.text = args.species
        status.text = args.status
        gender.text = args.gender
        characterPicture.load(args.imageURL){
            crossfade(450)
            crossfade(true)
            placeholder(R.drawable.downloading_icon)
            error(R.drawable.error_icon)
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
        }

    }
}