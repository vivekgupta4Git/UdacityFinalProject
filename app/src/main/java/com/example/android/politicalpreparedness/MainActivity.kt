package com.example.android.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/*Response from api civic information
    my api key :AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs
    Query : - https://www.googleapis.com/civicinfo/v2/elections?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs
    endpoint : - elections
 */

/*
{
  "elections": [
    {
      "id": "2000",
      "name": "VIP Test Election",
      "electionDay": "2025-06-06",
      "ocdDivisionId": "ocd-division/country:us"
    },
    {
      "id": "7166",
      "name": "Louisiana Special General Election",
      "electionDay": "2022-01-15",
      "ocdDivisionId": "ocd-division/country:us/state:la"
    },
    {
      "id": "7169",
      "name": "California Special Election",
      "electionDay": "2022-02-15",
      "ocdDivisionId": "ocd-division/country:us/state:ca"
    }
  ],
  "kind": "civicinfo#electionsQueryResponse"
}
 */