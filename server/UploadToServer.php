<?php
  /*
	this api call uploads the file to the specified location after 
	uploading it first to a temporary location
  */
    $file_path = "uploads/";
     
    $file_path = $file_path.basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path) ){
        echo $file_path;
    } else{
        echo "fail";
    }
 ?>