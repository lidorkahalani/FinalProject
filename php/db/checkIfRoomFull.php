<?php
	require_once('connection.php'); 
	//$roomName=$_POST['room_name']; //game_name=room_name
	//$game_id=getGameId($roomName);
	$game_id=$_POST['game_id'];
	$response=array();
	$result = $con->query("SELECT COUNT(*) FROM game_users WHERE game_id='$game_id'")->fetchColumn();
	if($result==2){
		$conn=mysqli_connect("localhost","root","","quartetsdb");
		$response=array();
		if (mysqli_connect_errno($conn))
		{
		   echo '{"query_result":"ERROR"}';
		}
		mysqli_set_charset($conn,"utf8");
		$sql_query = "SELECT user_id FROM game_users WHERE game_id='$game_id'";
		$result = mysqli_query($conn,$sql_query); 
		if(mysqli_num_rows($result) >0 )  
		{	
			$response['all_users_id']=array();
			$allUsersIds=array();
			$i=1;
			while($row=mysqli_fetch_array($result)){
				$allUsersIds["user_id"]=$row['user_id'];
				array_push($response['all_users_id'],$allUsersIds);
			}
		}
		$response["succsses"]=1;
	}
	else
		$response["succsses"]=3;
	
	echo json_encode($response);
	mysqli_close($conn);
	
	function getGameId($roomName){
	include('connection.php');
	$sth = $con->prepare("SELECT game_id FROM game where game_name='$roomName' AND is_active=1");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result['game_id'];
	}
	else{
		return null;
		}
}
?>