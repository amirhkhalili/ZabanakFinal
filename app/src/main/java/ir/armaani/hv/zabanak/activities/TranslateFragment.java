package ir.armaani.hv.zabanak.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.armaani.hv.zabanak.R;


public class TranslateFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.translate_example_fragment, container, false);
        //View rootView = inflater.inflate(R.layout.translate_example_fragment, container, false);

       // return rootView;

    }
}
