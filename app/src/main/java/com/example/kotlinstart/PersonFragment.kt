package com.example.kotlinstart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_person.*

internal class PersonFragment : Fragment() {

    private lateinit var communicator: Communicator

    /*fun getCommunicator():Communicator = communicator
    fun setCommunicator(communicator: Communicator) {this.communicator = communicator}
    */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as Communicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_apply.setOnClickListener {
            val person =
                Person(text_view_name.text.toString(), edit_text_age.text.toString().toInt())
            communicator.passDataComm(person)
        }
    }

    companion object {
        const val PERSON_KEY = "personKye"

        @JvmStatic
        fun newInstance(counter: Int) =
            PersonFragment().apply { arguments = bundleOf(PERSON_KEY to counter) }
    }
}
