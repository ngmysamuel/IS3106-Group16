/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.BookingControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.CreateNewBookingException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Asus
 */
@Named(value = "viewExperienceDetailsManagedBean")
@ViewScoped
public class ViewExperienceDetailsManagedBean implements Serializable{

    @EJB
    private BookingControllerLocal bookingControllerLocal;
    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    // session info
    private User currentUser;
    private Boolean isLogin;
    
    // basic Experience details information
    private Experience experience;
    private Boolean isExperienceFavouratedByThisUser;
    private List<User> experienceFollowers;
    private List<String> images;
    
    // booking information
    private Long selectedExperienceDateIdToBook;
    private Integer numOfPeopleForBooking;
    private ExperienceDate selectedExperienceDate;
    private Booking newBooking;
    
    // checkout information  
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    public ViewExperienceDetailsManagedBean() {
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
        
        isExperienceFavouratedByThisUser = false;
        numOfPeopleForBooking = 1;
        newBooking = new Booking();
    }
    
    @PostConstruct
    public void postConstruct(){
        // the experience passed in the session map only contains the experience date listings that are already filtered
        System.out.println("******** ViewExperienceDetailsManagedBean: postConstruct()");
        experience = (Experience)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("experienceToView");
        System.out.println("**** experience: " + experience.getTitle());
        
        isLogin = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
        if(isLogin != null && isLogin){
            currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            if(currentUser.getFollowedExperiences().contains(experience)){
                isExperienceFavouratedByThisUser = true;
            }
        }
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("selectedExperienceDateIdToBook")) {
            selectedExperienceDateIdToBook = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedExperienceDateIdToBook");
            System.out.println("**** selectedExperienceDateIdToBook: " + selectedExperienceDateIdToBook);
        }
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("numOfPeopleForBooking")) {
            numOfPeopleForBooking = (Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("numOfPeopleForBooking");
        }
        System.out.println("-----------------------------");
    }
    
    public void proceedToCheckOut(ActionEvent event){
        System.out.println("******** ViewExperienceDetailsManagedBean: proceedToCheckOut()");
        try {
            selectedExperienceDate = experienceDateControllerLocal.retrieveExperienceDateByExperienceDateId(selectedExperienceDateIdToBook);
            System.out.println("**** selectedExperienceDate ID: " + selectedExperienceDate.getExperienceDateId());
            
            subtotal = selectedExperienceDate.getPrice().multiply(new BigDecimal(numOfPeopleForBooking));
            subtotal.setScale(2, RoundingMode.HALF_UP);
            System.out.println("**** subtotal: " + subtotal);
            tax = subtotal.multiply(new BigDecimal(0.07));
            tax.setScale(2, RoundingMode.HALF_UP);
            System.out.println("**** tax: " + tax);
            total = subtotal.add(tax);
            System.out.println("**** total: " + total);
            
            newBooking.setTotalPrice(total);
            newBooking.setNumberOfPeople(numOfPeopleForBooking);
            newBooking.setGuest(currentUser);
        } catch (ExperienceDateNotFoundException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
        System.out.println("-----------------------------");
    }
    
    public void checkout(ActionEvent event) {
        System.out.println("******** ViewExperienceDetailsManagedBean: checkout()");
        System.out.println("-----------------------------");
        try {
            newBooking.setBookingDate(new Date());
            newBooking.setExperienceDate(selectedExperienceDate);
            newBooking = bookingControllerLocal.createNewBooking(newBooking);          
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your booking has been made successfully! Booking ID: " + newBooking.getBookingId(), null));
        } catch (CreateNewBookingException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured in your booking: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }
    
    public void initializeBookingState() {
        System.out.println("******** ViewExperienceDetailsManagedBean: initializeBookingState()");
        System.out.println("-----------------------------");
        newBooking = new Booking();
        numOfPeopleForBooking = 1;
        selectedExperienceDateIdToBook = null;
    }
    
    public void addFavoriteExperience(ActionEvent event){
        System.out.println("******** ViewExperienceDetailsManagedBean: addFavoriteExperience()");
        configureCurrentUser();
        experienceControllerLocal.addFollowerToExperience(experience.getExperienceId(), currentUser.getUserId());
        isExperienceFavouratedByThisUser = true;
    }
    
    public void removeFavoriteExperience(ActionEvent event){
        experienceControllerLocal.removeFollowerFromExperience(experience.getExperienceId(), currentUser.getUserId());
        isExperienceFavouratedByThisUser = false;
    }
    
    // Only when there is a user currently logged in and s/he is the host of the current experience will give a redirect to the personal account info page
    // other conditions all lead to the view other user info page
    public String viewOtherUserInfo() {
        System.out.println("******** ViewExperienceDetailsManagedBean: viewOtherUserInfo()");
        System.out.println("-----------------------------");
        configureCurrentUser();
        if(currentUser != null && currentUser.getUserId().equals(experience.getHost().getUserId())) {
            return "accountInfo.xhtml";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("userIdToView", experience.getHost().getUserId());
        }
        return "viewOtherUserInfo.xhtml";
    }
    
    public void configureCurrentUser() {
        isLogin = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
        if(isLogin != null && isLogin){
            currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            if(currentUser.getFollowedExperiences().contains(experience)){
                isExperienceFavouratedByThisUser = true;
            }
        }
    }
    
    public String convertDateToString(Date date) {
        if(date != null) {
            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");   
            return formatter.format(date);
        } else {
            return null;
        } 
    }
    
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public List<User> getExperienceFollowers() {
        return experienceFollowers;
    }

    public void setExperienceFollowers(List<User> experienceFollowers) {
        this.experienceFollowers = experienceFollowers;
    }

    public ExperienceDate getSelectedExperienceDate() {
        return selectedExperienceDate;
    }

    public void setSelectedExperienceDate(ExperienceDate selectedExperienceDate) {
        this.selectedExperienceDate = selectedExperienceDate;
    }

    public Booking getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Booking newBooking) {
        this.newBooking = newBooking;
    }

    public Long getSelectedExperienceDateIdToBook() {
        return selectedExperienceDateIdToBook;
    }

    public void setSelectedExperienceDateIdToBook(Long selectedExperienceDateIdToBook) {
        System.out.println("******** ViewExperienceDetailsManagedBean: setSelectedExperienceDateIdToBook()");
        try {
            selectedExperienceDate = experienceDateControllerLocal.retrieveExperienceDateByExperienceDateId(selectedExperienceDateIdToBook);
        } catch(ExperienceDateNotFoundException ex) {
            
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedExperienceDateIdToBook", selectedExperienceDateIdToBook);
        this.selectedExperienceDateIdToBook = selectedExperienceDateIdToBook;
    }

    public Integer getNumOfPeopleForBooking() {
        return numOfPeopleForBooking;
    }

    public void setNumOfPeopleForBooking(Integer numOfPeopleForBooking) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("numOfPeopleForBooking", numOfPeopleForBooking);
        this.numOfPeopleForBooking = numOfPeopleForBooking;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
        newBooking.setTotalPrice(total);
    }

    public User getCurrentUser() {
        configureCurrentUser();
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Boolean getIsExperienceFavouratedByThisUser() {
        return isExperienceFavouratedByThisUser;
    }

    public void setIsExperienceFavouratedByThisUser(Boolean isExperienceFavouratedByThisUser) {
        this.isExperienceFavouratedByThisUser = isExperienceFavouratedByThisUser;
    }
    
}
