/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Evaluation;
import entity.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.EvaluationControllerLocal;
import stateless.UserControllerLocal;
import util.exception.InputDataValidationException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "accountManagementManagedBean")
@ViewScoped
public class AccountManagementManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userController;
    @EJB
    private EvaluationControllerLocal evaluationControllerLocal;
    
    private User currentUser;
    private Long currentUserId;
    
    private List<Evaluation> reviewsFromHosts;
    private List<Evaluation> reviewsFromGuests;
    private BigDecimal averageScoreFromHosts;
    private BigDecimal averageScoreFromGuests;
    private Integer numOfReviewsFromHosts;
    private Integer numOfReviewsFromGuests;
 
    public AccountManagementManagedBean() {
        averageScoreFromHosts = BigDecimal.ZERO;
        averageScoreFromGuests = BigDecimal.ZERO;
        reviewsFromHosts = new ArrayList();
        reviewsFromGuests = new ArrayList();
        numOfReviewsFromHosts = 0;
        numOfReviewsFromGuests = 0;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("******** AccountManagementManagedBean: postConstruct()");
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserId = currentUser.getUserId();
        retrieveEvaluations();
        System.out.println("    **** currentUser: username = " + currentUser.getUsername());
    }

    public void updateAccountInfo(ActionEvent event) {
        System.out.println("******** AccountManagementManagedBean: updateAccountInfo");

        try {
            userController.updateAccount(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account information updated successfully!", null));
        } catch (InputDataValidationException | UpdateUserException ex) {
            // revert back to the orginal user info
            try {
                currentUser = userController.retrieveUserById(currentUserId);
            } catch (UserNotFoundException ex2) {
                // there should not be any exception thrown
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    private void retrieveEvaluations() {
        if(currentUserId != null) {
            // retrieve reviews written by hosts and calculate the average score
            reviewsFromHosts = evaluationControllerLocal.retrieveAllEvaluationsFromHostsByUserId(currentUserId);
            BigDecimal sumScore = BigDecimal.ZERO;
            for(Evaluation reviewFromHost: reviewsFromHosts){
                sumScore.add(reviewFromHost.getScore());
            }
            if(reviewsFromHosts.size() != 0) {
                averageScoreFromHosts = sumScore.divide(new BigDecimal(reviewsFromHosts.size()), 1, RoundingMode.UP);
            } 
            numOfReviewsFromHosts = reviewsFromHosts.size();
            
            // retrieve reviews written by guests and calculate the average score
            reviewsFromGuests = evaluationControllerLocal.retrieveAllEvaluationsFromGuestsByUserId(currentUserId);
            sumScore = BigDecimal.ZERO;
            for(Evaluation reviewFromGuest: reviewsFromGuests){
                sumScore.add(reviewFromGuest.getScore());
            }
            if(reviewsFromGuests.size() != 0) {
                averageScoreFromGuests = sumScore.divide(new BigDecimal(reviewsFromGuests.size()), 1, RoundingMode.UP);
            }  
            numOfReviewsFromGuests = reviewsFromGuests.size();
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public List<Evaluation> getReviewsFromHosts() {
        return reviewsFromHosts;
    }

    public void setReviewsFromHosts(List<Evaluation> reviewsFromHosts) {
        this.reviewsFromHosts = reviewsFromHosts;
    }

    public List<Evaluation> getReviewsFromGuests() {
        return reviewsFromGuests;
    }

    public void setReviewsFromGuests(List<Evaluation> reviewsFromGuests) {
        this.reviewsFromGuests = reviewsFromGuests;
    }

    public BigDecimal getAverageScoreFromHosts() {
        return averageScoreFromHosts;
    }

    public void setAverageScoreFromHosts(BigDecimal averageScoreFromHosts) {
        this.averageScoreFromHosts = averageScoreFromHosts;
    }

    public BigDecimal getAverageScoreFromGuests() {
        return averageScoreFromGuests;
    }

    public void setAverageScoreFromGuests(BigDecimal averageScoreFromGuests) {
        this.averageScoreFromGuests = averageScoreFromGuests;
    }

    public Integer getNumOfReviewsFromHosts() {
        return numOfReviewsFromHosts;
    }

    public void setNumOfReviewsFromHosts(Integer numOfReviewsFromHosts) {
        this.numOfReviewsFromHosts = numOfReviewsFromHosts;
    }

    public Integer getNumOfReviewsFromGuests() {
        return numOfReviewsFromGuests;
    }

    public void setNumOfReviewsFromGuests(Integer numOfReviewsFromGuests) {
        this.numOfReviewsFromGuests = numOfReviewsFromGuests;
    }

}
