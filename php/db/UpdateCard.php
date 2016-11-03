<?php
require_once('connection.php');
$response=array();
//$category=$_GET["category"];
$card_name=$_POST["card_name"];
$card_id=$_POST["card_id"];
//$card_image=$_GET["card_image"];

$res = $con->exec("UPDATE cards SET card_name = '$card_name' WHERE card_id = '$card_id'");
  if($res !== FALSE )
	$response['su']=1;
  else
	$response['su']=0;
echo json_encode($response);
?>