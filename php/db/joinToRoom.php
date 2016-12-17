<?php
require_once('connection.php');
$roomName=$_POST['room_name']; //game_name=room_name
$user_id=$_POST['user_id'];
$game_id=getGameId($roomName);

$response=array();
//if room exist
if($game_id!=null){
	
	if(countPlayerInRoom($game_id)<2){//2
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
			  $response["game_id"]=$game_id;
			   $response["roomIsFull"]=0;
			  $response["succsses"]=1;
		  }else{
			   $response["succsses"]=0;
		  }
		  if(countPlayerInRoom($game_id)==2)
			  $response["roomIsFull"]=1;
	}else{
	     $response["roomIsFull"]=1;
		 $response["succsses"]=0;
	}
}else
	 $response["succsses"]=0;
echo json_encode($response);
function getGameId($roomName){
	require('connection.php');
	$sth = $con->prepare("SELECT game_id FROM game where game_name='$roomName' AND my_turn_user_id=0");
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
	$result = $con->query("SELECT COUNT(*) FROM game_users WHERE game_id='$game_id'")->fetchColumn();	
	return $result;
}
?>