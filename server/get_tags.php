<?php
/*
This api call helps to get tags of a course
*/
require_once 'include/DB_Functions.php';

$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_id'])) {
    
    // receiving the GET params
    $course_id = $_GET['course_id'];
    
    // calling the function of DB helper
    $tags = $db->getAllTags($course_id);
    if (count($tags)>0) {
        // tags has been retrieved
        $response["error"] = FALSE;
        $response["tags"] = $tags;
        echo json_encode($response);
    } else {
        // course tag not found
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