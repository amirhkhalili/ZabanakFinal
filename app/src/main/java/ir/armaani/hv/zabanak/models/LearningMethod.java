package ir.armaani.hv.zabanak.models;

import com.orm.SugarRecord;

/**
 * Created by Siamak on 06/07/2016.
 */
public class LearningMethod extends SugarRecord {
    private String caption;

    public LearningMethod() {
    }

    public LearningMethod(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
