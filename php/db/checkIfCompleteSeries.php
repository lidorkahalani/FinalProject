<?php
include('connection.php');
$game_id=$_POST['game_id'];
$user_id=$_POST['user_id'];

$response=array();
$sth = $con->prepare("SELECT * FROM games_cards WHERE game_id='$game_id' AND user_id='$user_id'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	$category_id=$result['$category_id'];
	//wee need get the category_id that have full serie and update the relvente column
	$res = $con->exec("UPDATE games_cards SET series_complete = 1 WHERE category_id = '$category_id'");
			 if(  $res !== FALSE ) {
				$response["successes"]=1;
			}else {
			    $response["successes"]=0;
			}
	echo json_encode($response);
?>