<?php
	try{
    $con = new pdo('mysql:host=localhost;dbname=quartetsdb;charset=utf8','root','',array(
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    ));
	}catch(PDOException $pe){
        echo $pe->getMessage();
    }    

/*$con = new PDO('mysql:host=localhost;dbname=quartetsdb;charset=utf8', 'root', '');
 catch (PDOException $e) {
    echo 'Connection failed: ' . $e->getMessage();
}
$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);*/
?>