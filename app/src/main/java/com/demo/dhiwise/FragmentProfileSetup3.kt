package com.demo.dhiwise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.demo.dhiwise.view.adapter.GridviewAdapter

class FragmentProfileSetup3 : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var adapter: GridviewAdapter

    private val interests = listOf(
        GridItem(R.drawable.cardio, "Cardio"),
        GridItem(R.drawable.cycling, "Cycling"),
        GridItem(R.drawable.diet, "Diet"),
        GridItem(R.drawable.cardio, "Cardio"),
        GridItem(R.drawable.cycling, "Cycling"),
        GridItem(R.drawable.diet, "Diet"),
        GridItem(R.drawable.cardio, "Cardio"),
        GridItem(R.drawable.cycling, "Cycling"),
        GridItem(R.drawable.diet, "Diet")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup3, container, false)

        gridView = view.findViewById(R.id.gridView)
        adapter = GridviewAdapter(requireContext(), interests)
        gridView.adapter = adapter

        return view
    }
}
