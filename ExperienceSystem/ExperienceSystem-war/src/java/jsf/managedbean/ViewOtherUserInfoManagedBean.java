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
import util.exception.UserNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "viewOtherUserInfoManagedBean")
@ViewScoped
public class ViewOtherUserInfoManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userControllerLocal;
    @EJB
    private EvaluationControllerLocal evaluationControllerLocal;
    
    private User currentUser;
    private Long userIdToView;
    private User userToView;
    private Boolean isFollowedByMe;
    private Boolean isFollowingMe;
    private Boolean isBlockedByMe;
    
    private String previousPage;
    
    private List<Evaluation> reviewsFromHosts;
    private List<Evaluation> reviewsFromGuests;
    private BigDecimal averageScoreFromHosts;
    private BigDecimal averageScoreFromGuests;
    private Integer numOfReviewsFromHosts;
    private Integer numOfReviewsFromGuests;
    
    // If this user is blocking the current user, the page should be prevented from being shown in the first place
    public ViewOtherUserInfoManagedBean() {
        isFollowedByMe = false;
        isFollowingMe = false;
        isBlockedByMe = false;
        
        averageScoreFromHosts = BigDecimal.ZERO;
        averageScoreFromGuests = BigDecimal.ZERO;
        reviewsFromHosts = new ArrayList();
        reviewsFromGuests = new ArrayList();
        numOfReviewsFromHosts = 0;
        numOfReviewsFromGuests = 0;
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("******** ViewOtherUserInfoManagedBean: postConstruct()");
        if(FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey("userIdToView")) {
            userIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("userIdToView");
            previousPage = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("previousPage");
            System.out.println("**** userIdToView: " + userIdToView);
            try {
                userToView = userControllerLocal.retrieveUserById(userIdToView);
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser")) {
                    currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
                    System.out.println("**** currentUser: " + currentUser.getUserId());
                    if (currentUser.getFollows().contains(userToView)) {
                        isFollowedByMe = true;
                    }
                    if (currentUser.getFollowers().contains(userToView)) {
                        isFollowingMe = true;
                    }
                    if (currentUser.getBlocks().contains(userToView)) {
                        isBlockedByMe = true;
                    }
                }
                retrieveEvaluations();
            } catch(UserNotFoundException ex){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
        if(FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey("previousPage")) {
            previousPage = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("previousPage");
        }
        System.out.println("-----------------------------");
    }
    
    public void followUser(ActionEvent event) {
        System.out.println("******** ViewOtherUserInfoManagedBean: followUser()");
        if(currentUser != null && userToView != null) {
            try {
                userControllerLocal.followUser(currentUser.getUserId(), userToView.getUserId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Follow this user successfully!", null));
                currentUser.getFollows().add(userToView);
                isFollowedByMe = true;
            } catch (UserNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        } else {
            System.out.println("Current user of the user to view is null!");
        }
        System.out.println("-----------------------------");
    }
    
    public void unfollowUser(ActionEvent event) {
        System.out.println("******** ViewOtherUserInfoManagedBean: unfollowUser()");
        if(currentUser != null && userToView != null) {
            try {
                userControllerLocal.unfollowUser(currentUser.getUserId(), userToView.getUserId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unfollow this user successfully!", null));
                currentUser.getFollows().remove(userToView);
                isFollowedByMe = false;
            } catch (UserNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
    }
    
    public void blockUser(ActionEvent event) {
        if(currentUser != null && userToView != null) {
            try {
                userControllerLocal.blockUser(currentUser.getUserId(), userToView.getUserId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Block this user successfully!", null));
                currentUser.getBlocks().add(userToView);
                isBlockedByMe = true;
            } catch (UserNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
    }
    
    public void unblockUser(ActionEvent event) {
        if(currentUser != null && userToView != null) {
            try {
                userControllerLocal.unblockUser(currentUser.getUserId(), userToView.getUserId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unblock this user successfully!", null));
                currentUser.getBlocks().remove(userToView);
                isBlockedByMe = false;
            } catch (UserNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
    }

    private void retrieveEvaluations() {
        System.out.println("******** ViewOtherUserInfoManagedBean: retrieveEvaluations()");
        if(userIdToView != null) {
            // retrieve reviews written by hosts and calculate the average score
            reviewsFromHosts = evaluationControllerLocal.retrieveAllEvaluationsFromHostsByUserId(userIdToView);
            System.out.println("**** reviewsFromHosts.size: " + reviewsFromHosts.size());
            BigDecimal sumScore = BigDecimal.ZERO;
            for(Evaluation reviewFromHost: reviewsFromHosts){
                sumScore = sumScore.add(reviewFromHost.getScore());
            }
            if(reviewsFromHosts.size() != 0) {    
                averageScoreFromHosts = sumScore.divide(new BigDecimal(reviewsFromHosts.size()), 1, RoundingMode.UP);
                System.out.println("**** averageScoreFromHosts: " + averageScoreFromHosts);
            } 
            numOfReviewsFromHosts = reviewsFromHosts.size();
            
            // retrieve reviews written by guests and calculate the average score
            reviewsFromGuests = evaluationControllerLocal.retrieveAllEvaluationsFromGuestsByUserId(userIdToView);
            sumScore = BigDecimal.ZERO;
            for(Evaluation reviewFromGuest: reviewsFromGuests){
                sumScore = sumScore.add(reviewFromGuest.getScore());
            }
            if(reviewsFromGuests.size() != 0) {
                averageScoreFromGuests = sumScore.divide(new BigDecimal(reviewsFromGuests.size()), 1, RoundingMode.UP);
            }  
            numOfReviewsFromGuests = reviewsFromGuests.size();
        }
    }
    
    public Long getUserIdToView() {
        return userIdToView;
    }

    public void setUserIdToView(Long userIdToView) {
        this.userIdToView = userIdToView;
    }

    public User getUserToView() {
        return userToView;
    }

    public void setUserToView(User userToView) {
        this.userToView = userToView;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Boolean getIsFollowedByMe() {
        return isFollowedByMe;
    }

    public void setIsFollowedByMe(Boolean isFollowedByMe) {
        this.isFollowedByMe = isFollowedByMe;
    }

    public Boolean getIsFollowingMe() {
        return isFollowingMe;
    }

    public void setIsFollowingMe(Boolean isFollowingMe) {
        this.isFollowingMe = isFollowingMe;
    }

    public Boolean getIsBlockedByMe() {
        return isBlockedByMe;
    }

    public void setIsBlockedByMe(Boolean isBlockedByMe) {
        this.isBlockedByMe = isBlockedByMe;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
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
