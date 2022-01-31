package com.sd.spartan.flickagram.config;


public class FlickerApi {
    static final String API_KEY= "e346ad38094bff99f7bfd88815bf5fef" ;
    public static String BASE_URL = "https://api.flickr.com/services/rest?method=flickr.interestingness.getList&api_key="+API_KEY
            +"&format=json&nojsoncallback=1&extras=date_taken,url_h" ;

}
