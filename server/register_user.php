<?php
/*
    This api call helps to register a new user
*/
require_once 'include/DB_Functions.php';

$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
if (isset($_GET['user_id']) and isset($_GET['name']) and isset($_GET['year']) and isset($_GET['grade']) and isset($_GET['password'])) {
    
    // receiving the GET params
    $user_id=$_GET['user_id'];
    $name=$_GET['name'];
    $year=$_GET['year'];
    $grade=$_GET['grade'];
    $password=$_GET['password'];

    // calling the function of DB helper
    $courses = $db->storeUser($user_id,$name,$year,$grade,$password);
    if ($courses!=false) {
        // user is registered
        $response["error"] = FALSE;
        $response["user"] = $courses;
        echo json_encode($response);
    } else {
        // user is not registered
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