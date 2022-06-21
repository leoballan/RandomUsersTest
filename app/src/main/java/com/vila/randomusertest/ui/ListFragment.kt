package com.vila.randomusertest.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vila.randomusertest.R
import com.vila.randomusertest.databinding.FragmentListBinding
import com.vila.randomusertest.domain.models.WebResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private var _binding: FragmentListBinding? = null
    val binding get() = _binding
    val viewmodel: MainViewModel by activityViewModels()
    val mAdapter = UserAdapter { user ->
        viewmodel.setSelectedUser(user)
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        findNavController().navigate(action)
    }
    private lateinit var animation: Animation


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)
        init()
    }

    private fun init() {
        animation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
        initRecyclerview()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding?.btRefresh?.setOnClickListener {
            viewmodel.fetchUserWithStateFlow()
        }

        binding?.etSearch?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().length>4){
                    viewmodel.filterList(text.toString())
                }else if(text.toString().isEmpty()){
                    viewmodel.resetFilter()
                }
            }
        })
    }

    private fun initObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.usersStateFlow.collect { response ->
                    when (response) {
                        is WebResponse.Loading -> {
                            viewmodel.setProgressState(true)
                            Log.d("webservice", " Loading..............")
                        }
                        is WebResponse.Success -> {
                            viewmodel.setProgressState(false)
                            Toast.makeText(
                                activity,
                                R.string.Success_loading_data,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("webservice", " Success..............")
                            viewmodel.setListTemp(response.data)
                            viewmodel.resetUsersStateFlow()
                        }
                        is WebResponse.Error -> {
                            viewmodel.setProgressState(false)
                            Toast.makeText(
                                activity,
                                R.string.Error_loading_data,
                                Toast.LENGTH_SHORT
                            ).show()
                            viewmodel.resetUsersStateFlow()
                            Log.d("webservice", " error......${response.exception}")
                        }
                        else ->{}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.progressSharedFlow.collect { state ->
                    binding?.let {
                        when (state) {
                            true -> {
                                it.progress.startAnimation(animation)
                                it.progress.visibility = View.VISIBLE
                            }
                            false -> {
                                it.progress.clearAnimation()
                                it.progress.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.userList.collect { list ->
                    mAdapter.submitList(list)
                }
            }
        }
    }

    private fun initRecyclerview() {
        binding!!.recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}