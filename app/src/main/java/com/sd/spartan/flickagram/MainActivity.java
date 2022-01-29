package com.sd.spartan.flickagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar, progressLoadData ;
    private RecyclerView recyclerView ;
    private List<FlickerModel> flickerModelList ;
    private FlickerAdapter flickerAdapter ;
    private  GridLayoutManager manager ;
    String url ;

    private int testLast = 0, lengthArray=0;
    private int totalItemCount, pastVisiblesItems,  visibleItemCount, page =1, previousTotal ;
    private boolean loading = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FlickerApi flickerApi = new FlickerApi(this) ;
        url = flickerApi.constructInterestingPhotoListURL();

        progressBar = findViewById(R.id.progress) ;
        progressLoadData = findViewById(R.id.progress_load_data) ;
        recyclerView = findViewById(R.id.recycler) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        flickerModelList = new ArrayList<>();

        loadFlickerData(0);

    }
    private void loadFlickerData(final int number) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FlickerApi.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.e("TAGs", "response: "+response );
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject photosObj = object.getJSONObject("photos") ;
                            JSONArray jsonArray = photosObj.getJSONArray("photo") ;

                            lengthArray = jsonArray.length() ;

                            ForLoadMoreData(jsonArray, number) ; //initial load data

                            flickerAdapter = new FlickerAdapter(getApplicationContext(), flickerModelList);
                            recyclerView.setAdapter(flickerAdapter);
                            manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);

                            loadMoreData(jsonArray) ;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAGs", "error: "+error );
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void ForLoadMoreData(JSONArray array, int number) {
        progressLoadData.setVisibility(View.GONE);
        if(number==lengthArray){
            return;
        }

        for (int i = number; i < number+ 10 ; i++) {
            testLast = i ;

            JSONObject data = null;
            try {
                data = array.getJSONObject(i);

                flickerModelList.add(new FlickerModel(
                        data.getString("id"),
                        data.getString("owner"),
                        data.getString("secret"),
                        data.getString("server"),
                        data.getString("farm"),
                        data.getString("title"),
                        data.getString("ispublic"),
                        data.getString("datetaken"),
                        data.getString("datetakengranularity"),
                        data.getString("datetakenunknown"),
                        data.getString("url_h"),
                        data.getString("height_h"),
                        data.getString("width_h")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void loadMoreData(final JSONArray array) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-1) {
                            loading = false;
//                            progressbar.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadNextDataFromApi(array);
                                }
                            }, 1000);

                        }
                    }
                }
            }
        });
    }

    public void loadNextDataFromApi(JSONArray array) {
        loading = true;
        int j = testLast+1;
        progressLoadData.setVisibility(View.VISIBLE);
        ForLoadMoreData(array, j); //  2nd --- final load data

        flickerAdapter.notifyDataSetChanged();
    }


}