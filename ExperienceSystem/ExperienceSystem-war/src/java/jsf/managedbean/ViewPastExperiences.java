/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Evaluation;
import entity.Experience;
import entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.BookingControllerLocal;
import stateless.EvaluationControllerLocal;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewPastExperiences")
@RequestScoped
public class ViewPastExperiences {

    @EJB
    private BookingControllerLocal bookingController;

    @EJB
    private EvaluationControllerLocal evaluationController;

    @EJB
    private UserControllerLocal userController;
    

    private List<Experience> ls;
    
    private User currentUser;
    
    public ViewPastExperiences() {
    }
    
    public void eval(ActionEvent event) {
        Experience e = (Experience) event.getComponent().getAttributes().get("eval");
        String remark = (String) event.getComponent().getAttributes().get("remark");
        BigDecimal score = (BigDecimal) event.getComponent().getAttributes().get("score");
        Long experienceId = e.getExperienceId();
        for (Booking b : currentUser.getBookings()) {
            Experience eb = b.getExperienceDate().getExperience();
            if (eb.getExperienceId().equals(experienceId)) {
                if (b.getEvaluation() == null) {
                    Evaluation eval = new Evaluation();
                    eval.setBooking(b);
                    eval.setEvaluationTime(LocalDate.now());
                    eval.setRemark(remark);
                    eval.setScore(score);
                    eval.setUser(currentUser);
                    evaluationController.create(eval);
                    b.setEvaluation(eval);
                    bookingController.update(b);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have alr eval", null));
                }
            }
        }
    }

    public List<Experience> getLs() {
        List<Booking> ls2 = getCurrentUser().getBookings();
        List<Experience> masterLs = new LinkedList<>();
        for (Booking b : ls2) {
            LocalDate d = b.getExperienceDate().getStartDate();
            if (d.isBefore(LocalDate.now())) {
                continue;
            }
            masterLs.add(b.getExperienceDate().getExperience());
        }
        return masterLs;
    }

    public void setLs(List<Experience> ls) {
        this.ls = ls;
    }

    public User getCurrentUser() {
        User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustEntity");
        try {
            u = userController.retrieveUserByUsername(u.getUsername());
        } catch (UserNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "wrong", null));
        }
        return u;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
}
