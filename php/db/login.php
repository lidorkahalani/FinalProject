<?php
$response=array();
require_once('connection.php');
$user_name = $_POST['username'];  
$user_pass = $_POST['password'];
 
if (isset($_POST['username']) && isset($_POST['password'])){
$result = $con->query(
                "SELECT users.*, score_tabel.score 
			 FROM users, score_tabel 
			 WHERE users.user_name = '$user_name' and users.user_password = '$user_pass' AND users.user_id  = score_tabel.user_id")->fetchAll(PDO::FETCH_ASSOC);	
	 $response["succsses"]=1;
	 $response["User"]=$result;
}else{   
		$response["succsses"]=0;
		  
}
echo json_encode($response);

?>  
