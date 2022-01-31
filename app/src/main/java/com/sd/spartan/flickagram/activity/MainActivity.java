package com.sd.spartan.flickagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sd.spartan.flickagram.R;
import com.sd.spartan.flickagram.adapter.FlickerAdapter;
import com.sd.spartan.flickagram.config.FlickerApi;
import com.sd.spartan.flickagram.local_db.DatabaseHelperTest;
import com.sd.spartan.flickagram.model.FlickerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar, progressLoadData ;
    private RecyclerView recyclerView ;
    public static List<FlickerModel> flickerModelList ;
//    public static List<FlickerModel> flickerDBlList ;
    private FlickerAdapter flickerAdapter ;
    private  GridLayoutManager gridLayoutManager;

    private int testLast = 0, lengthArray=0;
    private int totalItemCount, pastVisiblesItems,  visibleItemCount ;
    private boolean loading = true, netConnection ;

    private CountDownTimer mRefTimer ;
    long mSwipeRefTime = 2000, countDownInterval = 2500, delayMillis= 1000 ;

    private DatabaseHelperTest mDatabaseHelperTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        netConnection = NetConnectionNotify();
        Initialize() ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        flickerDBlList = new ArrayList<>();

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
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @SuppressLint("NotifyDataSetChanged")
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
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, FlickerApi.BASE_URL,
                response -> {
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
                },
                error -> {
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void ForLoadMoreData(JSONArray array, int number) {
        if(flickerModelList.size()==lengthArray){
            return;
        }
        for (int i = number; i < number+ 10 ; i++) {
            testLast = i ;

            JSONObject data;
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
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-1) {
                            loading = false;
                            new Handler().postDelayed(() ->
                                    loadNextDataFromApi(array), delayMillis);

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
                    mSwipeRefTime, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    loading = true;
                    int j = testLast+1;

                    ForLoadMoreData(array, j); //  2nd load data
                }

                @SuppressLint("NotifyDataSetChanged")
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



    public boolean NetConnectionNotify() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()){
            return true ;
        }else{
            Toast.makeText(this, "Please check your Net Connection", Toast.LENGTH_SHORT).show();
            return false ;
        }
    }

    private void Initialize() {
        progressBar = findViewById(R.id.progress) ;
        progressLoadData = findViewById(R.id.progress_load_data) ;
        recyclerView = findViewById(R.id.recycler) ;

        flickerModelList = new ArrayList<>();
        mDatabaseHelperTest = new DatabaseHelperTest(this) ;
    }
    @Override
    protected void onStart() {
        super.onStart();
       netConnection = NetConnectionNotify();
    }
}