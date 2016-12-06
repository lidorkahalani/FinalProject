<?php
include('connection.php');
$response=array();
$game_id=$_POST['game_id'];
$user_id=$_POST['user_id'];
$json=$_POST['json'];
$cardsId["seriseCards"]=array();
$obj = json_decode($json,true);
//var_dump($obj[0]);

if($obj!=null){
	    for($i=0;$i<count($obj['seriesIds']);$i++){
			$category_id = $obj['seriesIds'][$i]["seriesId$i"];
			$cardsId["seriseCards"]=getCardID($category_id);
			for($j=0;$j<4;$j++){
				$card_id = $cardsId["seriseCards"][$j]["card_id"];
				$insert=$con->exec("INSERT INTO games_cards (game_id,card_id,category_id,user_id,series_complete) VALUES('$game_id','$card_id','$category_id',0,0)");
			}
		}
		if($i===count($obj['seriesIds']))
			$response["successes"]=1;
		else
			$response["successes"]=0;
}else
	$response["successes"]=0;

echo json_encode($response);

function getCardID($category_id){
require('connection.php');
$query = $con->query("select card_id from cards where category_id='$category_id'");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);
return $result;
}
?>