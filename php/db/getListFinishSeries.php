<?php
require_once('connection.php');
$game_id=$_POST["game_id"];
$user_id=$_POST["user_id"];
$response=array();
$query = $con->query("SELECT category.category_name FROM category WHERE category_id in(SELECT distinct category_id from games_cards where game_id='$game_id' and user_id='$user_id' and series_complete=1)");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);
if($result){
	$response["finishSeriesNameList"]=$result;
	$response["success"]=1;
}else
	$response["success"]=0;

echo json_encode($response);
?>