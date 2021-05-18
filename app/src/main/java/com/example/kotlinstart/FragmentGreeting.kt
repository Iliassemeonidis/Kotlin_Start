package com.example.kotlinstart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_greeting.*

internal class FragmentGreeting : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_greeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Person>(PERSON_KEY)?.let { person ->
            when {
                person.age in 19..49 -> {
                    text_introduce.text =
                            //String.format(Locale.getDefault(), getString(R.string.results_count), count)
                            //<string name="results_count">Number of results: %d</string>
                        "Привет, ${person.name}! Тебе уже можно сгоняй за пивком"
                }
                person.age > 50 -> {
                    text_introduce.text =
                        "Привет, ${person.name}! Столько не живут! Признайся тебя забыли вытащить из холодильника?!"
                }
                else -> text_introduce.text =
                    "Привет, ${person.name}! Отправляйся в криогенный сон, малыш!"
            }
        }
    }
}
