<?php
//include('connection.php');
$category_id=$_POST['category_id'];
$user_id=$_POST['user_id'];
$card1=$_POST['card1'];
$card2=$_POST['card2'];
$card3=$_POST['card3'];
$card4=$_POST['card4'];

$image1=$_FILES['image1'];
$image2=$_FILES['image2'];
$image3=$_FILES['image3'];
$image4=$_FILES['image4'];

$category_color=111;
$response=array();
$allCardsIds=getCardId($category_id);
$card_id=$allCardsIds[0]["card_id"];
$file_path = "../images/";
$card_img1=basename( $image1['name']);
$card_img2=basename( $image2['name']);
$card_img3=basename( $image3['name']);
$card_img4=basename( $image4['name']);

$file_path1 =$file_path.$card_img1;
$file_path2 =$file_path.$card_img2;
$file_path3 =$file_path.$card_img3;
$file_path4 =$file_path.$card_img4;



$query1="UPDATE cards set card_name='$card1' WHERE card_id='$card_id'";
$query2="UPDATE cards set card_name='$card2' WHERE card_id='$card_id'";
$query3="UPDATE cards set card_name='$card3' WHERE card_id='$card_id'";
$query4="UPDATE cards set card_name='$card4' WHERE card_id='$card_id'";

$host='localhost';
$username='root';
$password='';
$dbname='quartetsdb';
$link=mysqli_connect($host,$username,$password,$dbname);

UploadImage($card_img1,$file_path1);
	  if($card_id!=null){
		  if(mysqli_query($link,$query1)){
			  $file_path = "../images/";
			  $card_id=$allCardsIds[1]["card_id"];
			  if($card_id!=null){
				  UploadImage($card_img2,$file_path2);
				  if(mysqli_query($link,$query2)){
					$file_path = "../images/";
					$card_id=$allCardsIds[2]["card_id"];
					 if($card_id!=null){
						UploadImage($card_img3,$file_path3);
						if(mysqli_query($link,$query3)){
							  $file_path = "../images/";
							  $card_id=$allCardsIds[3]["card_id"]; 
						  if($card_id!=null){
							  UploadImage($card_img4,$file_path4);
							  if(mysqli_query($link,$query4)){
									$response=1;
						  }else
							  $response=0;
						}else
							$response=0;
					  }else
						  $response=0;
					}else
						 $response=0;
				  }else
						$response=0;
				}else
					$response=0;
			}else
			  $response=6;
		
	  }else
		  $response=0;	
  

echo json_encode($response);
mysqli_close($link);

function getCardId($category_id){
	require('connection.php');
	$sth = $con->prepare("SELECT card_id FROM cards where category_id='$category_id'");
	$sth->execute();
	$result = $sth->fetchAll(PDO::FETCH_ASSOC);
	if($result!=null){
		return $result;
	}
	else{
		return null;
		}
}
function UploadImage($card_image,$file_path){
if($card_image=="")
		return -1;
if(move_uploaded_file($card_image, $file_path))
		return 1;
else
	return 0;
}
function GetUpdateFields($card_name, $card_image,$file_path) {
	$updateFields1 = "";
	
	$updateFields1 = "card_name='$card_name'";
	$updateFields1 .= ", ";
	if ($card_image!=""){
		/*if ($updateFields1 != '') {
			$updateFields1 .= ", ";
		}*/
	if(move_uploaded_file($card_image, $file_path))
		return true;
		
		$updateFields1 .= "card_img='$card_image'";
	}
	return $updateFields1;
}
?>