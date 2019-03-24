/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "searchExperienceManagedBean")
@ViewScoped
public class SearchExperienceManagedBean implements Serializable {

    private List<Experience> defaultExperiences;
    
    private Date searchDate;
    private String category;
    private String city;
    private Integer numOfPeople;
    
    private List<Experience> searchResults;
    private Experience selectedExperienceToView;
    
    public SearchExperienceManagedBean() {
        // generate dummy Experiences
        searchResults = new ArrayList();
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
        searchResults.add(new Experience("Experience one", "Desc1"));
    }
    
    @PostConstruct
    public void postConstruct() {
        
    }
    
    public void searchExperience(ActionEvent event) {
                
    }

    public void viewExperienceDetails(ActionEvent event) {
                
    }
    
    public List<Experience> getDefaultExperiences() {
        return defaultExperiences;
    }

    public void setDefaultExperiences(List<Experience> defaultExperiences) {
        this.defaultExperiences = defaultExperiences;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public List<Experience> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<Experience> searchResults) {
        this.searchResults = searchResults;
    }

    public Experience getSelectedExperienceToView() {
        return selectedExperienceToView;
    }

    public void setSelectedExperienceToView(Experience selectedExperienceToView) {
        this.selectedExperienceToView = selectedExperienceToView;
    }
    
}
