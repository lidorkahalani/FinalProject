<?php
require_once('connection.php');
$roomName=$_POST['room_name']; //game_name=room_name
$user_id=$_POST['user_id'];
$game_id=getGameId($roomName);

//if room exist
if($game_id){
	if(countPlayerInRoom($game_id)<4){
		//another way to insert working
			/*try {
				$sql = "INSERT INTO game_users (game_id,user_id) VALUES(6,18)";
				$sth = $con->query($sql);
				echo json_encode(1);
			} catch(PDOException $e) {
				echo $e->getMessage();
				echo json_encode(0);
			}*/	
		  $insert=$con->exec("INSERT INTO game_users (game_id,user_id) VALUES('$game_id','$user_id')");
		  if($insert !== FALSE){
			  //after player join to room wee need check if room has 4 players
			  echo json_encode(1);
		  }else{
			  echo json_encode(0);
		  }
	}else{
		echo json_encode(0);
	}
}else
	echo json_encode(0);

function getGameId($roomName){
	require('connection.php');
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
function countPlayerInRoom($game_id){
	require('connection.php'); 
	$result = $con->query("SELECT COUNT(*) FROM game_users WHERE game_id=game_id")->fetchColumn();	
	return $result;
}
?>