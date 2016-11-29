<?php
include('connection.php');
$game_id=$_POST['game_id'];
//$user_id=$_POST['user_id'];

$response=array();
	$res = $con->exec("UPDATE game SET is_active = 0 WHERE game_id = '$game_id'");
			 if(  $res !== FALSE ) {
				$response["successes"]=1;
			}else {
			    $response["successes"]=0;
			}
	echo json_encode($response);
?>