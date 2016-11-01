<?php
$con=mysqli_connect("localhost","root","","quartetsdb");
$response=array();
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userName = $_POST['username'];
$passWord = $_POST['password'];
$user_id=0;
$score=0;

if($userName==""||$passWord==""){
	echo "empty field";
	return;
}
mysqli_set_charset($con,"utf8");
$result = mysqli_query($con,"insert into users (user_name,user_password) values('$userName','$passWord')");

 $user_id=mysqli_insert_id($con);
 if($user_id!=0){
	 if($result == true) {
		$response["sucsses"]=1;
		$result = mysqli_query($con,"insert into score_tabel (user_id,score) values('$user_id','$score')");
		echo json_encode($response);
	}else{
	 $response["sucsses"]=0;
	 echo json_encode($response);
	}
}else{
	 $response["sucsses"]=0;
	 echo json_encode($response);
}
mysqli_close($con);
 
?>