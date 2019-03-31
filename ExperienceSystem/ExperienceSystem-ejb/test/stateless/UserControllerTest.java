/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.User;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author samue
 */
public class UserControllerTest {

    private static UserControllerLocal userController;
    
    public UserControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        userController = lookupUserControllerLocal();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of register method, of class UserController.
     */
//    @Test
//    public void testRegister() throws Exception {
//        User object = new User();
//        object.setEmail("a@a.com");
//        object.setBirthday(Date.now());
//        object.setCreditCardDetails(new ArrayList<>());
//        object.setSelfIntro("ee");
//        object.setFirstName("dd");
//        object.setGender("ee");
//        object.setLastName("DD");
//        object.setUsername("name");
//        object.setPassword("a");
//        object.setPremium(false);
//        object.setPhoneNumber(Long.MIN_VALUE);
//        object.setPrefferedCurrency("FF");
//        User user = userController.register(object);
//        assertNotNull(user.getUserId());
//        assertEquals(user.getUsername(), "name");
//    }

//    /**
//     * Test of login method, of class UserController.
//     */
//    @Test
//    public void testLogin() throws Exception {
//        System.out.println("login");
//        String username = "";
//        String password = "";
//        UserController instance = new UserController();
//        User expResult = null;
//        User result = instance.login(username, password);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of followExperience method, of class UserController.
//     */
//    @Test
//    public void testFollowExperience() throws Exception {
//        System.out.println("followExperience");
//        Long id = null;
//        User user = null;
//        UserController instance = new UserController();
//        instance.followExperience(id, user);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of unfollowExperience method, of class UserController.
//     */
//    @Test
//    public void testUnfollowExperience() throws Exception {
//        System.out.println("unfollowExperience");
//        Long id = null;
//        User user = null;
//        UserController instance = new UserController();
//        instance.unfollowExperience(id, user);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveUserByUsername method, of class UserController.
//     */
//    @Test
//    public void testRetrieveUserByUsername() throws Exception {
//        System.out.println("retrieveUserByUsername");
//        String username = "";
//        UserController instance = new UserController();
//        User expResult = null;
//        User result = instance.retrieveUserByUsername(username);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveUserById method, of class UserController.
//     */
//    @Test
//    public void testRetrieveUserById() throws Exception {
//        System.out.println("retrieveUserById");
//        Long id = null;
//        UserController instance = new UserController();
//        User expResult = null;
//        User result = instance.retrieveUserById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveAllExperience method, of class UserController.
//     */
//    @Test
//    public void testRetrieveAllExperience() throws Exception {
//        System.out.println("retrieveAllExperience");
//        Long id = null;
//        UserController instance = new UserController();
//        List<Experience> expResult = null;
//        List<Experience> result = instance.retrieveAllExperience(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveAllUpcomingExperiences method, of class UserController.
//     */
//    @Test
//    public void testRetrieveAllUpcomingExperiences() throws Exception {
//        System.out.println("retrieveAllUpcomingExperiences");
//        Long id = null;
//        UserController instance = new UserController();
//        List<Experience> expResult = null;
//        List<Experience> result = instance.retrieveAllUpcomingExperiences(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveAllHostExperience method, of class UserController.
//     */
//    @Test
//    public void testRetrieveAllHostExperience() throws Exception {
//        System.out.println("retrieveAllHostExperience");
//        Long id = null;
//        UserController instance = new UserController();
//        List<ExperienceDate> expResult = null;
//        List<ExperienceDate> result = instance.retrieveAllHostExperience(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveAllUpcomingHostExperiences method, of class UserController.
//     */
//    @Test
//    public void testRetrieveAllUpcomingHostExperiences() throws Exception {
//        System.out.println("retrieveAllUpcomingHostExperiences");
//        Long id = null;
//        UserController instance = new UserController();
//        List<ExperienceDate> expResult = null;
//        List<ExperienceDate> result = instance.retrieveAllUpcomingHostExperiences(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createHostExperience method, of class UserController.
//     */
//    @Test
//    public void testCreateHostExperience() throws Exception {
//        System.out.println("createHostExperience");
//        Experience exp = null;
//        Long id = null;
//        UserController instance = new UserController();
//        instance.createHostExperience(exp, id);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteHostExperience method, of class UserController.
//     */
//    @Test
//    public void testDeleteHostExperience() throws Exception {
//        System.out.println("deleteHostExperience");
//        Long expId = null;
//        Long id = null;
//        String r = "";
//        UserController instance = new UserController();
//        instance.deleteHostExperience(expId, id, r);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteHostExperienceDate method, of class UserController.
//     */
//    @Test
//    public void testDeleteHostExperienceDate() throws Exception {
//        System.out.println("deleteHostExperienceDate");
//        Long expId = null;
//        Long id = null;
//        String r = "";
//        UserController instance = new UserController();
//        instance.deleteHostExperienceDate(expId, id, r);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    private static UserControllerLocal lookupUserControllerLocal() {
        try {
            Context c = new InitialContext();
            return (UserControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/UserController!stateless.UserControllerLocal");
        } catch (NamingException ne) {  
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
