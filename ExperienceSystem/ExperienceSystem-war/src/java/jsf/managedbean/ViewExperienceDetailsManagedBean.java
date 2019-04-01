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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
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
    private BookingControllerLocal bookingController;

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;
    private Experience experienceEntityToView;
    private List<ExperienceDate> experienceDateEntities;
    private List<User> experienceFollowers;
    private ExperienceDate selectedExperienceDate;
    private Booking newBooking;
    private Boolean isFollowed;
    private BigDecimal price;
    private int numOfPeople;
    private Date selectedDate;
    private boolean availability;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private User currentUser;
    private Boolean isLogin;
    private List<String> images;
    
    /**
     * Creates a new instance of ViewExperienceDetailsManagedBean
     */
    public ViewExperienceDetailsManagedBean() {
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
    }
    
    @PostConstruct
    public void postConstruct(){
        experienceEntityToView = (Experience)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("experienceEntityToView");
        price = experienceController.getAveragePrice(experienceEntityToView);
        isLogin = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
        if(isLogin != null && isLogin){
            currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity");
        }
    }
    
    public void reserveExperienceDate(){
        try{
            bookingController.createNewBooking(newBooking);
        } catch(CreateNewBookingException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while entering your booking: " + ex.getMessage(), null));
        } catch(InputDataValidationException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have inputted wrong data for the booking: " + ex.getMessage(), null));
        } catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }
    
    public void checkDateAvailability(ActionEvent event){
        try
        {
            selectedExperienceDate = experienceController.checkExperienceDateAvailability(experienceEntityToView.getExperienceId(), selectedDate, numOfPeople);
            this.availability = true;
            this.subtotal = selectedExperienceDate.getPrice().multiply(new BigDecimal(this.numOfPeople));
            this.tax = subtotal.multiply(new BigDecimal(0.07));
            this.total = this.subtotal.add(this.tax);
            newBooking.setTotalPrice(total);
            newBooking.setExperienceDate(selectedExperienceDate);
            newBooking.setNumberOfPeople(numOfPeople);
            newBooking.setGuest(currentUser);
        }
        catch (ExperienceDateNotFoundException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            this.availability= false;
        }
        catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
            this.availability = false;
        }
    }
    
    public int minPrice(){
        // Calculate the current minPrice for this exp.
        return 50;
    }
    
    public void addFavoriteExperience(){
        experienceController.addFollowerToExperience(experienceEntityToView.getExperienceId(), (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity"));
        this.isFollowed = true;
    }
    
    public void removeFavoriteExperience(){
        experienceController.removeFollowerFromExperience(experienceEntityToView.getExperienceId(), (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity"));
        this.isFollowed = false;
    }
    
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Experience getExperienceEntityToView() {
        return experienceEntityToView;
    }

    public void setExperienceEntityToView(Experience experienceEntityToView) {
        this.experienceEntityToView = experienceEntityToView;
    }

    public List<ExperienceDate> getExperienceDateEntities() {
        return experienceDateEntities;
    }

    public void setExperienceDateEntities(List<ExperienceDate> experienceDateEntities) {
        this.experienceDateEntities = experienceDateEntities;
    }

    public List<User> getExperienceFollowers() {
        return experienceFollowers;
    }

    public void setExperienceFollowers(List<User> experienceFollowers) {
        this.experienceFollowers = experienceFollowers;
    }

    public Booking getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Booking newBooking) {
        this.newBooking = newBooking;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public ExperienceDate getSelectedExperienceDate() {
        return selectedExperienceDate;
    }

    public void setSelectedExperienceDate(ExperienceDate selectedExperienceDate) {
        this.selectedExperienceDate = selectedExperienceDate;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getCurrentUser() {
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
    
    
    
}
