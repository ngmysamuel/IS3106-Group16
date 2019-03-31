/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Singleton;

import entity.Category;
import entity.Language;
import entity.Location;
import entity.Type;
import entity.User;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import stateless.CategoryControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
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
    }

}
