/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Singleton;

import entity.Category;
import entity.Language;
import entity.Type;
import entity.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import stateless.CategoryControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewCatgeoryException;
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
    

    @PostConstruct
    public void pp() {
System.out.println("--------------------gjgjg-----------------------------------");
        if (categoryController.retrieveAllCategories() == null || categoryController.retrieveAllCategories().isEmpty()) {
            try {
System.out.println("IAMTRYINGTOINIT");
                userController.register(new User("John"));
                categoryController.createNewCategory(new Category("FFF", "FFF"));
                categoryController.createNewCategory(new Category("fff", "fff"));
                typeController.createNewType(new Type("www"));
                typeController.createNewType(new Type("wss"));
                languageController.createNewLanguage(new Language("wewew"));
                languageController.createNewLanguage(new Language("jjj"));
            } catch (Exception ex) {
                Logger.getLogger(DataInitBean.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

}
