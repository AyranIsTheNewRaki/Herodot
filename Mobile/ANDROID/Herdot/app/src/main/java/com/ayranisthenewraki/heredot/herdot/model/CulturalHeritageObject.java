package com.ayranisthenewraki.heredot.herdot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idilgun on 06/05/17.
 */

public class CulturalHeritageObject {

    private String title;
    private String description;
    private String category;
    private List<String> timeLocations;
    private String imageUrl;
    private List<TimeLocationCouple> actualTimeLocations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTimeLocations() {
        return timeLocations;
    }

    public void setTimeLocations(List<String> timeLocations) {
        this.timeLocations = timeLocations;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<TimeLocationCouple> getActualTimeLocations() {
        return actualTimeLocations;
    }

    public void setActualTimeLocations(List<TimeLocationCouple> actualTimeLocations) {
        this.actualTimeLocations = actualTimeLocations;
    }
}
