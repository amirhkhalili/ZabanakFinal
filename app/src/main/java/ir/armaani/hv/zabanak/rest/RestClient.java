package ir.armaani.hv.zabanak.rest;

import ir.armaani.hv.zabanak.App;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Siamak on 15/07/2016.
 */
public class RestClient {
    private static Api _api;
    private static final String API_BASE_URL = App.getManifestValue("WEB_SERVICE_URL");

    public static void init() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        Retrofit.Builder retrofitBuilder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.client(okHttpClientBuilder.build()).build();

        _api = retrofit.create(Api.class);
    }

    public static Api getApi() {
        return _api;
    }
}
