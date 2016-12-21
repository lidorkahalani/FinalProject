<?php
include('connection.php');
$response=array();
$response["game"]=array();
$game=array();
$Room_name=$_POST['room_name'];
$user_id=$_POST['user_id'];

	$sth = $con->prepare("SELECT is_active FROM game where game_name='$Room_name'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result['is_active']!=4){
		$insert = $con->exec("INSERT INTO game (game_name,is_active,my_turn_user_id,admin_user_id) VALUES('$Room_name',0,0,'$user_id')");
		if ($insert !== FALSE) {
	     $id = $con->lastInsertId();
	     $game["game_id"]=$id;
	     $game["game_name"]=$Room_name;
         $insert=$con->exec("INSERT INTO game_users (game_id,user_id) VALUES('$id','$user_id')");
			if ($insert !== FALSE) {
			 array_push($response["game"],$game);
			 $response["room_status"]=1;
			}else
			 $response["room_status"]=0;
		}else
			$response["room_status"]=0;
	}else
		$response["room_status"]=0;
echo json_encode($response);
?>