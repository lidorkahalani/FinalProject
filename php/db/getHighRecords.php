<?php
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
mysqli_set_charset($con,"utf8");
$sql_query= "select user_name,user_password,score from users as a,score_tabel as b where a.user_id=b.user_id order by Score desc  LIMIT 10";
$result = mysqli_query($con,$sql_query);  
if(mysqli_num_rows($result) >0 )  
{
	$response["HighesRecordesUsers"]=array();
	$user=array();
	while($row=mysqli_fetch_array($result)){
		$user["user_name"]=$row["user_name"];
	    $user["user_password"]=$row["user_password"];
		$user["score"]=$row["score"];
		array_push($response["HighesRecordesUsers"],$user);
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
mysqli_close($con); 
?>