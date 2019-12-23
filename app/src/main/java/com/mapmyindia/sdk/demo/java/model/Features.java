package com.mapmyindia.sdk.demo.java.model;

/**
 * Created by CEINFO on 26-02-2019.
 */
public class Features {

    private int featureID;
    private String featureTittle;
    private Class featureActivityName;
    private String featureDescription;


    public Features(int featureId, String featureTittle, Class featureActivityName, String featureDescription) {
        this.featureID = featureId;
        this.featureTittle = featureTittle;
        this.featureActivityName = featureActivityName;
        this.featureDescription = featureDescription;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public String getFeatureTittle() {
        return featureTittle;
    }

    public void setFeatureTittle(String featureTittle) {
        this.featureTittle = featureTittle;
    }


    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public Class getFeatureActivityName() {
        return featureActivityName;
    }

    public void setFeatureActivityName(Class featureActivityName) {
        this.featureActivityName = featureActivityName;
    }
}
