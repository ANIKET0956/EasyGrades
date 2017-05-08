<?php
/*
This api call helps to login the user
if the provided credentials are correct
*/
require_once 'include/DB_Functions.php';

$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
// echo "<h3>".$_GET['course_term']"</h3>"
if (isset($_GET['user_id']) and isset($_GET['password'])) {
    //receiving the parameters
    $user_id=$_GET['user_id'];
    $password=$_GET['password'];
    
    // calling the function of DB helper
    $courses = $db->getUserByUser_idAndPassword($user_id, $password);
    if ($courses!=NULL) {
        // user is found
        $response["error"] = FALSE;
        $response["user"] = $courses;
        echo json_encode($response);
    } else {
        // user is not found with the credentials
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