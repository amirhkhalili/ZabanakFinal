package ir.armaani.hv.zabanak.activities.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.PakageActivity;
import ir.armaani.hv.zabanak.activities.adapters.OnlineStoreFragmentAdapter;
import ir.armaani.hv.zabanak.models.Series;
import ir.armaani.hv.zabanak.rest.RestClient;
import ir.armaani.hv.zabanak.rest.responses.SeriesSummary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amirhossein on 15/07/2016.
 */
public class OnlineStoreFragment extends Fragment {
    EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.online_store_fragment, container, false);
        final View rootView = inflater.inflate(R.layout.online_store_fragment, container, false);
        final ListView listView = (ListView)rootView.findViewById(R.id.online_listview);
        Call<List<SeriesSummary>> call = RestClient.getApi().getSeriesSummaries(null , null);
        call.enqueue(new Callback<List<SeriesSummary>>() {
            @Override
            public void onResponse(Call<List<SeriesSummary>> call, Response<List<SeriesSummary>> response) {
                List<SeriesSummary> result = response.body();
                final OnlineStoreFragmentAdapter adapter = new OnlineStoreFragmentAdapter(App.getContext(), result);
                listView.setAdapter(adapter);
                editText = (EditText) rootView.findViewById(R.id.SearchBox);
                editText.setHint("جستجو کنید ...");
                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            return true;
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<SeriesSummary>> call, Throwable t) {
                Toast.makeText(App.getContext(),"اتصال به اینترنت خود را بررسی نمایید.",Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                SeriesSummary seriesSummary = (SeriesSummary) parent.getAdapter().getItem(position);
                Call<ir.armaani.hv.zabanak.rest.responses.Series> call = RestClient.getApi().getSeriesDetails(seriesSummary.id);
                call.enqueue(new Callback<ir.armaani.hv.zabanak.rest.responses.Series>() {
                    @Override
                    public void onResponse(Call<ir.armaani.hv.zabanak.rest.responses.Series> call, Response<ir.armaani.hv.zabanak.rest.responses.Series> response) {
                        ir.armaani.hv.zabanak.rest.responses.Series series = response.body();

                        new AsyncTask<ir.armaani.hv.zabanak.rest.responses.Series, Void, Void>() {

                            @Override
                            protected Void doInBackground(ir.armaani.hv.zabanak.rest.responses.Series... params) {
                                Series newSeries1 = new Series();
                                if (newSeries1.addNewSeriesFromServer(params[0])) {
                                    Intent myIntent = new Intent(   getActivity(), PakageActivity.class);
                                    myIntent.putExtra("series", newSeries1); //Optional parameters
                                    myIntent.putExtra("seriesId" , newSeries1.getId());
                                    getActivity().startActivity(myIntent);
                                }else{
                                    Toast.makeText(App.getContext(),"اتصال به اینترنت خود را بررسی نمایید.",Toast.LENGTH_LONG).show();
                                }
                                return null;
                            }
                        }.execute(series);

                    }

                    @Override
                    public void onFailure(Call<ir.armaani.hv.zabanak.rest.responses.Series> call, Throwable t) {
                        Toast.makeText(App.getContext(),"اتصال به اینترنت خود را بررسی نمایید.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        return rootView;

    }
}
