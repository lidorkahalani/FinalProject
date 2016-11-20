<?php
$category_id=$_POST['category_id'];
$categoryName=$_POST['category_name'];

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

$file_path = "../images/";
$card_img1=basename( $image1['name']);
$card_img2=basename( $image2['name']);
$card_img3=basename( $image3['name']);
$card_img4=basename( $image4['name']);
$file_path1 =$file_path.$card_img1;
$file_path2 =$file_path.$card_img2;
$file_path3=$file_path.$card_img3;
$file_path4 =$file_path.$card_img4;


$query1="INSERT INTO `cards` (card_name,category_id,card_img,user_id) VALUES ('$card1','$category_id','$card_img1','$user_id')";
$query2="INSERT INTO `cards` (card_name,category_id,card_img,user_id) VALUES ('$card2','$category_id','$card_img2','$user_id')";
$query3="INSERT INTO `cards` (card_name,category_id,card_img,user_id) VALUES ('$card3','$category_id','$card_img3','$user_id')";
$query4="INSERT INTO `cards` (card_name,category_id,card_img,user_id) VALUES ('$card4','$category_id','$card_img4','$user_id')";
$final_query="INSERT into category (category_name,category_color) VALUES('$categoryName','$category_color')";

if(move_uploaded_file($image1['tmp_name'], $file_path1)) {
	  $host='localhost';
	  $username='root';
	  $password='';
	  $dbname='quartetsdb';
	  $link=mysqli_connect($host,$username,$password,$dbname);
	   // or trigger_error($link->error."[ $sql]");
	  if(mysqli_query($link,$query1)){
		  $file_path = "../images/";
			//$card_img=basename( $image2['name']);
			//$file_path =$file_path.basename( $image2['name']);
			move_uploaded_file($image2['tmp_name'], $file_path2);
	  }
		  if(mysqli_query($link,$query2)){
			$file_path = "../images/";
			//$card_img=basename( $image3['name']);
			//$file_path =$file_path.basename( $image3['name']);
			move_uploaded_file($image3['tmp_name'], $file_path3);
			}
			  if(mysqli_query($link,$query3)){
				  $file_path = "../images/";
				  //$card_img=basename( $image4['name']);
				//$file_path =$file_path.basename( $image4['name']);
				move_uploaded_file($image4['tmp_name'], $file_path4);
			  }
				  if(mysqli_query($link,$query4)){
					  
					   if(mysqli_query($link,$final_query))
						$response["succsses"]=1;
				  }
		 
	  mysqli_close($link);
} else{
    $response["succsses"]=0;
}
echo json_encode($response);

?>