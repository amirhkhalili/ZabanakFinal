package ir.armaani.hv.zabanak.helpers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ir.armaani.hv.zabanak.App;

/**
 * Created by Siamak on 15/07/2016.
 */
public class ImageDownloader {

    private static final String IMAGES_URL = App.getManifestValue("IMAGES_URL");

    public static Boolean downloadImage(String imageId) {
        Bitmap image;
        try {
            image = Picasso.with(App.getContext()).load(IMAGES_URL + imageId).get();
        } catch (IOException e) {
            return false;
        }

        String imageStorageDir = App.getManifestValue("IMAGE_STORAGE_DIR");
        File storageDirectory = new File(Environment.getExternalStorageDirectory(), imageStorageDir);
        if (!storageDirectory.exists())
            if (!storageDirectory.mkdirs())
                return false;
        File file = new File(storageDirectory + "/" + imageId);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
            ostream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Bitmap downloadImageOnTheFly(String imageId) {
        Bitmap image;
        try {
            image = Picasso.with(App.getContext()).load(IMAGES_URL + imageId).get();
        } catch (IOException e) {
            return null;
        }
        return image;
    }

    public static void downloadImageOnTheFly(String imageId, ImageView imageView) {
        Picasso.with(App.getContext()).load(IMAGES_URL + imageId).into(imageView);
    }
}
