<?php
include('connection.php');
$room_size=$_POST["room_size"];
$game_id=$_POST['game_id'];

$response=array();
	$res = $con->exec("UPDATE game SET room_size = '$room_size' WHERE game_id = '$game_id'");
			 if(  $res !== FALSE ) {
				$response["successes"]=1;
			}else {
			    $response["successes"]=0;
			}
	echo json_encode($response);
?>