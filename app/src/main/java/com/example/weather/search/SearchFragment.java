package com.example.weather.search;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weather.databinding.FragmentSearchBinding;
import com.example.weather.helpers.LocationHelper;
import com.example.weather.helpers.SharedPreferenceHelper;

public class SearchFragment extends Fragment {
    private SearchViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        String city = SharedPreferenceHelper.getCity(requireContext());
        if (!city.isEmpty()) {
            viewModel.updateLocationWithCity(city);
        } else if (LocationHelper.checkPermission(requireContext())) {
            LocationHelper.getLocation(requireContext(), viewModel::updateLocation);
        } else {
            requestPermissions(LocationHelper.permissions, LocationHelper.PERMISSION_REQUEST);
        }

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.PERMISSION_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Go to settings and enable the permission", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LocationHelper.getLocation(requireContext(), viewModel::updateLocation);
                }
            }
        }
    }
}