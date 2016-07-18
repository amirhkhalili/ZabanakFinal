package ir.armaani.hv.zabanak.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.orm.SugarRecord;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.exceptions.AlreadyStartedLearningException;
import ir.armaani.hv.zabanak.exceptions.DependedPackageNotLearnedYetException;
import ir.armaani.hv.zabanak.exceptions.SomeWordsNotLearnedYetException;

/**
 * Created by Siamak on 06/07/2016.
 */
public class Package extends SugarRecord implements Serializable {
    //-------------------------------- SERVER ATTRIBUTES -------------------------------------------
    private Integer serverId;
    private String caption;
    private Integer wordsCount;
    private Integer dependOnPackage;
    private String imageFile;
    private Series series;
    //-------------------------------- LOCAL ATTRIBUTES --------------------------------------------
    private Date dateOfLearningStart;
    private Date dateOfLearningEnd;

    public Package() {
    }

    public Package(Integer serverId, String caption, Integer wordsCount, Integer dependOnPackage, String imageFile, Series series) {
        this.serverId = serverId;
        this.caption = caption;
        this.wordsCount = wordsCount;
        this.dependOnPackage = dependOnPackage;
        this.imageFile = imageFile;
        this.series = series;
    }

    public Boolean addNewPackageFromServer(ir.armaani.hv.zabanak.rest.responses.Package serverPackage , Series series) {
        this.serverId = serverPackage.id;
        this.caption = serverPackage.caption;
        this.dependOnPackage  = serverPackage.depend_on_package;
        this.imageFile = serverPackage.image;
        this.series = series;
        this.save();
        for (ir.armaani.hv.zabanak.rest.responses.Word word:
                serverPackage.words) {
            new Word().addNewWordFromServer(word , this);
        }
        return true;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getCaption() {
        return caption;
    }

    public Integer getWordsCount() {
        return getWords().size();
    }

    public Integer getDependOnPackage() {
        return dependOnPackage;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getImageURL() {
        return App.getManifestValue("IMAGES_URL") + imageFile;
    }

    public String getImagePath() {
        return Environment.getExternalStorageDirectory()+ "/" + App.getManifestValue("IMAGE_STORAGE_DIR") + "/" + imageFile;
    }

    public Bitmap getImage() {
        File imgFile = new File(getImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;
        }else{
            return null;
        }
    }

    public Series getSeries() {
        return series;
    }

    public List<Word> getWords() {
        return Word.find(Word.class, "A_PACKAGE = ?", getId().toString());
    }

    //----------------------------------------------------------------------------------------------

    public Date getDateOfLearningStart() {
        return dateOfLearningStart;
    }

    public Date getDateOfLearningEnd() {
        return dateOfLearningEnd;
    }

    public Integer getExpiredWordsCount() {
        Long expiredWordsCount = Word.count(Word.class, "A_PACKAGE = ? AND NEXT_REVIEW_DATE IS NULL AND NEXT_REVIEW_DATE < ?", new String[]{getId().toString(), new LocalDate().toString()});
        return expiredWordsCount.intValue();
    }

    public Integer getTodayWordsCount() {
        Long todayWordsCount = Word.count(Word.class, "A_PACKAGE = ? AND NEXT_REVIEW_DATE = ?", new String[]{getId().toString(), new LocalDate().toString()});
        return todayWordsCount.intValue();
    }

    public Integer getLearnedWordsCount() {
        Long learnedWordsCount = Word.count(Word.class, "A_PACKAGE = ? AND IS_LEARNED = ?", new String[]{getId().toString(), new Boolean(true).toString()});
        return learnedWordsCount.intValue();
    }

    public void resetAllExpiredWords() {
        List<Word> words = Word.find(Word.class, "A_PACKAGE = ? AND NEXT_REVIEW_DATE IS NOT NULL AND NEXT_REVIEW_DATE < ?", getId().toString(), new LocalDate().toString());
        for (Word word : words)
            word.resetLearning();
    }

    public List<Word> getTodayWords() {
        return Word.find(Word.class, "A_PACKAGE = ? AND NEXT_REVIEW_DATE = ?", getId().toString(), new LocalDate().toString());
    }

    public void startLearning() throws AlreadyStartedLearningException, DependedPackageNotLearnedYetException {
        if (dateOfLearningStart == null) {
            if (isOpenToLearning() != true)
                throw new DependedPackageNotLearnedYetException();
            List<Word> words = Word.find(Word.class, "A_PACKAGE = ?", getId().toString());
            for (Word word : words)
                word.resetLearning();
            dateOfLearningStart = new LocalDate().toDate();
        } else {
            throw new AlreadyStartedLearningException();
        }
    }

    public void tryToFinishLearning() throws SomeWordsNotLearnedYetException {
        if (getLearnedWordsCount() == getWordsCount()) {
            dateOfLearningEnd = new LocalDate().toDate();
        } else {
            throw new SomeWordsNotLearnedYetException();
        }
    }

    public Boolean isLearningFinished() {
        if (dateOfLearningEnd == null)
            return false;
        return true;
    }

    public Boolean isOpenToLearning() {
        if (dependOnPackage != null) {
            List<Package> packages = Package.find(Package.class, "SERVER_ID = ?", dependOnPackage.toString());
            if (packages.size() == 1) {
                Package aPackage = packages.get(0);
                if (aPackage.isLearningFinished() == false)
                    return false;
            }
        }
        return true;
    }
}