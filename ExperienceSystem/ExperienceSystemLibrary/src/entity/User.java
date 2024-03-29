/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus
 */
@Entity
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @NotNull
    @Size(min=6)
    private String username;
    @NotNull
    @Size(min = 8, max = 32)
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String gender;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @NotNull
    private String email;
    private Long phoneNumber;
    private String prefferedCurrency;
    private List<String> creditCardDetails;
    private BigDecimal averageHostScore;
    private BigDecimal averageAttendScore;
    private String selfIntro;
    @NotNull
    private Boolean premium; 
    @OneToOne
    private Language preferredLanguage;
    
    @ManyToMany
    @JoinTable(name="BLOCK_USER", 
            joinColumns=@JoinColumn(name="BLOCKER_ID", referencedColumnName="USERID"),
            inverseJoinColumns=@JoinColumn(name="BLOCK_ID", referencedColumnName="USERID"))
    private List<User> blocks; // Other users blocked by this user
    @ManyToMany
    @JoinTable(name="FOLLOW_USER", 
            joinColumns=@JoinColumn(name="FOLLOWER_ID", referencedColumnName="USERID"),
            inverseJoinColumns=@JoinColumn(name="FOLLOW_ID", referencedColumnName="USERID"))
    private List<User> follows; // Follow other users  
    @ManyToMany(mappedBy = "follows")
    private List<User> followers;
    @ManyToMany(mappedBy = "blocks")
    private List<User> blockers; // Users who blocked this user.
    @OneToMany(mappedBy = "recipient")
    private List<Message> messagesReplied;
    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;
    @ManyToMany(mappedBy = "users")
    private List<Notification> notifications;
    
    @OneToMany(mappedBy = "guest")
    private List<Booking> bookings;
    @OneToMany(mappedBy = "user")
    private List<Appeal> appeals;
    
    @OneToMany(mappedBy = "host")
    private List<Experience> experienceHosted;
    @ManyToMany(mappedBy = "followers")
    private List<Experience> followedExperiences;
 
    private List<Evaluation> evaluationsForUserAsGuest;
    private List<Evaluation> evaluationsForUserAsHost;

    public User() {
        // for initializing and testing purposes
        firstName = "User";
        lastName = "Name";
        premium = false;
        creditCardDetails = new ArrayList();
        blocks = new ArrayList();
        follows = new ArrayList();
        evaluationsForUserAsGuest = new ArrayList();
        evaluationsForUserAsHost = new ArrayList();
        bookings = new ArrayList();
        followers = new ArrayList();
        blockers = new ArrayList();
        messagesReplied = new ArrayList();
        messagesSent = new ArrayList();
        notifications = new ArrayList();
        experienceHosted = new ArrayList();
        followedExperiences = new ArrayList();
        appeals = new ArrayList();
    }

    @XmlTransient
    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @XmlTransient
    public List<User> getBlockers() {
        return blockers;
    }

    public void setBlockers(List<User> blockers) {
        this.blockers = blockers;
    }

    @XmlTransient
    public List<Message> getMessagesReplied() {
        return messagesReplied;
    }

    public void setMessagesReplied(List<Message> messagesReplied) {
        this.messagesReplied = messagesReplied;
    }

    @XmlTransient
    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    @XmlTransient
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @XmlTransient
    public List<Experience> getExperienceHosted() {
        return experienceHosted;
    }

    public void setExperienceHosted(List<Experience> experienceHosted) {
        this.experienceHosted = experienceHosted;
    }

    @XmlTransient
    public List<Experience> getFollowedExperiences() {
        return followedExperiences;
    }

    public void setFollowedExperiences(List<Experience> followedExperiences) {
        this.followedExperiences = followedExperiences;
    }

    @XmlTransient
    public List<Appeal> getAppeals() {
        return appeals;
    }

    public void setAppeals(List<Appeal> appeals) {
        this.appeals = appeals;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getPrefferedCurrency() {
        return prefferedCurrency;
    }

    public void setPrefferedCurrency(String prefferedCurrency) {
        this.prefferedCurrency = prefferedCurrency;
    }

    public List<String> getCreditCardDetails() {
        return creditCardDetails;
    }

    public void setCreditCardDetails(List<String> creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    public BigDecimal getAverageHostScore() {
        return averageHostScore;
    }

    public void setAverageHostScore(BigDecimal averageHostScore) {
        this.averageHostScore = averageHostScore;
    }

    public BigDecimal getAverageAttendScore() {
        return averageAttendScore;
    }

    public void setAverageAttendScore(BigDecimal averageAttendScore) {
        this.averageAttendScore = averageAttendScore;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Language getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(Language preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @XmlTransient
    public List<User> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<User> blocks) {
        this.blocks = blocks;
    }

    @XmlTransient
    public List<User> getFollows() {
        return follows;
    }

    public void setFollows(List<User> follows) {
        this.follows = follows;
    }

    public List<Evaluation> getEvaluationsForUserAsGuest() {
        return evaluationsForUserAsGuest;
    }

    public void setEvaluationsForUserAsGuest(List<Evaluation> evaluationsForUserAsGuest) {
        this.evaluationsForUserAsGuest = evaluationsForUserAsGuest;
    }

    public List<Evaluation> getEvaluationsForUserAsHost() {
        return evaluationsForUserAsHost;
    }

    public void setEvaluationsForUserAsHost(List<Evaluation> evaluationsForUserAsHost) {
        this.evaluationsForUserAsHost = evaluationsForUserAsHost;
    }

    @XmlTransient
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userId fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + userId + " ]";
    }
    
}
