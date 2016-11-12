<?php
$con = new PDO('mysql:host=localhost;dbname=quartetsdb;charset=utf8', 'root', 'root');
$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
?>