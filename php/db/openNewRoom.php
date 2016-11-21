<?php
require('connection.php');
$response=array();
$roomName=$_POST['room_name'];
$user_id=$_POST['user_id'];

//0 minnig that the name free for use
 $response["game"]=array();
 $game=array();
if(checkIfRoomNameAvailable($roomName)==0){
	$insert = $con->exec("INSERT INTO game (game_name,my_turn_user_id,admin_user_id) VALUES('$roomName',0,'$user_id')");
 if ($insert !== FALSE) {
	 $id = $con->lastInsertId();
	  $game["game_id"]=$id;
     $insert=$con->exec("INSERT INTO game_users (game_id,user_id) VALUES('$id','$user_id')");
	 if ($insert !== FALSE) {
	    $conn=mysqli_connect("localhost","root","","quartetsdb");
		mysqli_set_charset($conn,"utf8");
		$sql_query = "select card_id from cards";  
		$result = mysqli_query($conn,$sql_query);  
		$card_id=0;
		if(mysqli_num_rows($result) >0 ) {
			//echo json_encode($result);			
			//   $stmt = $con->prepare("INSERT INTO games_cards (game_id,card_id) VALUES(:id, :card_id)");
			//	$stmt->bindParam(':id', $id);
			//	$stmt->bindParam(':card_id',$card_id);
			while($row=mysqli_fetch_array($result)){
				$card_id = $row['card_id'];
				$insert=$con->exec("INSERT INTO games_cards (game_id,card_id,user_id) VALUES('$id','$card_id',0)");
				/*if($insert === FALSE){
					$response["successes"]=0;
					break;
				}*/
				
			//	$card_id = $row['card_id'];
			//	$stmt->execute();
				
			}
			$game["game_name"]=$roomName;
			array_push($response["game"],$game);
			$response["successes"]=1;
		}else
			$response["successes"]=0;	
	}else
		$response["successes"]=0;	
 }else
	 $response["successes"]=0;
}else
	$response["successes"]=0;	
 echo json_encode($response);
function checkIfRoomNameAvailable($req_Room_name){
	require('connection.php');
	$sth = $con->prepare("SELECT is_active FROM game where game_name='$req_Room_name'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result)
		return $result['is_active'];
}
?>
