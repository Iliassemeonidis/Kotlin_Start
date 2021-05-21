package com.example.kotlinstart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_person.*
// TODO rename this class in listOfCitiesFragment
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
//        floating_action_button.setOnClickListener {
//            val person =
//                Weather("text_view_name.text.toString()", 6)
//            communicator.passDataComm(person)
//        }
        createList(view)


    }

    private fun createList(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_main)
        val arr = resources.getStringArray(R.array.city).toList()
        val weather = ArrayList<Weather>()
        (arr.indices).forEach{ i -> weather.add(Weather(arr[i],"27°C"))}

        recyclerView.adapter = WeatherAdapter(weather)
    }




    companion object {
        const val PERSON_KEY = "personKye"

        @JvmStatic
        fun newInstance(counter: Int) =
            PersonFragment().apply { arguments = bundleOf(PERSON_KEY to counter) }
    }

    interface OnClickItem{
       fun onClickItem(weather: Weather)
    }
}
