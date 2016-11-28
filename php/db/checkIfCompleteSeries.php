<?php
include('connection.php');
$game_id=$_POST['game_id'];
$user_id=$_POST['user_id'];

$response=array();
$sth = $con->prepare("SELECT * FROM games_cards WHERE game_id='$game_id' AND user_id='$user_id'");
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