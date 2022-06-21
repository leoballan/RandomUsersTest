package com.vila.randomusertest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.vila.randomusertest.R
import com.vila.randomusertest.databinding.FragmentDetailBinding
import com.vila.randomusertest.domain.models.WebResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding
    private val viewmodel : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
        init()
    }

    private fun init(){
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.selectedUser.collect { user ->
                    binding?.let {
                        it.name.text=user.name.first
                        it.lastName.text = user.name.last
                        it.age.text = user.age.toString()
                        it.email.text = user.email
                        it.tvLatitude.text = user.coordinates.latitude
                        it.tvLongitude.text = user.coordinates.longitude
                        Glide.with(requireActivity())
                            .load(user.picture.large)
                            .centerInside()
                            .into(it.image)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}