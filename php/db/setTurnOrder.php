<?php
require('connection.php');
 $response=array();
 $game_id=$_POST['$game_id']; 

	   $res = $con->exec("UPDATE game_users SET my_turn = 1 WHERE game_id = '$game_id'");
        if(  $res !== FALSE ) {
          $response["successes"]=1;
        }else {
           $response["successes"]=0;
        }

	 $response["successes"]=0;
		echo json_encode($response);
		

?>