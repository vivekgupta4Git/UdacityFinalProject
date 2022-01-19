package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"



/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 * Adding Java Date Adapter and custom Election Adapter
 */
private val moshi = Moshi.Builder()
        .add(Date::class.java,Rfc3339DateJsonAdapter())
        .add(ElectionAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

/*
Use the Retrofit builder to build a retrofit object using moshi
convertor with our moshi object
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  A public interface that exposes the [getElections]method
 */

interface CivicsApiService {

    /*
    Returns a ElectionResponse Object
    The @GET annotation indicated that the "elections" endpoint will be
    requested with the GET http method
     */
    @GET("elections")
    suspend fun getElections() : ElectionResponse


    @GET("voterinfo")
    suspend fun getVoterInfo(@Query ("address") address: String,
        @Query("electionId") electionID : Int
                             ) : VoterInfoResponse

    /**
     * this url resulted successful response
     * https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs&address=%22ks%22
     * example of address : 345 Park Ave New York NY 10154
     * Address is must and by default includeOffices is true , so need to add it
     */

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String) : RepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}