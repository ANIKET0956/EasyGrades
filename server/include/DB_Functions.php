<?php
 


class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 	
    // destructor
    function __destruct() {
         
    }

 	//functions of users table
    /**
     * Store new user
     * @param user_id,name,year,grade and password
     * returns user details
     */
    public function storeUser($user_id,$name,$year,$grade,$password) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $stmt = $this->conn->prepare("INSERT INTO users(user_id,name,year,grade, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?,?, NOW())");
        $stmt->bind_param("ssssss", $user_id, $name,$year,$grade, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE user_id = ?");
            $stmt->bind_param("s", $user_id);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by user_id and password
     * @param user_id and password
     * returns user or NULL
     */
    public function getUserByUser_idAndPassword($user_id, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE user_id = ?");
 
        $stmt->bind_param("s", $user_id);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     * @param user_id
     * returns true or false
     */
    public function isUserExisted($user_id) {
        $stmt = $this->conn->prepare("SELECT user_id from users WHERE user_id = ?");
 
        $stmt->bind_param("s", $user_id);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }

    //functions of user_course table

    /**
     * adding courses of a user
     * @param user and course
     * returns true and false
     */
    public function addCoursesUser($user_id,$courses) {
    	for($i=0;i<count($courses);$i++){
			$stmt=$this->conn->prepare("INSERT INTO user_course (user_id, course_id) VALUES(?,?)");
			$stmt->bind_param("ss",$user_id,$courses[$i]); 
			$result = $stmt->execute();
	        $stmt->close();
	    	if ($result) {
	        }
	        else{
	            return false;
	        }
    	}
    	return true;
    }

    //functions for admin table

    /**
     * adding admin to the database
     * @param admin_id,name, password
     * returns true or false
     */
    public function addAdmin($admin_id,$name,$password) {
    	$stmt=$this->conn->prepare("INSERT INTO admin(admin_id,name,password) VALUES(?,?,?)");
    	$stmt->bind_param("sss",$admin_id,$name,$password);
    	$result = $stmt->execute();
	    $stmt->close();
	    if ($result) {
            return true;
	    }
	    else{
	        return false;
	    }
    }

    //functions for course table

    /**
     * adding course which can be floated
     * @param course_id, course_name and password
     * returns true or false
     */
    public function addCourse($course_id,$course_name,$dept_name) {
	   	$stmt=$this->conn->prepare("INSERT INTO course(course_id,course_name,dept_name) VALUES(?,?,?)");
	   	$stmt->bind_param("sss",$course_id,$course_name,$dept_name);
	   	$result = $stmt->execute();
	    $stmt->close();
	    if ($result) {
            return true;
	    }
	    else{
	        return false;
	    }
    }

    //function for course_float table
	
    /**
     * floating course for a particular term
     * @param course_id, prof_name and course_term
     * returns true or false
     */
	public function floatCourse($course_id,$prof_name,$course_term) {
	   	$stmt=$this->conn->prepare("INSERT INTO course_float(course_id,prof_name,course_term) VALUES(?,?,?)");
	   	$stmt->bind_param("sss",$course_id,$prof_name,$course_term);
	   	$result = $stmt->execute();
	    $stmt->close();
	    if ($result) {
            return true;
	    }
	    else{
	        return false;
	    }
    }

    //function to get all courses

    /**
     * get all the courses floated in a term
     * @param password
     * returns list of courses
     */
    public function getAllCourse($course_term){
        $result=$this->conn->query("SELECT course.course_id,course_name,dept_name,prof_name from course,course_float where course.course_id=course_float.course_id and course_term=('$course_term')");
        $rows = array();
        while($row =$result->fetch_array()){
                array_push($rows, $row);
        }
        if ($result) {
            return $rows;
        }
        else{
            return false;
        }
    }    
    //function to get all tags of a course 

    /**
     * Get all the tags of a particular course
     * @param course_id
     * returns list of tags
     */
    public function getAllTags($course_id){
        $result=$this->conn->query("SELECT * from course_tag where course_id=('$course_id')");
        $rows = array();
        while($row =$result->fetch_array()){
                array_push($rows, $row);
        }
        if ($result) {
            return $rows;
        }
        else{
            return false;
        }
    }

    //functions for course_tag table

    /**
     * Add tags to a course
     * @param course_id and list of tags
     * returns true or false
     */
    public function addCourseTag($course_id,$tags) {
    	for($i=0;i<count($tags);$i++){
			$stmt=$this->conn->prepare("INSERT INTO course_tag (course_id,course_tag) VALUES(?,?)");
			$stmt->bind_param("ss",$course_id,$tags[$i]); 
			$result = $stmt->execute();
	        $stmt->close();
	    	if ($result) {
	        }
	        else{
	            return false;
	        }
    	}
    	return true;
    }

    //function for notes table
    /**
     * Add notes to a course
     * @param course_id and notes_title
     * returns id of added notes
     */
    public function addNotes($course_id,$notes_title) {
	   	$stmt=$this->conn->prepare("INSERT INTO notes(course_id,notes_title) VALUES (?,?)");
	   	$stmt->bind_param("ss",$course_id,$notes_title);
	   	$result = $stmt->execute();
	    $stmt->close();
        $result1=$this->conn->query("SELECT * from notes where course_id=('$course_id') and notes_title=('$notes_title')");
        $row =$result1->fetch_array();
        while($row1 =$result1->fetch_array()){
                $row=$row1;
        }
        if ($result) {
            return $row['notes_id'];
	    }
	    else{
	        return false;
	    }
    }

    //function for notes_location table
    /**
     * Upldating location where notes is stored
     * @param notes id and notes object location
     * returns true or false
     */
    public function addNotesLocations($notes_id,$notes_obj_loc) {
	   	$stmt=$this->conn->prepare("INSERT INTO notes_location(notes_id,notes_obj_loc)  VALUES (?,?)");
	   	$stmt->bind_param("ss",$notes_id,$notes_obj_loc);
	   	$result = $stmt->execute();
	    $stmt->close();
	    if ($result) {
	    }
	    else{
	        return false;
	    }
        return true;
    }


    //function for notes_tags table
    /**
     * Add tags of a notes
     * @param notes_id and tag
     * returns true or false
     */
    public function addNotesTag($notes_id,$tags) {
		$stmt=$this->conn->prepare("INSERT INTO notes_tag (notes_id,notes_tag)  VALUES (?,?)");
		$stmt->bind_param("ss",$notes_id,$tags); 
		$result = $stmt->execute();
	    $stmt->close();
	   	if ($result) {
	       }
	       else{
	           return false;
	       }
    	return true;
    }

    /**
     * Creates all teh steps of adding notes fields to different tables 
     * @param course_id,notes_title,notes_tag and notes_url
     * returns true or false
     */
    public function addNotesFlow($course_id,$notes_title,$notes_tag1,$notes_tag2,$notes_tag3,$notes_url)
    {
        $notes_id=$this->addNotes($course_id,$notes_title);
        if(notes_id==false)
            return false;
        $this->addNotesLocations($notes_id,$notes_url);
        if(!($notes_tag1==''))
        {
            $this->addNotesTag($notes_id,$notes_tag1);    
        }
        if(!($notes_tag2==''))
        {
            $this->addNotesTag($notes_id,$notes_tag2);    
        }
        if(!($notes_tag3==''))
        {
            $this->addNotesTag($notes_id,$notes_tag3);    
        }
        $stmt2=$this->conn->prepare("INSERT INTO notes_rating(course_id,notes_id,ratings) VALUES(?,?,1)");
        $stmt2->bind_param("ss",$course_id,$notes_id);
        $result = $stmt2->execute();
        $stmt2->close();
        return true;                
        
    }

    /**
     * get all notes of a course
     * @param course_id
     * returns all notes or false
     */
    public function getAllNotes($course_id){
        $result=$this->conn->query("SELECT notes.notes_id,notes_title,notes_obj_loc,notes_tag,ratings from notes,notes_location,notes_tag,notes_rating where notes.notes_id=notes_location.notes_id and notes_location.notes_id=notes_tag.notes_id and notes_tag.notes_id=notes.notes_id and notes.course_id=('$course_id') and notes_rating.notes_id=notes.notes_id and notes_rating.course_id=('$course_id')");
                $rows = array();
        if($result)
        {
        }
        else
        {
            return $rows;
        }
        while($row =$result->fetch_array()){
                array_push($rows, $row);
        }
        if ($result) {
            return $rows;
        }
        else{
            return false;
        }
    }

    // function for course rating table
    /**
     * Add course rating
     * @param course_id, tags list and ratings list
     * returns true or false
     */
    public function addRatingCourse($course_id,$tags,$ratings) {
    	for($i=0;$i<count($tags);$i++){
	    	$result=$this->conn->query("SELECT * FROM course_rating where course_id=('$course_id') and course_tag=('$tags[$i]')");
	    	if($row = $result->fetch_array()){
                $stmt1=$this->conn->prepare("UPDATE course_rating SET numb_ratings=numb_ratings+1,ratings=(ratings*{$row['numb_ratings']}+{$ratings[$i]})/({$row['numb_ratings']}+1) where course_id=('$course_id') and course_tag=('$tags[$i]')");
                $result = $stmt1->execute();
	        	$stmt1->close();
	    	}	
	    	else{
	    		$stmt2=$this->conn->prepare("INSERT INTO course_rating (course_id,course_tag,numb_ratings,ratings) VALUES (?,?,1,?)");
	    		$stmt2->bind_param("ssd",$course_id,$tags[$i],$ratings[$i]);
	    		$result=$stmt2->execute();
	    		$stmt2->close();
	    	}
    	}
        return true;

    }

    //function for notes ratings table
    /**
     * updates number of downloads for each 
     * @param course_id  and notes_id
     * returns NULL
     */
    public function addNotesRating($course_id,$notes_id) {
    	$result=$this->conn->query("SELECT * FROM notes_rating where course_id=('$course_id') and notes_id=('$notes_id')");
    	if($result){
    		$row = mysql_fetch_array($result);
    		$stmt1=$this->conn->prepare("UPDATE notes_rating SET ratings=ratings+1 where course_id=? and notes_id=?");
    		$stmt1->bind_param("ss",$course_id,$notes_id);
    		$result = $stmt1->execute();
	        $stmt1->close();
    	}
    }

    /**
     * Suggest course based on logistic regression to the user based on provided tags and their ratings
     * @param tag list and rating list
     * returns suggested course
     */
    public function suggestCourse($tags,$ratings){
        $diff=array();
        $result1=$this->conn->query("SELECT distinct course_id FROM course_rating");
        while($id=$result1->fetch_array()){
            $diff[$id['course_id']]=0;
        }
        for($i=0;$i<count($tags);$i++){
            $result=$this->conn->query("SELECT course_id,ratings FROM course_rating where course_tag=('$tags[$i]')");
            while($row=$result->fetch_array()){
            $diff[$row['course_id']]+=(($row['ratings']-$ratings[$i])*($row['ratings']-$ratings[$i]));
            }   
        }
        $ans=array();
        $min_val = 750;
        $min_key = null;
        foreach($diff as $course_id => $val) {
            if ($val < $min_val) {
            $min_val = $min;
            $min_key = $course_id;
            }
        }
        array_push($ans,$min_key);
        return $ans;
    }



}
 
?>