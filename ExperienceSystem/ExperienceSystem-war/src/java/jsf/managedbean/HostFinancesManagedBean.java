/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import stateless.UserControllerLocal;

@Named(value = "hostFinancesManagedBean")
@ViewScoped
public class HostFinancesManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userController;

    private LineChartModel lineModel1;
    private User currentUser;
    
    private int numOfExpHosted;
    private int numOfExpAttended;
     
    public HostFinancesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        createLineModel();
        currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity");
    }
    
    public int numOfExpHosted() {
        int i = userController.retrieveAllExperience(currentUser.getUserId()).size();
        return i;
    }
    
    
 
    private void createLineModel() {
        lineModel1 = initLinearModel();
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
    }
    
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
 
        model.addSeries(series1);
 
        return model;
    }
    
    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public int getNumOfExpHosted() {
        
        return numOfExpHosted;
    }

    public void setNumOfExpHosted(int numOfExpHosted) {
        this.numOfExpHosted = numOfExpHosted;
    }

    public int getNumOfExpAttended() {
        return numOfExpAttended;
    }

    public void setNumOfExpAttended(int numOfExpAttended) {
        this.numOfExpAttended = numOfExpAttended;
    }
    
    
}
