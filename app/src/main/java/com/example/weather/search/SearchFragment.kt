package com.example.weather.search

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.helpers.LocationHelper
import com.example.weather.helpers.LocationHelper.PERMISSION_REQUEST
import com.example.weather.helpers.SharedPreferenceHelper

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        context?.let { appContext ->
            val city = SharedPreferenceHelper.getCity(appContext)
            if (!city.isNullOrEmpty()) {
                viewModel.updateLocationWithCity(city)
            } else if (LocationHelper.checkPermission(appContext)) {
                LocationHelper.getLocation(appContext) {
                    viewModel.updateLocation(it)
                }
            } else{
                requestPermissions(LocationHelper.permissions, PERMISSION_REQUEST)
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    context?.let { appContext ->
                        LocationHelper.getLocation(appContext) {
                            viewModel.updateLocation(it)
                        }
                    }

                }
            }
        }
    }
}