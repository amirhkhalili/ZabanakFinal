package ir.armaani.hv.zabanak.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.models.Series;
import ir.armaani.hv.zabanak.rest.responses.Sentence;

/**
 * Created by Siamak on 18/07/2016.
 */
public class TranslateExampleListAdapter extends ArrayAdapter<Sentence> {

    private final Context context;
    private final List<Sentence> itemsArrayList;
    public TranslateExampleListAdapter(Context context, List<Sentence> itemsArrayList) {

        super(context, R.layout.series_listview_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.translate_listview_example_layout, parent, false);

        TextView enExample = (TextView) rowView.findViewById(R.id.enExample);
        TextView faExample = (TextView) rowView.findViewById(R.id.faExample);

        enExample.setText(itemsArrayList.get(position).sentence);
        faExample.setText(itemsArrayList.get(position).meaning);

        return rowView;
    }
}