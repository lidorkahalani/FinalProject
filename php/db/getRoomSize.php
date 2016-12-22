<?php
require_once('connection.php');
$response=array();
$game_name=$_POST['game_name'];
$sth = $con->prepare("SELECT room_size FROM game where game_name='$game_name'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result){
				$response["room_size"]=$result["room_size"];
				$response["succsses"]=1;
			}else
				$response["succsses"]=0;
			echo json_encode($response);
			
?>