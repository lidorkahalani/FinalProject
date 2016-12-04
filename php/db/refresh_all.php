<?php
include('connection.php');
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
$seriseIdList=array();
if (mysqli_connect_errno($con)){
   echo '{"query_result":"ERROR"}';
}
$userId=$_POST["user_id"];
$game_id=$_POST["game_id"];
mysqli_set_charset($con,"utf8");
$sql_query = "select card_id from games_cards where user_id='$userId' AND game_id='$game_id' AND series_complete=0"; 
 
$result = mysqli_query($con,$sql_query);  
$response["myCards"]=array();
if(mysqli_num_rows($result) >0 )  {
	while($row=mysqli_fetch_array($result)){
		$card=getAllMyCard($row['card_id']);
		array_push($response["myCards"],$card);
	}

 $response["isMyturn"]=isMyTurn($userId,$game_id);
 
 $seriseIdList=getIdOfAllFullSeries($userId,$game_id);
 if($seriseIdList!=null){
	 $response["SeriesIds"]=$seriseIdList;
	 $response["finishSeries"]=1;
 }else
	$response["finishSeries"]=0;

  $response["isAllPlayersConnected"]=isAllPlayersConnected($game_id);//1-all connected 0-one or more logut
  if(getStatusGame($game_id)!=0)
	$response['is_active']=1;
  else
	$response['is_active']=0;
 
 $response["succsses"]=1;
 echo json_encode($response);
 }else {   
 $response["succsses"]=0;
 echo json_encode($response);  
 }
 mysqli_close($con);
function isMyTurn($user_id,$game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT my_turn_user_id FROM game where game_id='$game_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['my_turn_user_id']==$user_id)
				return 1;
			else
				return 0;
}
function getAllMyCard($cardId){
	$con=mysqli_connect("localhost","root","","quartetsdb");
	mysqli_set_charset($con,"utf8");
	$sql_query = "select * from cards where card_id='$cardId'";  
	$result = mysqli_query($con,$sql_query);  
	if(mysqli_num_rows($result) >0 )  
	{
		$card=array();
		while($row=mysqli_fetch_array($result)){
			$card["card_id"]=$row["card_id"];
			$card["card_name"]=$row["card_name"];
			$card["image_name"]=$row["card_img"];
			$card["category_id"]=$row["category_id"];
			$card["category_name"]=getCategoryName($card["category_id"]);
			$card["category_color"]=getCategorColor($card["category_id"]);
			$card["card_labels"]=array();
			$card["card_labels"]=getAllItems($card["category_id"]);
		}
		mysqli_close($con);
		return $card;
	}
	mysqli_close($con);
	
} 
function getIdOfAllFullSeries($user_id,$game_id){
	require('connection.php');
	$game_id=$_POST['game_id'];
	$sth = $con->prepare("SELECT category_id FROM games_cards WHERE user_id='$user_id' AND game_id='$game_id' AND series_complete=0 GROUP BY category_id HAVING COUNT(category_id)=4");
	$sth->execute();
	$result = $sth->fetchAll(PDO::FETCH_ASSOC);
	if($result){
		return $result;
	}else{
		return null;
	}
	

}
function getCategoryName($category_id){
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
mysqli_set_charset($con,"utf8");
	 $sql_query = "select category_name,category_color from category where category_id='$category_id'";
		$result = mysqli_query($con,$sql_query); 
		if(mysqli_num_rows($result) >0 )  
		{
			while($row=mysqli_fetch_array($result)){
				mysqli_close($con);
		      return $row["category_name"];
		      
			} 
		}
mysqli_close($con);		
 }
function getCategorColor($category_id){
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
mysqli_set_charset($con,"utf8");
	 $sql_query = "select category_color from category where category_id='$category_id'";
		$result = mysqli_query($con,$sql_query); 
		if(mysqli_num_rows($result) >0 )  
		{
			while($row=mysqli_fetch_array($result)){
				mysqli_close($con);
		      return $row["category_color"];
		      
			} 
		}
mysqli_close($con);		
 }
function getAllItems($category_id){
	 $con=mysqli_connect("localhost","root","","quartetsdb");
	 mysqli_set_charset($con,"utf8");
	 $sql_query = "select * from cards where category_id='$category_id'";  
		$result = mysqli_query($con,$sql_query);  
		$card_labels=array();
		$ABCD=array();
		if(mysqli_num_rows($result) >0 )  
		{	
			while($row=mysqli_fetch_array($result)){
				$card_labels["card_name"]=$row["card_name"];
				array_push($ABCD,$card_labels);
				}
				mysqli_close($con);
			return $ABCD;
		}
		mysqli_close($con);
 }
function isAllPlayersConnected($game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT is_active FROM game where game_id='$game_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['is_active']==1)
				return 1;
			else
				return 0;
}
function getStatusGame($game_id){
	require('Connection.php');
	$game_id=$_POST['game_id'];
	$sth = $con->prepare("SELECT is_active FROM game where game_id='$game_id'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result['is_active'];
	}else{
		return null;
	}
}
?>