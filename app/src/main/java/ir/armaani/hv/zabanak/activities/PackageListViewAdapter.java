package ir.armaani.hv.zabanak.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.models.Package;


public class PackageListViewAdapter extends ArrayAdapter<Package> {
    private final Context context;
    int seriesImage;


    private final List<Package> itemsArraylist;
    public PackageListViewAdapter(Context context,List<Package> itemsArraylist){
        super(context, R.layout.package_listview_layout, itemsArraylist);
        this.context=context;
        this.itemsArraylist= itemsArraylist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Package thisPackage = itemsArraylist.get(position);
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.package_listview_layout, parent, false);
        ImageView PackagePoster = (ImageView)rowView.findViewById(R.id.packagePoster);
        TextView PackageCaption = (TextView) rowView.findViewById(R.id.packageCaption);
        TextView PackageOkWord = (TextView) rowView.findViewById(R.id.okWordCounterText);
        TextView PackageNkWord = (TextView) rowView.findViewById(R.id.nkWordCounterText);
        TextView PackageID = (TextView) rowView.findViewById(R.id.packageIdText);
        TextView PackageExpireWord = (TextView) rowView.findViewById(R.id.expireWordCounterText);
        RelativeLayout PackageNoPoster = (RelativeLayout) rowView.findViewById(R.id.ifNoPosterRelativeLayout);
        RelativeLayout PackageLock = (RelativeLayout) rowView.findViewById(R.id.packageLockRelativeLayout);
        RelativeLayout PackageExpireLayout = (RelativeLayout)rowView.findViewById(R.id.expireWordLayout);
        RelativeLayout PackageOKLayout = (RelativeLayout)rowView.findViewById(R.id.PackageokLayout);
        RelativeLayout PackageNKLayout = (RelativeLayout)rowView.findViewById(R.id.PackagenkLayout);


        if(thisPackage.getImageFile()==null){
            PackagePoster.setImageBitmap(thisPackage.getSeries().getImage());
            PackageNoPoster.setVisibility(View.VISIBLE);
            PackageID.setText(String.valueOf(position));
        }else {
            PackageNoPoster.setVisibility(View.GONE);
            PackagePoster.setImageBitmap(thisPackage.getImage());
        }
        if(thisPackage.getCountOfExpiredWords()<1){
            PackageExpireLayout.setVisibility(View.GONE);
        }else{
            PackageExpireLayout.setVisibility(View.VISIBLE);
        }
        PackageCaption.setText(String.valueOf(thisPackage.getCaption()));
        PackageExpireWord.setText(String.valueOf(thisPackage.getCountOfExpiredWords()));
        PackageOkWord.setText(String.valueOf(thisPackage.getCountOfLearnedWords()));
        PackageNkWord.setText(String.valueOf(thisPackage.getCountOfTodayWords()));
        if (!thisPackage.canLearningBeStarted()){
            PackageLock.setVisibility(View.VISIBLE);
            PackageExpireLayout.setBackgroundResource(R.drawable.dark_desable);
            PackageNKLayout.setBackgroundResource(R.drawable.dark_desable);
            PackageOKLayout.setBackgroundResource(R.drawable.dark_desable);
        }else{
            PackageLock.setVisibility(View.GONE);

        }

        return rowView;
    }

}
