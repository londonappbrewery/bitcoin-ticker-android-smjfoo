package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin","" + adapterView.getItemAtPosition(i));

                String params = "BTC"+adapterView.getItemAtPosition(i);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing Selected");
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String params) {

        AsyncHttpClient client = new AsyncHttpClient();
        // Since the BitCoin site doesn't use the same key = value structure, I will combine the BASE_URL with the params.
        // params is just a concatenation of BTC and the selected Currency from the Spinner.
        String SEARCH_URL = BASE_URL + params;
        client.get(SEARCH_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                Log.d("Bitcoin","JSON: " + responseBody.toString());

                BitCoinDataModel BitCoinData = BitCoinDataModel.fromJson(responseBody);
                updateUI(BitCoinData);
            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, JSONObject responseBody, Throwable e) {
//                Log.d("BitCoin", "Request fail! Status code: " + statusCode);
//                Log.d("BitCoin", "Fail response: " + responseBody);
//            }
        });

    }

    private void updateUI(BitCoinDataModel BitCoin){
        mPriceTextView.setText(BitCoin.getAskBitCoin());
    }

}
