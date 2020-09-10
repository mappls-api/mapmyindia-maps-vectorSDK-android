package com.mapmyindia.sdk.demo.java.model;

public class FeaturesList {

    private String featureTittle;
    private String featureDescription;

    public FeaturesList(String featureTittle, String featureDescription) {
        this.featureTittle=featureTittle;
        this.featureDescription=featureDescription;

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





}
