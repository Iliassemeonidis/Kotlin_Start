package com.example.kotlinstart.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentThreadBinding
import kotlinx.android.synthetic.main.fragment_thread.*
import java.util.*
import java.util.concurrent.TimeUnit

class ThreadFragment : Fragment() {

    private var _binding: FragmentThreadBinding? = null
    private val binding get() = _binding!!
    private var counterThread = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            binding.textView.text = startCalculations(binding.editText.text.toString().toInt())
            binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                text = getString(R.string.in_main_thread)
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })
        }

        binding.calcThreadBtn.setOnClickListener {
            Thread {
                counterThread++
                val calculatedText = startCalculations(editText.text.toString().toInt())
                requireActivity().runOnUiThread {
                    binding.textView.text = calculatedText
                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                        text = String.format(getString(R.string.from_thread), counterThread)
                        textSize = resources.getDimension(R.dimen.main_container_text_size)
                    })
                }
            }.start()
        }
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ThreadFragment().apply {

            }
    }
}