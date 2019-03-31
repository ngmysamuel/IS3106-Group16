/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Asus
 */
@Entity
public class Experience implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;
    @NotNull
    private String title;
    private String description;
    private List<String> providingItems;
    private List<String> requiringItems;
    private String supportingDocument;
    // 0.0 - 5.0
    @NotNull
    private BigDecimal averageScore;
    @NotNull
    private String address;
    private List<String> reminders;
    // An experience is active only when it has experience dates that are happening currently or will happen in the future
    @NotNull
    private boolean active;
    
    private BigDecimal avgPrice;

    @ManyToOne
    private Type type;
    @ManyToOne
    private Location location;
    @ManyToOne
    private Language language;
    @OneToMany(mappedBy = "experience")
    private List<ExperienceDate> experienceDates = new ArrayList<>();
    @ManyToMany
    private List<User> followers;
    @ManyToOne
    private User host;
    @ManyToOne
    private Category category;
    
    private Date nextAvailDate;

    public Experience() {
    }

    public Experience(String title, String description) {
        this.title = title;
        this.description = description;
        // for initializing and testing purposes
        averageScore = new BigDecimal(0);
        address = "Kent Ridge Road";
        active = false;
    }
    
    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (experienceId != null ? experienceId.hashCode() : 0);
        return hash;
    }

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

    public String getSupportingDocument() {
        return supportingDocument;
    }

    public void setSupportingDocument(String supportingDocument) {
        this.supportingDocument = supportingDocument;
    }

    public List<String> getProvidingItems() {
        return providingItems;
    }

    public void setProvidingItems(List<String> providingItems) {
        this.providingItems = providingItems;
    }

    public List<String> getRequiringItems() {
        return requiringItems;
    }

    public void setRequiringItems(List<String> requiringItems) {
        this.requiringItems = requiringItems;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getReminders() {
        return reminders;
    }

    public void setReminders(List<String> reminders) {
        this.reminders = reminders;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<ExperienceDate> getExperienceDates() {
        for (ExperienceDate ed : experienceDates) {
        }
        return experienceDates;
    }

    public void setExperienceDates(List<ExperienceDate> experienceDates) {
        this.experienceDates = experienceDates;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the experienceId fields are not set
        if (!(object instanceof Experience)) {
            return false;
        }
        Experience other = (Experience) object;
        if ((this.experienceId == null && other.experienceId != null) || (this.experienceId != null && !this.experienceId.equals(other.experienceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Experience[ id=" + experienceId + " ]";
    }

    public Date getNextAvailDate() {
        if (getExperienceDates() == null) {
            return null;
        }
        Collections.sort(getExperienceDates());
        for (ExperienceDate ed : getExperienceDates()) {
            if (ed.isActive()) {
                nextAvailDate = ed.getStartDate();
            }
        }
        return nextAvailDate;
    }

    public void setNextAvailDate(Date nextAvailDate) {
        this.nextAvailDate = nextAvailDate;
    }

    public BigDecimal getAvgPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal count = BigDecimal.ZERO;
        if (!this.experienceDates.isEmpty()) {
            for (ExperienceDate ed : experienceDates) {
                count = count.add(BigDecimal.ONE);
                sum = sum.add(ed.getPrice());
            }
System.out.println(sum);
System.out.println("count: "+count);
            return sum.divide(count,2,RoundingMode.HALF_UP);
        }
        return null;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }
    
}
