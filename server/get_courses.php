<?php
/*
this api call is used to get the list of courses for a 
given semester
*/
require_once 'include/DB_Functions.php';

$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_term'])) {
    
    // receiving the GET params
    $course_term = $_GET['course_term'];

    // calling the function of DB helper
    $courses = $db->getAllCourse($course_term);
    if (count($courses)>0) {
        // list of course found
        $response["error"] = FALSE;
        $response["courses"] = $courses;
        echo json_encode($response);
    } else {
        // given semester has no course
        $response["error"] = TRUE;
        $response["error_msg"] = "Error Occurred!";
        echo json_encode($response);
    }
} else {
    // required GET params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>