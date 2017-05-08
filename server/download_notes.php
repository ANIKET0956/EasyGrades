<?php
require_once 'include/DB_Functions.php';
/*
This api call is used to increase
the number of downloads of a notes.
*/
$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_id']) and isset($_GET['notes_id']) and isset($_GET['notes_loc'])) {
    // receiving the GET params
    $course_id = $_GET['course_id'];
    $notes_id = $_GET['notes_id'];
    $notes_loc='uploads/'.$_GET['notes_loc'];

    // calling the function of DB helper
    $db->addNotesRating($course_id,$notes_id);
        $response["error"] = FALSE;
        $response["size"] = filesize($notes_loc);
        echo json_encode($response);
    }
else {
    // required GET params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>