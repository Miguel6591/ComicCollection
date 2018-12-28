<?php

class funciones {

    private $conn;

    // constructor
    function __construct() {
        require_once 'Conect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }

    public function getComics(){
        $stmt = $this->conn->prepare("SELECT * FROM comics ORDER BY nombre");
        $stmt->execute();
        if ($stmt->rowCount()) {
            $row_all = $stmt->fetchall(PDO::FETCH_ASSOC);
            return $row_all;
        } elseif(!$stmt->rowCount()){
          return false;
      }
  }

  public function insertComic($nombre, $autor, $editorial, $descripcion, $estado, $valoracion){
    $stmt = $this->conn->prepare("INSERT INTO comics(nombre, autor, editorial, descripcion, estado, valoracion) 
        VALUES(:nombre,:autor,:editorial,:descripcion,:estado,:valoracion)");

    $stmt->bindParam(':nombre', $nombre);
    $stmt->bindParam(':autor', $autor);
    $stmt->bindParam(':editorial', $editorial);
    $stmt->bindParam(':descripcion', $descripcion);
    $stmt->bindParam(':estado', $estado);
    $stmt->bindParam(':valoracion', $valoracion);

    $stmt->execute();

    if ($stmt) {
        return true;
    } else {
        return false;
    }

}



}	

?>
