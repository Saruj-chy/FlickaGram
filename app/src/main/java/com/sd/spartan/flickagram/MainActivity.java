package com.sd.spartan.flickagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    public static List<FlickerModel> flickerModelList ;
    public static List<FlickerModel> flickerDBlList ;
    private FlickerAdapter flickerAdapter ;
    private  GridLayoutManager manager ;
    String url ;

    private int testLast = 0, lengthArray=0;
    private int totalItemCount, pastVisiblesItems,  visibleItemCount, page =1, previousTotal ;
    private boolean loading = true, netConnection ;

    private CountDownTimer mCountDownTimer, mRefTimer ;
    long mRemainingRefreshTime = 1000, mCountIntervalTime = 1500, mSwipeRefTime = 2000 ;

    private DatabaseHelperTest mDatabaseHelperTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        netConnection = NetConnectionNotify();
        FlickerApi flickerApi = new FlickerApi(this) ;
        url = flickerApi.constructInterestingPhotoListURL();

        progressBar = findViewById(R.id.progress) ;
        progressLoadData = findViewById(R.id.progress_load_data) ;
        recyclerView = findViewById(R.id.recycler) ;
        mDatabaseHelperTest = new DatabaseHelperTest(this) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        flickerModelList = new ArrayList<>();
        flickerDBlList = new ArrayList<>();

        LoadAdapter() ;
        if(netConnection){
            loadFlickerData();
        }else{
            LoadDBData() ;
        }



    }

    private void LoadAdapter() {
        flickerAdapter = new FlickerAdapter(getApplicationContext(), flickerModelList);
        recyclerView.setAdapter(flickerAdapter);
        manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    private void LoadDBData() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Cursor y = mDatabaseHelperTest.checkTable("all", 0+"") ;
        if (y.moveToFirst()) {
            while (true) {
                flickerModelList.add(
                        new FlickerModel(y.getString(1),
                                y.getString(2),
                                y.getString(3),
                                y.getString(4),
                                y.getString(5),
                                y.getString(6),
                                y.getString(7),
                                y.getString(8),
                                y.getString(9),
                                y.getString(10),
                                y.getString(11),
                                y.getString(12),
                                y.getString(13))
                );
                if (y.isLast())
                    break;
                y.moveToNext();
            }
        }
        flickerAdapter.notifyDataSetChanged();
    }

    private void loadFlickerData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FlickerApi.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flickerModelList.clear();
                        mDatabaseHelperTest.DeleteFlickaTbl();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject photosObj = object.getJSONObject("photos") ;
                            JSONArray jsonArray = photosObj.getJSONArray("photo") ;

                            lengthArray = jsonArray.length() ;

                            ForLoadMoreData(jsonArray, 0) ; //initial load data

                            flickerAdapter.notifyDataSetChanged();

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

                mDatabaseHelperTest.InsertFlickaTbl(
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
                        data.getString("width_h")) ;


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
        progressLoadData.setVisibility(View.VISIBLE);
        SetAutoRefresh(array);

    }

    private void SetAutoRefresh(JSONArray array){

        if(mSwipeRefTime <=0){
            if(mRefTimer != null){
                mRefTimer.cancel();
            }
            return;

        }
        if(mRefTimer == null){

            mRefTimer = new CountDownTimer(
                    mSwipeRefTime, 2500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    loading = true;
                    int j = testLast+1;

                    ForLoadMoreData(array, j); //  2nd load data
                }

                @Override
                public void onFinish() {
                    progressLoadData.setVisibility(View.GONE);
                    flickerAdapter.notifyDataSetChanged();
                    CancelRef() ;
                }
            };

            mRefTimer.start() ;
        }
    }
    private void CancelRef() {
        if(mRefTimer != null){
            mRefTimer.cancel();
            mRefTimer =null;
        }
    }


    public void OnCheck(View view) {
        flickerDBlList.clear();
        Cursor y = mDatabaseHelperTest.checkTable("all", 0+"") ;
        if (y.moveToFirst()) {
            while (true) {
                flickerDBlList.add(
                        new FlickerModel(y.getString(1),
                                y.getString(2),
                                y.getString(3),
                                y.getString(4),
                                y.getString(5),
                                y.getString(6),
                                y.getString(7),
                                y.getString(8),
                                y.getString(9),
                                y.getString(10),
                                y.getString(11),
                                y.getString(12),
                                y.getString(13))
                        );
                if (y.isLast())
                    break;
                y.moveToNext();
            }
        }

        Log.e("Tag", "list: "+ flickerDBlList.size() ) ;
    }
    public boolean NetConnectionNotify() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()){
            Log.e("Tag", "connect on: " ) ;
            return true ;
        }else{
            Log.e("Tag", "connect off: " ) ;
            Toast.makeText(this, "Please check your Net Connection", Toast.LENGTH_SHORT).show();
            return false ;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       netConnection = NetConnectionNotify();
    }
}