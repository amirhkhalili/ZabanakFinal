package ir.armaani.hv.zabanak.models;

import com.orm.SugarRecord;

/**
 * Created by Siamak on 06/07/2016.
 */
public class LearningMethodStep extends SugarRecord {
    private Integer currentStep;
    private Integer nextStep;
    private Integer daysToNextStep;
    private LearningMethod learningMethod;

    public LearningMethodStep() {
    }

    public LearningMethodStep(Integer currentStep, Integer nextStep, Integer daysToNextStep, LearningMethod learningMethod) {
        this.currentStep = currentStep;
        this.nextStep = nextStep;
        this.daysToNextStep = daysToNextStep;
        this.learningMethod = learningMethod;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public Integer getNextStep() {
        return nextStep;
    }

    public Integer getDaysToNextStep() {
        return daysToNextStep;
    }

    public LearningMethod getLearningMethod() {
        return learningMethod;
    }
}
