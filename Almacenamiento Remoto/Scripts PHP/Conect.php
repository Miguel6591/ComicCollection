<?php

class DB_Connect {
    private $conn;

    // Connecting to database
    public function connect() {
        require_once 'config.php';
        
        // Connecting to mysql database
        try {
	        $this->conn = new PDO('mysql:host='. DB_HOST .';dbname='. DB_DATABASE , DB_USER, DB_PASSWORD);
	    	$this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	    }
		catch(PDOException $e)
	    {
	    	die("OOPs something went wrong");
	    }
        
        // return database handler
        return $this->conn;
    }
}

?>