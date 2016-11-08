<?php
require('connection.php');
$response=array();
$user_id=$_POST['user_id'];
$game_id=$_POST['game_id'];
	$sth = $con->prepare("SELECT my_turn_user_id FROM game where game_id='$game_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['my_turn_user_id']==$user_id){
				$response["successes"]=1;
			}else{
				$response["successes"]=0;

			}
			echo json_encode($response);
?>