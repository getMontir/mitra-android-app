package com.getmontir.mitra.view.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.getmontir.lib.presentation.base.BaseFragment
import com.getmontir.mitra.databinding.FragmentOfflineBinding

open class OfflineFragment : BaseFragment() {

    private lateinit var binding: FragmentOfflineBinding

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment OfflineFragment.
         */
        @JvmStatic
        fun newInstance() = OfflineFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfflineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTryAgain.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnClose.setOnClickListener {
            activity?.finish()
        }
    }
}