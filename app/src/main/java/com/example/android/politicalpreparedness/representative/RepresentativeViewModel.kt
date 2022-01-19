package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RepresentativeViewModel: ViewModel() {

    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives : LiveData<List<Representative>>
    get() = _representatives

     private var _addressLiveData = MutableLiveData<Address>()
        val addressLiveData : LiveData<Address>
        get() = _addressLiveData

init {
    _addressLiveData.value =Address("","","","","")
}

     fun getRepresentativeList(address : String) {

             viewModelScope.launch {
          try
          {
              val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                 _representatives.value =
                     offices.flatMap { office -> office.getRepresentatives(officials) }
             }catch (e : HttpException)
          {
              Log.i("myTag","Error Occurred :${e.message}")
          }
         }
     }

    fun setAddress(address: Address){
        _addressLiveData.value = address

    }

}
