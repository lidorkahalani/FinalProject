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

$imageFileType1 = pathinfo($file_path1,PATHINFO_EXTENSION);
$imageFileType2 = pathinfo($file_path2,PATHINFO_EXTENSION);
$imageFileType3 = pathinfo($file_path3,PATHINFO_EXTENSION);
$imageFileType4 = pathinfo($file_path4,PATHINFO_EXTENSION);

$newImageToUpload1=0;
$newImageToUpload2=0;
$newImageToUpload3=0;
$newImageToUpload4=0;

//echo json_encode($imageFileType1);
/*
if($imageFileType1==='txt')
	echo json_encode(1);
else
	echo json_encode(0);
*/

//echo json_encode($allCardsIds["card_id"]);

$status=false;

$card_id=$allCardsIds[0]["card_id"];
if($imageFileType1=="txt")
	$query1="UPDATE cards set card_name='$card1' WHERE card_id='$card_id'";
else{
	$newImageToUpload1=1;
	$query1="UPDATE cards set card_name='$card1',card_img='$card_img1' WHERE card_id='$card_id'";
}


$card_id=$allCardsIds[1]["card_id"];
if($imageFileType2=="txt")
	$query2="UPDATE cards set card_name='$card2' WHERE card_id='$card_id'";
else{
	$newImageToUpload2=1;
	$query2="UPDATE cards set card_name='$card2',card_img='$card_img2' WHERE card_id='$card_id'";
}

$card_id=$allCardsIds[2]["card_id"];
if($imageFileType3=="txt")
	$query3="UPDATE cards set card_name='$card3' WHERE card_id='$card_id'";
else{
	$newImageToUpload3=1;
	$query3="UPDATE cards set card_name='$card3',card_img='$card_img3' WHERE card_id='$card_id'";
}

$card_id=$allCardsIds[3]["card_id"];
if($imageFileType4=="txt")
	$query4="UPDATE cards set card_name='$card4' WHERE card_id='$card_id'";
else{
	$newImageToUpload4=1;
	$query4="UPDATE cards set card_name='$card4',card_img='$card_img4' WHERE card_id='$card_id'";
}



$host='localhost';
$username='root';
$password='';
$dbname='quartetsdb';
$link=mysqli_connect($host,$username,$password,$dbname);

if($newImageToUpload1==1){
	move_uploaded_file($image1['tmp_name'], $file_path1);
	$status=mysqli_query($link,$query1);
}else
	$status=(mysqli_query($link,$query1));


if($status){
	$status=false;
	if($newImageToUpload2==1){
		move_uploaded_file($image2['tmp_name'], $file_path2);
		$status=mysqli_query($link,$query2);
	}else
		$status=mysqli_query($link,$query2);
}

if($status){
	$status=false;
	if($newImageToUpload3==1){
		move_uploaded_file($image3['tmp_name'], $file_path3);
		$status=mysqli_query($link,$query3);
	}else
		$status=mysqli_query($link,$query3);
}

if($status){
	$status=false;
	if($newImageToUpload4==1){
		move_uploaded_file($image4['tmp_name'], $file_path4);
		$status=mysqli_query($link,$query4);
	}else
		$status=mysqli_query($link,$query4);
}

if($status)
    $res="1";
	//$response["succese"]=1;
else
    $res="0";
	//$response["succese"]=0;
	
	 
mysqli_close($link);
echo json_encode($res);

function getCardId($category_id){
	require('connection.php');
	$sth = $con->prepare("SELECT card_id FROM cards where category_id='$category_id'");
	$sth->execute();
	$result = $sth->fetchAll(PDO::FETCH_ASSOC);
	if($result!=null){
		return $result;
	}else{
		return null;
		}
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