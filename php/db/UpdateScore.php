<?php
	   require('connection.php');
	   $score=$_POST["score"];
	   $user_id=$_POST["user_id"];
	   $response=array();
	   $res = $con->exec("UPDATE score_tabel SET score='$score' WHERE user_id='$user_id' AND score<'$score'");
        if(  $res>0 ) {
          $response["succsses"]=1;
        }else {
           $response["succsses"]=0;
        }	
		echo json_encode($response);
?>