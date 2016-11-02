<?php
include('connection.php');

$roomName=$_POST['room_name'];
$user_id=$_POST['user_id'];

//0 minnig that game is over and the name free for use
if(checkIfRoomNameAvailable($roomName)==0){
	$insert = $con->exec("INSERT INTO game (game_name) VALUES('$roomName')");
 if ($insert !== FALSE) {
	 $id = $con->lastInsertId();
     $insert=$con->exec("INSERT INTO game_users (game_id,user_id) VALUES('$id','$user_id')");
	 if ($insert !== FALSE) {
	    $conn=mysqli_connect("localhost","root","","quartetsdb");
		mysqli_set_charset($conn,"utf8");
		$sql_query = "select card_id from cards";  
		$result = mysqli_query($conn,$sql_query);  
		$card_id=0;
		if(mysqli_num_rows($result) >0 ) {	
			while($row=mysqli_fetch_array($result)){
				$card_id = $row['card_id'];
				$insert=$con->exec("INSERT INTO games_cards (game_id,card_id) VALUES('$id','$card_id')");
			}
			echo json_encode(1);
		}else{
			echo json_encode(0);
		}	
	}else{
		echo json_encode(0);
	}	
 }else{
	 echo json_encode(0);
 }
}else{
	 echo json_encode(0);
 }
function checkIfRoomNameAvailable($req_Room_name){
	require('connection.php');
	$sth = $con->prepare("SELECT is_active FROM game where game_name='$req_Room_name'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result)
		return $result['is_active'];
}
?>
