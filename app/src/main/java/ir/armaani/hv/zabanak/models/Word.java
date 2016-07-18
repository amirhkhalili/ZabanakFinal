package ir.armaani.hv.zabanak.models;

import com.orm.SugarRecord;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Date;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.exceptions.DoesNotStartedAlreadyException;
import ir.armaani.hv.zabanak.exceptions.SomeWordsNotLearnedYetException;

/**
 * Created by Siamak on 06/07/2016.
 */
public class Word extends SugarRecord implements Serializable{
    //-------------------------------- SERVER ATTRIBUTES -------------------------------------------
    private Integer serverId;
    private String word;
    private String translate;
    private String movie;
    private Integer playTime;
    private Integer stopTime;
    private Package aPackage;
    //-------------------------------- LOCAL ATTRIBUTES --------------------------------------------
    private Date nextReviewDate;
    private Integer currentLevel = 1;
    private Boolean isLearned = false;

    public Word() {
    }

    public Word(Integer serverId, String word, String translate, String movie, Integer playTime, Integer stopTime, Package aPackage) {
        this.serverId = serverId;
        this.word = word;
        this.translate = translate;
        this.movie = movie;
        this.playTime = playTime;
        this.stopTime = stopTime;
        this.aPackage = aPackage;
    }

    public Boolean addNewWordFromServer(ir.armaani.hv.zabanak.rest.responses.Word serverWord , Package aPackage) {
        this.serverId = serverWord.id;
        this.word = serverWord.word;
        this.translate = serverWord.translate;
        this.movie = serverWord.movie;
        this.playTime = serverWord.play_time;
        this.stopTime = serverWord.stop_time;
        this.aPackage = aPackage;
        this.save();
        return true;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getWord() {
        return word;
    }

    public String getTranslate() {
        return translate;
    }

    public String getMovie() {
        return movie;
    }

    public String getMovieURL() {
        return App.getManifestValue("VIDEOS_URL") + getMovie().substring(0 , getMovie().length() - 4) + "/stream.mpd";
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public Package getaPackage() {
        return aPackage;
    }

    //----------------------------------------------------------------------------------------------
    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public Boolean getLearned() {
        return isLearned;
    }

    public void resetLearning() {
        currentLevel = 1;
        nextReviewDate = new LocalDate().plusDays(1).toDate();
        save();
    }

    public void doSuccess() throws DoesNotStartedAlreadyException {
        if (nextReviewDate != null) {
            LearningMethod learningMethod = getaPackage().getSeries().getLearningMethod();
            LearningMethodStep learningMethodStep = LearningMethodStep.find(LearningMethodStep.class, "LEARNING_METHOD = ? AND CURRENT_STEP = ?", learningMethod.getId().toString(), currentLevel.toString()).get(0);
            Integer nextStep = learningMethodStep.getNextStep();
            if (nextStep == 0) {
                isLearned = true;
                save();
                try {
                    getaPackage().tryToFinishLearning();
                } catch (SomeWordsNotLearnedYetException e) {
                    //
                }
            } else {
                DateTime newNextReviewDate = new DateTime(getNextReviewDate());
                nextReviewDate = newNextReviewDate.plusDays(learningMethodStep.getDaysToNextStep()).toDate();
                currentLevel = learningMethodStep.getNextStep();
                save();
            }
        } else {
            throw new DoesNotStartedAlreadyException();
        }
    }

    public void doFailure() throws DoesNotStartedAlreadyException {
        if (nextReviewDate != null) {
            resetLearning();
        } else {
            throw new DoesNotStartedAlreadyException();
        }
    }


}
