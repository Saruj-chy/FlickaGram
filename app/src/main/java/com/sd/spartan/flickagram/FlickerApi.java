package com.sd.spartan.flickagram;

import android.util.Log;

public class FlickerApi {


    static final String API_KEY= "e346ad38094bff99f7bfd88815bf5fef" ;
    static final String TAG= "FlickaGame" ;
    public static String BASE_URL = "https://api.flickr.com/services/rest?method=flickr.interestingness.getList&api_key="+API_KEY
            +"&format=json&nojsoncallback=1&extras=date_taken,url_h" ;

    MainActivity mainActivity ;

    public FlickerApi(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void fetchInterestingPhotos(){
        String url = constructInterestingPhotoListURL() ;

        Log.e("TAG", "url:"+url) ;
    }

    public String constructInterestingPhotoListURL() {
        String url = BASE_URL ;
        url += "?method=flickr.interestingness.getList" ;
        url += "&api_key="+API_KEY ;
        url += "&format=json" ;
        url += "&nojsoncallback=1";
        url += "&extras=date_taken,url_h" ;



        return url;
    }

}
