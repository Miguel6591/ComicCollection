<?php

require_once 'Funciones.php';
$db = new funciones();

$nombre = $_POST['nombre'];
$autor = $_POST['autor'];
$editorial = $_POST['editorial'];
$descripcion = $_POST['descripcion'];
$estado = $_POST['estado'];
$valoracion = $_POST['valoracion'];

$comic = $db->insertComic($nombre, $autor, $editorial, $descripcion, $estado, $valoracion);
if ($comic) {
    $response["error"] = FALSE;
    $response["comic"] = $comic ;
    echo json_encode($response);

} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "JSON Error occured in Registartion";
    echo json_encode($response);
}




?>