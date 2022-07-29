package com.ocsen.onestep.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.R
import com.ocsen.onestep.databinding.FragmentHomeBinding
import com.ocsen.onestep.ui.adapter.EventAdapter
import com.ocsen.onestep.ui.dashboard.DashboardViewModel
import com.ocsen.onestep.view.RecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), EventAdapter.OnItemClickListener {
    val viewModel: DashboardViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val adapterActiveEvent = EventAdapter(this)
    private lateinit var onScrollListener: RecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()

        viewModel.getPlaces()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupView() {
        val linearlayout =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerActiveEvent.apply {
            adapter = adapterActiveEvent
            itemAnimator = null
            layoutManager = linearlayout
            onScrollListener = object : RecyclerViewScrollListener(linearlayout) {
                override fun onLoadMore(
                    page: Int,
                    totalItemsCount: Int,
                    view: RecyclerView?
                ) {

                }
            }
            addOnScrollListener(onScrollListener)
        }
    }

    fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.listPlaces.collect {
                        adapterActiveEvent.submitList(it)
                    }
                }

            }

        }

    }
    override fun onItemClick(item: LocalPlace) {
        val bundle = Bundle()
        bundle.putParcelable("EVENT_DETAIL", item)
        findNavController().navigate(R.id.navigation_dashboard, bundle)
    }
}