<?php
$card_name=$_POST['card_name'];
$category_id=$_POST['category_id'];
//$category_id=getMaxCcategoryId();
$user_id=$_POST['user_id'];

$file_path = "../images/";
$card_img=basename( $_FILES['uploaded_file']['name']);
$file_path =$file_path.basename( $_FILES['uploaded_file']['name']);

$response=array();
if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
	  $host='localhost';
	  $username='root';
	  $password='';
	  $dbname='quartetsdb';
	  $link=mysqli_connect($host,$username,$password,$dbname);
	  mysqli_query($link,"INSERT INTO `cards` (card_name,category_id,card_img,user_id) VALUES ('$card_name','$category_id','$card_img','$user_id')") or trigger_error($link->error."[ $sql]");
	  $response["succsses"]=1;
	  mysqli_close($link);
} else{
    $response["succsses"]=0;
}
echo json_encode($response);
mysqli_close($link);

function getMaxCcategoryId(){
	require('connection.php');
	$sth = $con->prepare("SELECT MAX(category_id) FROM cards");
			$sth->execute();
			$result = $sth->fetchColumn();
			return $result;
	mysqli_close($con);
}
?>