package com.example.kotlinstart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Communicator {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.list_container, PersonFragment())
            .commitAllowingStateLoss()
    }

    override fun passDataComm(person: Person) {
        val bundle = Bundle()
        bundle.putParcelable(personKye, person)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentMain = MainFragment()
        fragmentMain.arguments = bundle

        transaction.replace(R.id.list_container, fragmentMain).addToBackStack(null).commitAllowingStateLoss()
    }
}