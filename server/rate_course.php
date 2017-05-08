<?php
/*
This api call helps user to rate the course based
on the provided tags
*/
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_id']) and isset($_GET['course_tag1']) and isset($_GET['course_rating1']) and isset($_GET['course_tag2']) and isset($_GET['course_rating2']) and isset($_GET['course_tag3']) and isset($_GET['course_rating3']) and isset($_GET['course_tag4']) and isset($_GET['course_rating4']) and isset($_GET['course_tag5']) and isset($_GET['course_rating5'])) {
 
    // receiving the get params
    $course_id = $_GET['course_id'];
    $tags=array($_GET['course_tag1'],$_GET['course_tag2'],$_GET['course_tag3'],$_GET['course_tag4'],$_GET['course_tag5']);
    $ratings=array($_GET['course_rating1'],$_GET['course_rating2'],$_GET['course_rating3'],$_GET['course_rating4'],$_GET['course_rating5']);
    
    // calling the function of DB helper
    $courses = $db->addRatingCourse($course_id,$tags,$ratings);
    if ($courses != false) {
        // course has been rated
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
        // error in course rating
        $response["error"] = TRUE;
        $response["error_msg"] = "Error Occurred!";
        echo json_encode($response);
    }
} else {
    // required get params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>