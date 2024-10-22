package com.demo.dhiwise.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.demo.dhiwise.R
import com.demo.dhiwise.model.Interest
import com.demo.dhiwise.view.adapter.GridviewAdapter

class FragmentProfileSetup3 : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var adapter: GridviewAdapter

    private val interests = listOf(
        Interest(R.drawable.gym, R.drawable.gym_selected, "Strength Training"),
        Interest(R.drawable.yoga, R.drawable.yoga_selected, "Mind/Body"),
        Interest(R.drawable.running, R.drawable.running_selected, "Cardio"),
        Interest(R.drawable.home_workout, R.drawable.home_workout_selected, "Rehab/Recovery"),
        Interest(R.drawable.cycling, R.drawable.cycling_selected, "Outdoor Activities"),
        Interest(R.drawable.running, R.drawable.running_selected, "Group Classes"),
        Interest(R.drawable.diet, R.drawable.diet_selected, "Nutrition"),
        Interest(R.drawable.sports, R.drawable.sports_selected, "Sports"),
        Interest(R.drawable.cardio, R.drawable.cardio_selected, "HIIT")
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup3, container, false)

        gridView = view.findViewById(R.id.gridView)
        adapter = GridviewAdapter(requireContext(), interests)
        gridView.adapter = adapter

        return view
    }
}
