package com.example.kotlinstart.view.mainscreen

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentMainBinding
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.DetailsViewPagerAdapter
import com.example.kotlinstart.view.weatherlistscreen.ListState
import com.example.kotlinstart.view.weatherlistscreen.WeatherListFragment
import com.example.kotlinstart.view.weatherlistscreen.WeatherListFragment.Companion.LIST_STATE_KEY
import com.example.kotlinstart.view.weatherlistscreen.WeatherListFragment.Companion.REQUEST_KEY
import com.google.android.material.bottomappbar.BottomAppBar

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var mainBinding: FragmentMainBinding? = null
    private val binding get() = mainBinding!!
    private lateinit var adapter: DetailsViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        checkFragmentResult()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewMainParams()
        initAdapterAndPager()
        createBottomBarAndNavigationIcon()
    }

    private fun initViewMainParams() {
        viewModel.subscribeOnWeatherFromDB().observe(viewLifecycleOwner) { onWeatherList(it) }
        viewModel.getWeatherParamsFromDataBase()
    }

    private fun onWeatherList(list: MutableList<DetailsFragment>) {
        if (list.isNotEmpty()) {
            binding.listEmptyTextView.visibility = View.INVISIBLE
        }
        adapter.addNewList(list)
    }

    //TODO Разобратья почему не реагирует на клики меню
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bottom_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_favourite -> {
                openWeatherListFragment()
            }
            R.id.app_bar_settings -> Toast.makeText(
                requireContext(),
                R.string.settings,
                Toast.LENGTH_SHORT
            )
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBottomAppBar() {
        binding.bottomAppBar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_hamburger_menu_bottom_bar)
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
    }

    private fun initAdapterAndPager(position: Int = 0) {
        adapter = DetailsViewPagerAdapter(requireActivity(), mutableListOf())
        binding.pager.adapter = adapter
        setPagerPosition(position)
    }

    private fun setPagerPosition(position: Int) {
        Handler().postDelayed({
            binding.pager.setCurrentItem(position, true)
        }, 100)
    }

    private fun createBottomBarAndNavigationIcon() {
        initFab()
        initBottomAppBar()
    }

    private fun initFab() {
        binding.fab.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_plus_cross
            )
        )
        binding.fab.setOnClickListener {
            openWeatherListFragment()
        }
    }

    private fun openWeatherListFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_container, WeatherListFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun checkFragmentResult() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this
        ) { requestKey, bundle ->
            if (requestKey == REQUEST_KEY) {
                val result = bundle.get(LIST_STATE_KEY) as ListState
                println(result)
                when (result) {
                    is ListState.NotChanged -> {
                        if (result.refresh) {
                            initViewMainParams()
                        }
                    }
                    is ListState.ToPosition -> {
                        if (result.refresh) {
                            initViewMainParams()
                            setPagerPosition(result.position)
                        } else {
                            setPagerPosition(result.position)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGER_POSITION = "PAGER_POSITION"

        fun newInstance(position: Int) =
            MainFragment().apply { arguments = bundleOf(PAGER_POSITION to position) }
    }
}
