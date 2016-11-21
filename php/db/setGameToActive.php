<?php
require_once('connection.php');
$opertion=$_POST['opertion'];
$game_id=$_POST['game_id'];
$response=array();
$res = $con->exec("UPDATE game SET is_active = '$opertion' WHERE game_id ='$game_id'");
        if(  $res !== FALSE ) {
          $response["succsses"]=1;
        }else {
           $response["succsses"]=0;
        }	
echo json_encode($response);
?>