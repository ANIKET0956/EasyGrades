package com.example.aniket.easygrades.test;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).<br>
 *
 * <b>Testing Plan:</b>
 * We divided the application testing into two phases : Development Testing and User Testing.
 * We skipped the Release Testing because of the unavailablity of independent team to verify the software.
 *
 *
 * @see <a href="TestingDoc.html">Testing documentation</a>
 */
public class TestingModule {

    /**
     * <b>Unit Test</b><br>
     * We have an automated script for checking this module. We have given as input a list of users with all the credentials.
     * Output of each test case is compared against the predefined/expected output of the test case.
     */
    @Test
    public void authenticationTest(String[] users, String[] trueOutputs) throws Exception {
        for (int i=0; i<users.length; i++){
            String output = performAuthentication(users[i]);
            assertEquals(trueOutputs[i],output);
        }
    }

    /**
     * <b>Unit Test</b><br>
     * This checks the display of the user profile on the client.
     * Test code hardcode a user details (in order to avoid communication with the server).
     * Output is an xml file which can be seen in the third fragment of the homepage.
     * @param userDetails
     */

    @Test
    public void userProfileTest(String[] userDetails, File trueLayout){
        File outputLayout = generateUserProfile(userDetails);
        String trueData = trueLayout.toString();
        String testData = outputLayout.toString();
        assertEquals(trueData, testData);
    }

    /**
     * <b>Unit Test</b><br>
     * Input : an XML with a list of courses.
     * Output : XML layout file which can be seen in the first fragment of the homepage.
     * @param courseListXML
     */
    @Test
    public void courseFragmentTest(File courseListXML, File trueLayout){
        String coursesSTR = courseListXML.toString();
        String[] courses = coursesSTR.split(",");
        File outputLayout = generateCoursesLayout(courses);
        String trueData = trueLayout.toString();
        String testData = outputLayout.toString();
        assertEquals(trueData, testData);
    }

    /**
     * <b>Unit Test</b><br>
     * Input : User details (which imitates the requirement of the user)<br>
     * Output : XML layout file which can be seen in the first fragment of the homepage.
     * @param userDetails
     */
    @Test
    public void CRSinputFragmentTest(String userDetails, String trueFeed){
        String testFeed = generateCRSfeed(userDetails);
        assertEquals(trueFeed, testFeed);
    }

    /**
     * <b>Unit Test</b><br>
     * Input : an XML with a list of all the notes uploaded in a page.
     * Output : XML layout file which can be seen in the first fragment of the homepage.
     * @param notesListXML
     */
    @Test
    public void CourseHomePage(File notesListXML, File trueLayout){
        String coursesSTR = notesListXML.toString();
        String[] courses = coursesSTR.split(",");
        File outputLayout = generateCoursePageLayout(courses);
        String trueData = trueLayout.toString();
        String testData = outputLayout.toString();
        assertEquals(trueData, testData);
    }

    /**
     * <b>Component Test</b><br>
     * Fetches all the courses that are already stored in the database of the server.
     * @param trueLayout
     */
    @Test
    public void fetchCoursesFromServerTest(File trueLayout){
        String coursesSTR = fetchCourses();
        File courseListXML = new File(coursesSTR);
        courseFragmentTest(courseListXML, trueLayout);
    }


    /**
     * <b>Component Test</b><br>
     * Fetches all the courses that are output from Course Recommendation System and are
     * displayed on the screen
     * @param userDetails
     * @param trueLayout
     */
    @Test
    public void getRecoFromCRSTest(String userDetails, File trueLayout){
        String testFeed = generateCRSfeed(userDetails);
        String[] courses = fetchCRSoutput(testFeed);
        File courseListXML = new File(courses.toString());
        courseFragmentTest(courseListXML, trueLayout);
    }

    /**
     * <b>Component Test</b><br>
     * Check upload and download functionality of the system.
     * @param filename
     */
    @Test
    public void uploadDownloadTest(String filename){
        File fileToUpload = new File(filename);
        uploadToServer(fileToUpload);
        File fileDownloaded = downloadFromServer(filename);
        assertEquals(fileToUpload.toString(), fileDownloaded.toString());
    }

    private File downloadFromServer(String filename) {
        return null;
    }

    private void uploadToServer(File fileToUpload) {
        //upload file
    }

    private String[] fetchCRSoutput(String testFeed) {
        return null;
    }


    private String fetchCourses() {
        return null;
    }

    private File generateCoursePageLayout(String[] courses) {
        return null;
    }


    private String generateCRSfeed(String userDetails) {
        return "";
    }

    private File generateUserProfile(String[] userDetails) {
        return null;
    }

    String performAuthentication(String user){
        return "";
    }


    private File generateCoursesLayout(String[] courses) {
        return null;
    }
}