<?php
/*
    this api call updates the database entry to the uploads notes
*/
require_once 'include/DB_Functions.php';

$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
if (isset($_GET['course_id']) and isset($_GET['notes_title']) and isset($_GET['notes_tag1']) and isset($_GET['notes_tag2']) and isset($_GET['notes_tag3']) and isset($_GET['notes_url'])) {
    
    // receiving the GET params
    $course_id=$_GET['course_id'];
    $notes_title = $_GET['notes_title'];
    $notes_tag1=$_GET['notes_tag1'];
    $notes_tag2=$_GET['notes_tag2'];
    $notes_tag3=$_GET['notes_tag3'];
    $notes_loc=$_GET['notes_url'];
    
    // calling the add notes function of DB helper
    $done=$db->addNotesFlow($course_id,$notes_title,$notes_tag1,$notes_tag2,$notes_tag3,$notes_loc);
    if (done) {
        // database updated
        $response["error"] = FALSE;
        echo json_encode($response);
    } else {
        // databse not updates
        $response["error"] = TRUE;
        echo json_encode($response);
    }
} else {
    // required GET params is missing
    $response["error"] = TRUE;
    echo json_encode($response);
}
?>