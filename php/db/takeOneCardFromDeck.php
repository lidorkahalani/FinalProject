<?php
include('connection.php');
$response=array();
$user_id=$_POST['user_id'];
$game_id=$_POST['game_id'];
		$cnt=0;			
		
if(!checkIfDeckEnd($game_id)){
	while($cnt<1){
			   $randCard=checkIfCardAvailable();
			    if($randCard!=0){
					$res = $con->exec("UPDATE games_cards SET user_id = '$user_id' WHERE game_id = '$game_id' AND card_id=$randCard");
					if($res!==false ){
						$sql_query = "select * from cards where card_id='$randCard'";  
						$conn=mysqli_connect("localhost","root","","quartetsdb");
						$result = mysqli_query($conn,$sql_query);  
						mysqli_set_charset($conn,"utf8");
						if(mysqli_num_rows($result) >0 )  
						{
							$response["AllCards"]=array();
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
								array_push($response["AllCards"],$card);
							}
						   $cnt++;
						   if($cnt==1){
								$response["succsses"]=1;
								echo json_encode($response);
								break;
						   }
						}else {
								$response["succsses"]=0;
								echo json_encode($response);
								break;
						}
					}else {
						$response["succsses"]=0;
						break;
					}
				}else 
					 continue;
		}
	
}else{
	$response["succsses"]=-1;
	echo json_encode($response);
}
		
function checkIfDeckEnd($game_id){
   require('connection.php');
	$sth = $con->prepare("SELECT * FROM games_cards where user_id=0 AND game_id='$game_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result)
			  return false;
			else
			  return true;
}
function checkIfCardAvailable(){
			include('connection.php');
			$randCard=rand(1, 32);
			$sth = $con->prepare("SELECT user_id FROM games_cards where card_id='$randCard'");
			$sth->execute();
			$result = $sth->fetchAll();
			if($result){
				return $randCard;
			}else{
				return 0;
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
		      return $row["category_name"];
		      
			} 
		}		
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
		      return $row["category_color"];
		      
			} 
		}		
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
			return $ABCD;
		}	
}
?>