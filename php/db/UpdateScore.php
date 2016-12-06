<?php
	   require('connection.php');
	   $score=$_POST["score"];
	   $user_id=$_POST["user_id"];
	  // $game_id=$_POST["game_id"];
	   $response=array();
	   $res = $con->exec("UPDATE score_table SET score = '$score' WHEN '$score'>(SELECT score FROM score_table WHERE user_id='$user_id') AND user_id = '$user_id'");
        if(  $res !== FALSE ) {
          $response["succsses"]=1;
        }else {
           $response["succsses"]=0;
        }	
		echo json_encode($response);
?>