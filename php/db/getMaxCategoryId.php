<?php
require_once('connection.php');
$response=array();
$sth = $con->prepare("SELECT MAX(category_id) FROM cards");
			$sth->execute();
			$result = $sth->fetchColumn();
			if($result){
				$response["max_category_id"]=$result;
				$response["succsses"]=1;
			}else
				$response["succsses"]=0;
			echo json_encode($response);
?>