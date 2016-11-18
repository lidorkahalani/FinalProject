<?php
require('connection.php');
$userId=$_GET["user_id"];
$response=array();
	$sth = $con->prepare("SELECT DISTINCT a.category_id,b.category_name,a.card_name,a.card_img FROM cards AS a, category AS b WHERE a.user_id='$userId' AND a.category_id=b.category_id");
			$sth->execute();
			$result = $sth->fetchAll(PDO::FETCH_ASSOC);
			if($result){
				$response["succsses"]=1;
				$response["myCards"]=$result;
			}
			else	
				$response["succsses"]=0;
			
echo json_encode($response);
/*$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
$userId=$_GET["user_id"];
mysqli_set_charset($con,"utf8");
$sql_query = "SELECT DISTINCT a.category_id,b.category_name,a.card_name,a.card_img FROM cards AS a, category AS b WHERE a.user_id='$userId' AND a.category_id=b.category_id";  
$result = mysqli_query($con,$sql_query);  
if(mysqli_num_rows($result) >0 )  
{
	$response["myCards"]=array();
	$series=array();
	while($row=mysqli_fetch_array($result)){
		$series["cardsName"]=array();
		$series["cardsImage"]=array();
		while($row1=mysqli_fetch_array($result)){
			$series["cardsName"]=$row1["card_name"];
			$series["cardsImage"]=$row1["card_img"];
		}
		$series["category_id"]=$row["category_id"];
		$series["category_name"]=$row["category_name"];
		array_push($response["myCards"],$series);
	}

 $response["succsses"]=1;
 $jsonstring = json_encode($response);
 echo $jsonstring;
 }
 else  
 {   
 $response["succsses"]=0;
 echo json_encode($response);  
 }  */
?>
