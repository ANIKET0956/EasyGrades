package com.example.aniket.easygrades.test;

/**
 * Created by Noman Sheikh on 05-05-2017.
 */

/**
 * <h1>TESTING PLAN</h1><br>
 *     <hr>
 *     <h2>1. Introduction</h2><br>
 *         We divided the application testing into two phases : Development Testing and User Testing.
 *         We skipped the Release Testing because of the unavailablity of independent team to verify the software.
 *     <hr>
 *     <h2>2. Development Testing</h2><br>
 *         This kind of testing is done by developers in the process of developing the software.
 *         Development Testing is further divided into 3 stages: Unit Testing, Component Testing, System Testing
 *         <ol>
 *             <li>
 *                 <b>Unit Testing:</b><br>
 *                 We have designed test cases every object that is present in our entire framework.
 *                 The list of test that we have done are illustrated below.
 *                 Some of the test are based on the automated script and some of them are tested manually depending on the feasibility.
 *                 <ul>
 *                     <li>
 *                         <b>Authentication</b><br>
 *                             We have an automated script for checking this module.
 *                             We have given as input a list of users with all the credentials.
 *                             Output of each test case is compared against the predefined/expected output of the test case.
 *                     </li>
 *                     <li>
 *                         <b>User Profile</b><br>
 *                             This checks tha display of the user profile on the client.
 *                             Test code hardcode a user details (in order to avoid communication with the server).
 *                             Output is the an xml file which can be seen in the third fragment of the homepage.
 *                             We have an automated script for checking this module.
 *                     </li>
 *                     <li>
 *                         <b>Course Fragment</b><br>
 *                             Input : an XML with a list of courses.<br>
 *                             Output : XML layout file which can be seen in the first fragment of the homepage.
 *                     </li>
 *                     <li>
 *                         <b>CRS Input Fragment</b><br>
 *                             Input : User details (which imitates the requirement of the user)<br>
 *                             Output : XML layout file which can be seen in the first fragment of the homepage.
 *                     </li>
 *                     <li>
 *                         <b>Course Homepage</b><br>
 *                             Input : an XML with a list of all the notes uploaded in a page.<br>
 *                             Output : XML layout file which can be seen in the first fragment of the homepage.
 *                     </li>
 *                     <li>
 *                         <b>Database</b><br>
 *                              An automated SQL script for testing behaviour of all the tables.
 *                              This script is run on the server terminal and has nothing to do with the client architecture.
 *                     </li>
 *                     <li>
 *                         <b>PHP at Server</b>
 *                     </li>
 *                 </ul>
 *             </li>
 *
 *             <li>
 *                 <b>Component Testing</b><br>
 *                 We have identified certain components which are composed several units interacting with each other.
 *                 These components are identified on the basis of requirement document submitted earlier.
 *                 The components are such that they cover all the requirements mentioned.
 *                 Various Components are as follows:
 *                 <ul>
 *                     <li>Fetching all course list from server</li>
 *                     <li>Getting course recommendation from CRS.</li>
 *                     <li>Upload and Download Task</li>
 *                     <li>Adding the course</li>
 *                     <li>Searching</li>
 *                     <li>Favourites</li>
 *                 </ul>
 *             </li>
 *
 *             <li>
 *                 <b>System Testing</b><br>
 *                     This involves testing system from end-to-end to check correctness and performance.
 *                     We performed testing with different devices and enviroment in order to get sure to its reliablity and performance.
 *                     We have tested for various input cases both trivial and extreme.
 *                     Application seems to perform as expected and there were no anomalies as such.
 *                     However it takes some time to load profile page due to its dynamic UI.
 *                     On mobile phones with lower android versions, application hangs for 5-10 seconds but no such behaviour is
 *                     observerd with Android 6.0+ devices.
 *             </li>
 *         </ol>
 *      <hr>
 *     <h2>3. User Testing</h2><br>
 *     Lastly we gave our application to a curated set of user to check and get some feedback.
 *     <ol>
 *         <li>
 *            <b>Alpha Testing:</b><br>
 *            In the Alpha testing phase, we got some very crucial feedback about the UI concerning colors and display of profile and course.
 *            We also got suggestions on the flow of all pages in the application. What should be the correct order of connecting all these pages
 *         </li>
 *         <li>
 *             <b>Beta Testing:</b><br>
 *             We have incorporated mostly all the suggestions that were reasonable and feasible given the restricted frame of time.
 *             Application is currently in beta-testing phase with a large set of users.
 *         </li>
 *     </ol>
 */

public class TestingDoc {
}
