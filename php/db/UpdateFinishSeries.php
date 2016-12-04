<?php
include('connection.php');
$game_id=$_POST['game_id'];
$user_id=$_POST['user_id'];
$json=$_POST['finish_category'];


$categorys_ids["seriseCards"]=array();
$obj = json_decode($json,true);
$response=array();

if($obj!=null){
		for($i=0;$i<count($obj['finish_categorys_id']);$i++){
			$category_id = $obj['finish_categorys_id'][$i]["category_id$i"];
				$res = $con->exec("UPDATE games_cards SET series_complete = 1 WHERE category_id = '$category_id' AND game_id='$game_id' AND user_id='$user_id'");
				 if(  $res !== FALSE ) {
					$response["successes"]=1;
				}else {
					$response["successes"]=0;
					echo json_encode($response);
					break;
				}	
		}
}else
	$response["successes"]=-1;

echo json_encode($response);
?>