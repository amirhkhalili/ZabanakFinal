package ir.armaani.hv.zabanak.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.adapters.SeriesListViewAdapter;
import ir.armaani.hv.zabanak.models.Series;

/**
 * Created by Amirhossein on 15/07/2016.
 */
public class OfflineStoreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.online_store_fragment, container, false);
        View rootView = inflater.inflate(R.layout.offline_store_fragment, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.offline_listview);
        final SeriesListViewAdapter adapter = new SeriesListViewAdapter(super.getContext(), Series.getListOfSeries());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;

    }
}
