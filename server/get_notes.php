<?php
/*
this api call helps to get all the notes of the course
*/
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_id'])) {
 
    // receiving the GET params
    $course_id = $_GET['course_id'];
    
    // calling the function of DB helper
    $notes = $db->getAllNotes($course_id);
    if (count($notes)>0) {
        // all notes of course found
        $response["error"] = FALSE;
        $response["id"] = $course_id;
        $response["notes"] = $notes;
        echo json_encode($response);
    } else {
        // course has no notes
        $response["error"] = TRUE;
        $response["error_msg"] = "no_notes";
        echo json_encode($response);
    }
} else {
    // required get params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>