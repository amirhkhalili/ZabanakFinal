package ir.armaani.hv.zabanak.activities;

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

/**
 * Created by Amirhossein on 08/07/2016.
 */
public class SeriesListViewAdapter extends ArrayAdapter<Series> {

    private final Context context;
    private final List<Series> itemsArrayList;
    public SeriesListViewAdapter(Context context, List<Series> itemsArrayList) {

        super(context, R.layout.series_listview_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.series_listview_layout, parent, false);

        // 3. Get the two text view from the rowView
        ImageView seriesImage = (ImageView) rowView.findViewById(R.id.seriesImage);
        ImageView seriesImageBackground = (ImageView) rowView.findViewById(R.id.seriesImageBack);
        TextView seriesCaption = (TextView) rowView.findViewById(R.id.seriesCaptipn);
        TextView pakageCount = (TextView)rowView.findViewById(R.id.packageCount);

        // 4. Set the text for textView
        seriesImage.setImageBitmap(itemsArrayList.get(position).getImage());
        pakageCount.setText(itemsArrayList.get(position).getPackageCount().toString());
        seriesImageBackground.setImageBitmap(itemsArrayList.get(position).getImage());
        seriesCaption.setText(itemsArrayList.get(position).getCaption());

        // 5. retrn rowView
        return rowView;
    }
}

