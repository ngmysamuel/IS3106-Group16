/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Singleton;

import entity.Category;
import entity.Experience;
import entity.ExperienceDate;
import entity.Language;
import entity.Location;
import entity.Type;
import entity.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import stateless.CategoryControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Asus
 */
@Singleton
@LocalBean
@Startup
public class DataInitBean {

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;
    @EJB
    private UserControllerLocal userController;
    @EJB
    private LanguageControllerLocal languageController;
    @EJB
    private TypeControllerLocal typeController;
    @EJB
    private CategoryControllerLocal categoryController;
    @EJB
    private LocationControllerLocal locationController;

    @PostConstruct
    public void postConstruct() {
        System.out.println("******** DataInitBean: postConstruct() ********");
        
        if(userController.retrieveAllUsers() == null || userController.retrieveAllUsers().isEmpty()) {
            try {
                System.out.println("    **** initializing users");
                
                User user1 = new User();
                user1.setEmail("yuqian@gmail.com");
                user1.setUsername("yuqian");
                user1.setPassword("password");
                userController.register(user1);
                User user2 = new User();
                user2.setEmail("ruichun@gmail.com");
                user2.setUsername("ruichun");
                user2.setPassword("password");
                userController.register(user2);
            } catch (InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if (categoryController.retrieveAllCategories() == null || categoryController.retrieveAllCategories().isEmpty()) {
            try {
                System.out.println("    **** initializing categories");

                Category category1 = new Category();
                category1.setName("Music");
                category1.setDescription("Anything about music, from concerts to workshops!");
                categoryController.createNewCategory(category1);
                Category category2 = new Category();
                category2.setName("Sports");
                category2.setDescription("No profession reqired!");
                categoryController.createNewCategory(category2);
                Category category3 = new Category();
                category3.setName("Foodie");
                category3.setDescription("Cook or eat, whatever it is!");
                categoryController.createNewCategory(category3);
                Category category4 = new Category();
                category4.setName("Culture");
                category4.setDescription("Full of explorations!");
                categoryController.createNewCategory(category4);
                Category category5 = new Category();
                category5.setName("Arts");
                category5.setDescription("Cater to your artsy need!");
                categoryController.createNewCategory(category5);
                Category category6 = new Category();
                category6.setName("Landmark");
                category6.setDescription("No profession reqired!");
                categoryController.createNewCategory(category6);
                Category category7 = new Category();
                category7.setName("Entertainment");
                category7.setDescription("Just wanna have fun!");
                categoryController.createNewCategory(category7);
                Category category8 = new Category();
                category8.setName("Health&Well being");
                category8.setDescription("More self-care, more self-love!");
                categoryController.createNewCategory(category8);
                Category category9 = new Category();
                category9.setName("Others");
                category9.setDescription("All you can imagine!");
                categoryController.createNewCategory(category9);

            } catch (InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if (languageController.retrieveAllLanguages() == null || languageController.retrieveAllLanguages().isEmpty()) {
            try {
                System.out.println("    **** initializing languages");

                Language language1 = new Language();
                language1.setName("English");
                languageController.createNewLanguage(language1);
                Language language2 = new Language();
                language2.setName("Chinese");
                languageController.createNewLanguage(language2);
                Language language3 = new Language();
                language3.setName("Malay");
                languageController.createNewLanguage(language3);
                Language language4 = new Language();
                language4.setName("Tamil");
                languageController.createNewLanguage(language4);
                Language language5 = new Language();
                language5.setName("Indonesian");
                languageController.createNewLanguage(language5);
                
            } catch (InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if (typeController.retrieveAllTypes() == null || typeController.retrieveAllTypes().isEmpty()) {
            try {
                System.out.println("    **** initializing types");

                Type type1 = new Type();
                type1.setName("hangout");
                typeController.createNewType(type1);
                Type type2 = new Type();
                type2.setName("workshop");
                typeController.createNewType(type2);
                Type type3 = new Type();
                type3.setName("sharing session");
                typeController.createNewType(type3);
                Type type4 = new Type();
                type4.setName("outdoor");
                typeController.createNewType(type4);
                
            } catch (InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if (locationController.retrieveAllLocations() == null || locationController.retrieveAllLocations().isEmpty()) {
            try {
                System.out.println("    **** initializing locations");

                Location location1 = new Location();
                location1.setName("North-East");
                locationController.createNewLocation(location1);
                Location location2 = new Location();
                location2.setName("North");
                locationController.createNewLocation(location2);
                Location location3 = new Location();
                location3.setName("Central");
                locationController.createNewLocation(location3);
                Location location4 = new Location();
                location4.setName("West");
                locationController.createNewLocation(location4);
                Location location5 = new Location();
                location5.setName("East");
                locationController.createNewLocation(location5);
                
            } catch (InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if(experienceController.retrieveAllExperiences() == null || experienceController.retrieveAllExperiences().isEmpty()){
            try{
                System.out.println("********** Initializing Experiences");
                Experience exp1 = new Experience("Pasta Cooking", "Italian pasta-making training.");
                Location l1 = locationController.retrieveLocationById(new Long(1));
                exp1.setLocation(l1);
                Type t2 = typeController.retrieveTypeById(new Long(2));
                exp1.setType(t2);
                Category c1 = categoryController.retrieveCategoryById(new Long(1));
                exp1.setCategory(c1);
                Language la1 = languageController.retrieveLanguageById(new Long(1));
                exp1.setLanguage(la1);
                User u1 = userController.retrieveUserById(new Long(1));
                exp1.setHost(u1);
                exp1.setAddress("Address");
                experienceController.createNewExperience(exp1);
                l1.getExperiences().add(exp1);
                t2.getExperiences().add(exp1);
                c1.getExperiences().add(exp1);
                la1.getExperiences().add(exp1);
                u1.getExperienceHosted().add(exp1);
                
                Experience exp2 = new Experience("Museum Trip", "NUS Museum");
                exp2.setLocation(l1);
                Type t1 = typeController.retrieveTypeById(new Long(1));
                exp2.setType(t1);
                Category c4 = categoryController.retrieveCategoryById(new Long(4));
                exp2.setCategory(c4);
                exp2.setLanguage(la1);
                exp2.setLocation(l1);
                exp2.setAddress("Address2");
                User u2 = userController.retrieveUserById(new Long(2));
                exp2.setHost(u2);
                experienceController.createNewExperience(exp2);
                l1.getExperiences().add(exp2);
                t1.getExperiences().add(exp2);
                c4.getExperiences().add(exp2);
                la1.getExperiences().add(exp2);
                u2.getExperienceHosted().add(exp2);
            }
            catch (CreateNewExperienceException | InputDataValidationException ex){
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            }
            catch (Exception ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
        
        if(experienceDateController.retrieveAllExperienceDates().isEmpty()){
            try{
                Experience e1 = experienceController.retrieveExperienceById(new Long(1));
                ExperienceDate ed1 = new ExperienceDate();
                ed1.setExperience(e1);
                ed1.setCapacity(10);
                ed1.setPrice(new BigDecimal(67));
                ed1.setSpotsAvailable(10);
                ed1.setStartDate(new Date(118,10,30,9,30,27));
                experienceDateController.createNewExperienceDate(ed1);
                e1.getExperienceDates().add(ed1);
                
                ExperienceDate ed2 = new ExperienceDate();
                ed2.setExperience(e1);
                ed2.setCapacity(12);
                ed2.setPrice(new BigDecimal(70.3));
                ed2.setSpotsAvailable(12);
                ed2.setStartDate(new Date(118,11,1,9,30,27));
                experienceDateController.createNewExperienceDate(ed2);
                e1.getExperienceDates().add(ed2);
                
                Experience e2 = experienceController.retrieveExperienceById(new Long(2));
                ExperienceDate ed3 = new ExperienceDate();
                ed3.setExperience(e2);
                ed3.setCapacity(5);
                ed3.setPrice(new BigDecimal(30.30));
                ed3.setSpotsAvailable(5);
                ed3.setStartDate(new Date(118,11,1,10,32,27));
                experienceDateController.createNewExperienceDate(ed3);
                e2.getExperienceDates().add(ed3);
            } catch (ExperienceNotFoundException | CreateNewExperienceDateException | InputDataValidationException ex) {
                System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
            } catch (Exception ex){
                System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
            }
        }
    }

}
