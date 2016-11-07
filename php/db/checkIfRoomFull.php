<?php
	require_once('connection.php'); 
	//$roomName=$_POST['room_name']; //game_name=room_name
	//$game_id=getGameId($roomName);
	$game_id=$_POST['game_id'];
	$response=array();
	$result = $con->query("SELECT COUNT(*) FROM game_users WHERE game_id='$game_id'")->fetchColumn();
	if($result==4)
		$response["succsses"]=1;
	else
		$response["succsses"]=0;
	
	echo json_encode($response);
	
	function getGameId($roomName){
	include('connection.php');
	$sth = $con->prepare("SELECT game_id FROM game where game_name='$roomName' AND is_active=1");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result['game_id'];
	}
	else{
		return null;
		}
}
?>