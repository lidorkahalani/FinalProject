<?php
require_once('connection.php');
$userId=$_POST["user_id"];
$game_id=$_POST['game_id'];

$sth = $con->prepare("SELECT * FROM game WHERE game_id='$game_id' AND admin_user_id='$userId'");
			$sth->execute();
			$result = $sth->fetchAll(PDO::FETCH_ASSOC);
			if($result){
				$response["succsses"]=1;
			}
			else	
				$response["succsses"]=0;
			
echo json_encode($response);

?>