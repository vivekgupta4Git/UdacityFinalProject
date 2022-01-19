package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.Status
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException


enum class ResponseStatus{
    STATUS_LOADING,
    STATUS_ERROR,
    STATUS_DONE
}
class RepresentativeViewModel: ViewModel() {


    private var _status = MutableLiveData<ResponseStatus>()
    val status : LiveData<ResponseStatus>
    get() = _status


    fun doneShowingSnackbar(){
        _status.value = ResponseStatus.STATUS_LOADING
    }

    private var _representatives = MutableLiveData<List<Representative>?>()
    val representatives : LiveData<List<Representative>?>
    get() = _representatives

     private var _addressLiveData = MutableLiveData<Address>()
        val addressLiveData : LiveData<Address>
        get() = _addressLiveData

init {
    _addressLiveData.value =Address("","","","","")
}


    fun getRepresentativeList(address : String) {

             viewModelScope.launch {
                 _status.value = ResponseStatus.STATUS_LOADING
                 //making list to reappear by discarding earlier fetched list and invalidating address
                 _representatives.value = null
          try
          {
              val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                 _representatives.value =
                     offices.flatMap { office -> office.getRepresentatives(officials) }
              _status.value = ResponseStatus.STATUS_DONE
             }catch (e : HttpException)
          {
            _status.value = ResponseStatus.STATUS_ERROR
              _representatives.value = null
          }
         }
     }

    fun setAddress(address: Address){

         _addressLiveData.value = address
        getRepresentativeList(_addressLiveData.value!!.toFormattedString())
    }


}
