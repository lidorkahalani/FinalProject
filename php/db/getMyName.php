<?php
require_once('connection.php');
$response=array();
$user_id=$_POST['user_id'];
$sth = $con->prepare("SELECT user_name FROM users where user_id='$user_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result){
				$response["user_name"]=$result["user_name"];
				$response["succsses"]=1;
			}else
				$response["succsses"]=0;
			echo json_encode($response);
			
?>