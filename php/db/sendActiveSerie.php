<?php
require('connection.php');
$response=array();
$roomName=$_POST['room_name'];
$user_id=$_POST['user_id'];
$json=$_POST['json'];
$obj = json_decode($json);

echo json_encode(count($json));

/*	$insert = $con->exec("INSERT INTO game (game_name,is_active,my_turn_user_id,admin_user_id) VALUES('$roomName',0,0,'$user_id')");
 if ($insert !== FALSE) {
	 $id = $con->lastInsertId();
	  $game["game_id"]=$id;
     $insert=$con->exec("INSERT INTO game_users (game_id,user_id) VALUES('$id','$user_id')");
	 if ($insert !== FALSE) {
		 $cardsId=array();
	    for(int $i=0;$i<count($obj);$i++){
			$category_id = $obj['seriesId'+'$i'];
			$cardsId=getCardID($category_id);
			for($j=0;$j<4;$j++){
				$card_id = $cardsId[$j]
				$insert=$con->exec("INSERT INTO games_cards (game_id,card_id,category_id,user_id) VALUES('$id','$card_id','$category_id',0)");
			}
		}
			$game["game_name"]=$roomName;
			array_push($response["game"],$game);
			$response["successes"]=1;
	 }
		}else
			$response["successes"]=0;	
 
function getCardID($category_id){
require_once('connection.php');
$query = $con->query("select card_id from cards where category_id='$category_id'");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);
return $result;*/
?>
