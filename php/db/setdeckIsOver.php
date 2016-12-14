<?php
include('connection.php');
$game_id=$_POST['game_id'];

$response=array();
	$res = $con->exec("UPDATE game SET is_deck_over = 1 WHERE game_id = '$game_id'");
			 if(  $res !== FALSE ) {
				$response["successes"]=1;
			}else {
			    $response["successes"]=0;
			}
	echo json_encode($response);
?>