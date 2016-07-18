package ir.armaani.hv.zabanak.rest;

import java.util.List;

import ir.armaani.hv.zabanak.rest.responses.Sentence;
import ir.armaani.hv.zabanak.rest.responses.Series;
import ir.armaani.hv.zabanak.rest.responses.SeriesSummary;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Siamak on 15/07/2016.
 */
public interface Api {
    @GET("series/list")
    Call<List<SeriesSummary>> getSeriesSummaries(@Query("p") Integer page, @Query("c") Integer count);

    @GET("series/search")
    Call<List<SeriesSummary>> searchSeries(@Query("s") String phrase, @Query("p") Integer page, @Query("c") Integer count);

    @GET("series/{seriesId}")
    Call<Series> getSeriesDetails(@Path("seriesId") Integer seriesId);

    @PUT("series/download/{seriesId}")
    Call<Void> updateDownloadCounter(@Path("seriesId") Integer seriesId);

    @GET("words/fa")
    Call<List<String>> getPersianMeaning(@Query("w") String phrase);

    @GET("words/en")
    Call<List<String>> getEnglishMeaning(@Query("w") String phrase);

    @GET("words/examples")
    Call<List<Sentence>> getExamples(@Query("w") String phrase);
}
