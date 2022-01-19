package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.Status
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import java.util.jar.Manifest

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModels<RepresentativeViewModel>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner  =this
        binding.viewModel = viewModel

    return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher  = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            if(it)
                Log.i("myTag","Permission : Granted")
            else
                Log.i("myTag","Permission : Denied")
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {
                if(it.ordinal== 1)
                {
                    Snackbar.make(binding.root,getString(R.string.error_incorrect_address_message),Snackbar.LENGTH_LONG).show()
                    viewModel.doneShowingSnackbar()
                }
        })


        val adapter = RepresentativeListAdapter()

        binding.representativeRecyclerview.adapter = adapter

        viewModel.addressLiveData.observe(viewLifecycleOwner, Observer {
          binding.state.setNewValue(viewModel.addressLiveData.value?.state)
        })

        viewModel.representatives.observe(this, Observer {
            if(null!=it)
                adapter.submitList(it)
        })

        viewModel.addressLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.state.setNewValue(it.state)
            }
        })
        binding.buttonLocation.setOnClickListener {
            requestPermission()
        }

        binding.buttonSearch.setOnClickListener {

            val address = Address(
                    binding.addressLine1.text.toString(),
                    binding.addressLine2.text.toString(),
                    binding.city.text.toString(),
                    binding.state.selectedItem.toString(),
                    binding.zip.text.toString()
            )
            viewModel.setAddress(address)
            hideKeyboard()
        }
    }

    private fun requestPermission() {
        when {
      ContextCompat.checkSelfPermission(requireContext(),
      android.Manifest.permission.ACCESS_FINE_LOCATION)==
              PackageManager.PERMISSION_GRANTED ->{
                  //Permission Granted
                  getLocation()
                    hideKeyboard()

      }
      ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
      android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
          //Additional rationale should be shown

          Snackbar.make(binding.root,R.string.permission_required,Snackbar.LENGTH_INDEFINITE).setAction(
              R.string.ok
          ) {
              requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
          }
          //End of rationale
      }else ->{
                //Permission has not been asked yet
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location->
        if(location!=null)
        {
            viewModel.setAddress(
            geoCodeLocation(location))
        }

        }

     }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


}