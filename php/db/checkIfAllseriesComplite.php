<?php
require_once('connection.php');
$game_id=$_POST["game_id"];
$response=array();
/*$result = $con->query("SELECT games_cards.series_complete from games_cards where games_cards.series_complete=0 AND game_id='$game_id'")->fetchColumn();
if($result>0)*/
	
$sth = $con->prepare("SELECT games_cards.series_complete from games_cards where games_cards.series_complete=0 AND game_id='$game_id'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
if($result)
	$response["successes"]=0;
else
	$response["successes"]=1;
echo json_encode($response);
?>