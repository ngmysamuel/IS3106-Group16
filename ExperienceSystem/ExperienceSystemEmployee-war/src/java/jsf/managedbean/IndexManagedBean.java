/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {
    
    private Date searchDate;
    private Integer searchNumOfPeople;

    public IndexManagedBean() {

    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public Integer getSearchNumOfPeople() {
        return searchNumOfPeople;
    }

    public void setSearchNumOfPeople(Integer searchNumOfPeople) {
        this.searchNumOfPeople = searchNumOfPeople;
    }
    

    
}
