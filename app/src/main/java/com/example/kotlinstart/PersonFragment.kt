package com.example.kotlinstart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_person.*

const val personKye = "personKye"

class PersonFragment : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_person, container, false)

        communicator = activity as Communicator

        view.findViewById<Button>(R.id.button_apply).setOnClickListener {
            val person =
                Person(text_view_name.text.toString(), edit_text_age.text.toString().toInt())
            communicator.passDataComm(person)
        }

        return view
    }

}