package ir.armaani.hv.zabanak.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Amirhossein on 08/07/2016.
 */
public class SanseBoldTextview extends TextView {

    public SanseBoldTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SanseBoldTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SanseBoldTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/IRANSansMobile(FaNum)_Medium.ttf");
        setTypeface(tf ,1);

    }

}