<?php
include 'Config.php';
class DB_Connect {
    private $conn;
 
    // Connecting to database
    public function connect() {
    	// Connecting to mysql database
        
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
        if(!$this->conn){
        }
        else{
        } 
        // return database handler
        return $this->conn;
    }
}
$db = new Db_Connect();
$db->connect();
?>