package com.example.kotlinstart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val person: Person? = arguments?.getParcelable(personKye)

        when {
            person?.age!! in 19..49 -> {
                view.findViewById<TextView>(R.id.text_introduce).text  = "Привет, ${person.name}! Тебе уже можно сгоняй за пивком"
            }
            person.age > 50 -> {
                view.findViewById<TextView>(R.id.text_introduce).text =
                    "Привет, ${person.name}! Столько не живут! Признайся тебя забыли вытащить из холодильника?!"
            }
            else -> view.findViewById<TextView>(R.id.text_introduce).text =
                "Привет, ${person.name}! Отправляйся в криогенный сон, малыш!"
        }

        return view
    }

}