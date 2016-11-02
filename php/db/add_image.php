<?php
require('connection.php');
if(isset($_POST['image']){
$image=$_POST['image'];
$path="../images"
 move_uploaded_file($image,$path);
 $location=$path."/".$image;
 $insert=$con->exec("INSERT INTO card_img (game_id,user_id) VALUES('$id','$user_id')");
}
?>