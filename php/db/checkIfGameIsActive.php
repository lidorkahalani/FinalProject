<?php
require_once('connection.php');

$game_id=$_POST['game_id'];
$response=array();
$sth = $con->prepare("SELECT is_active FROM game where game_id='$game_id'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		$response["result"]=$result;
		$response["succsses"]=1;
	}else{
		$response["succsses"]=0;
	}
	echo json_encode($response);
?>