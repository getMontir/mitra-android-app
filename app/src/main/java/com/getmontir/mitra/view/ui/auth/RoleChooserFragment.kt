package com.getmontir.mitra.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.getmontir.mitra.R
import com.getmontir.mitra.databinding.FragmentAuthRoleChooserBinding
import com.getmontir.mitra.utils.data.AuthChooserItem
import com.getmontir.mitra.utils.enums.AuthDestination
import com.getmontir.mitra.utils.enums.Role
import com.getmontir.mitra.view.adapter.RoleChooserAdapter
import com.getmontir.mitra.view.ui.base.GetFragment

/**
 * A simple [Fragment] subclass.
 * Use the [RoleChooserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoleChooserFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RoleChooserFragment.
         */
        @JvmStatic
        fun newInstance() = RoleChooserFragment()
    }

    private lateinit var binding: FragmentAuthRoleChooserBinding

    private val args: RoleChooserFragmentArgs by navArgs()

    private val listRole = ArrayList<AuthChooserItem>()

    private var destination: AuthDestination? = null

    private var role: Role? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthRoleChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup args
        destination = args.destination

        // Add role item
        listRole.clear()
        listRole.add(
            AuthChooserItem(
                icon = R.drawable.ic_actor_station,
                role = Role.STATION,
                name = resources.getString(R.string.auth_chooser_station),
                description = resources.getString(R.string.auth_chooser_station_description)
            )
        )
        listRole.add(
            AuthChooserItem(
                icon = R.drawable.ic_actor_mechanic,
                role = Role.MECHANIC,
                name = resources.getString(R.string.auth_chooser_mechanic),
                description = resources.getString(R.string.auth_chooser_mechanic_description)
            )
        )

        // Setup view
        binding.rcRoles.layoutManager = LinearLayoutManager(context)
        binding.rcRoles.adapter = RoleChooserAdapter(requireContext(), listRole, object: RoleChooserAdapter.Callback {
            override fun onItemSelected(r: AuthChooserItem) {
                role = r.role
                if( !binding.btnChoose.isEnabled ) {
                    binding.btnChoose.isEnabled = true
                }
            }

        })

        // Setup listener
        binding.btnChoose.setOnClickListener {
            showDestination()
        }
    }

    private fun showDestination() {
        if( destination == AuthDestination.LOGIN ) {
            // Show login view
            role?.let {
                val action = RoleChooserFragmentDirections.actionRoleChooserFragmentToLoginFragment(it)
                findNavController().navigate(action)
            }
        }

        if( destination == AuthDestination.REGISTER ) {
            // Show register view
            if( role == Role.MECHANIC ) {
                // Show register mechanic
                val action = RoleChooserFragmentDirections.actionRoleChooserFragmentToMechanicRegisterFragment()
                findNavController().navigate(action)
            } else {
                // Show contact register
                val action = RoleChooserFragmentDirections.actionRoleChooserFragmentToStationRegisterContactFragment()
                findNavController().navigate(action)
            }
        }
    }
}