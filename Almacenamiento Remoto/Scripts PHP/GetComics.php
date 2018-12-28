<?php

require_once 'Funciones.php';
$db = new funciones();


// get avistamiento
$comic = $db->getComics();
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
