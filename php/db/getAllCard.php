<?php
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
mysqli_set_charset($con,"utf8");
$sql_query = "select * from cards";  
$result = mysqli_query($con,$sql_query);  
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

 $response["succsses"]=1;
 $jsonstring = json_encode($response);
 echo $jsonstring;
 }
 else  
 {   
 $response["succsses"]=0;
 echo json_encode($response);  
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