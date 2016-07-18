package ir.armaani.hv.zabanak.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.TranslateActivity;
import ir.armaani.hv.zabanak.activities.adapters.TranslateExampleListAdapter;
import ir.armaani.hv.zabanak.activities.adapters.TranslateListAdapter;
import ir.armaani.hv.zabanak.models.Word;
import ir.armaani.hv.zabanak.rest.RestClient;
import ir.armaani.hv.zabanak.rest.responses.Sentence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TranslateFragment extends Fragment {
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.translate_example_fragment, container, false);
        View rootView = inflater.inflate(R.layout.translate_example_fragment, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView_translate);
        Word wordItem = ((TranslateActivity)getActivity()).getWordItem();

        Call<List<String>> call1 = RestClient.getApi().getPersianMeaning(wordItem.getWord());
        call1.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> result = response.body();
                TranslateListAdapter translateListAdapter = new TranslateListAdapter(App.getContext(),result);
                listView.setAdapter(translateListAdapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        Call<List<String>> call2 = RestClient.getApi().getEnglishMeaning(wordItem.getWord());
        call2.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> result = response.body();
                TranslateListAdapter translateListAdapter = new TranslateListAdapter(App.getContext(),result);
                listView.setAdapter(translateListAdapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        Call<List<Sentence>> call3 = RestClient.getApi().getExamples(wordItem.getWord());
        call3.enqueue(new Callback<List<Sentence>>() {
            @Override
            public void onResponse(Call<List<Sentence>> call, Response<List<Sentence>> response) {
                List<Sentence> result = response.body();
                TranslateExampleListAdapter translateListAdapter = new TranslateExampleListAdapter(App.getContext(),result);
                listView.setAdapter(translateListAdapter);

            }

            @Override
            public void onFailure(Call<List<Sentence>> call, Throwable t) {

            }
        });

        return rootView;

    }
}
