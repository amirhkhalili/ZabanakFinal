package ir.armaani.hv.zabanak.rest.responses;

import android.graphics.Bitmap;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.helpers.ImageDownloader;

/**
 * Created by Siamak on 15/07/2016.
 */
public class SeriesSummary {
    public Integer id;
    public Integer download_count;
    public String name;
    public String image;
    public String provider;

    public String getImageURL() {
        return App.getManifestValue("IMAGES_URL") + image;
    }

    public Bitmap getImage() {
        return ImageDownloader.downloadImageOnTheFly(image);
    }
}
