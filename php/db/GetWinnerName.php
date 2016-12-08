<?php
require_once('connection.php');
$game_id=$_POST['game_id'];
$response=array();
/*
$query = $con->query("Select distinct user_id,COUNT(series_complete) FROM games_cards Where series_complete=1 AND game_id=53 group by user_id");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);*/

$query = $con->query("SELECT * FROM users WHERE user_id=( Select distinct user_id FROM games_cards Where series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete) ORDER by COUNT(series_complete) desc LIMIT 1)");
	$query->execute();
	$result = $query->fetchAll(PDO::FETCH_ASSOC);
	if($result){
	//$response["successes"]=1;
	$response["winner"]=$result;
	}else{
	//$response["successes"]=0;
	$response["winner"]=null;
	}
/*if($query->rowCount()==1){
	$query = $con->query("SELECT * FROM users WHERE user_id=( Select distinct user_id FROM games_cards Where series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete) ORDER by COUNT(series_complete) desc LIMIT 1)");
	$query->execute();
	$result = $query->fetchAll(PDO::FETCH_ASSOC);
	$response["successes"]=1;
	$response["winner"]=$result;
	
}
else{
	$response["successes"]=2;
	$response["winner"]=$result;
}*/
echo json_encode($response);
?>