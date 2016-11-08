<?php
include('connection.php');
 $response=array();
 $game_id=$_POST['game_id'];
 
	   $sth = $con->prepare("SELECT * ,(SELECT case g.my_turn_user_id when 0 then min(user_id)	when max(user_id) then min(user_id)
				else (SELECT min(user_id) FROM game_users g2 where g2.game_id=g.game_id and g2.user_id > my_turn_user_id)end from game_users where game_id=g.game_id) as new_player FROM game g WHERE g.game_id = '$game_id'");
		$sth->execute();
		$result = $sth->fetch(PDO::FETCH_ASSOC);
		$newPlayerId=$result['new_player'];
		
		$res = $con->exec("UPDATE game SET my_turn_user_id = '$newPlayerId' WHERE game_id = '$game_id'");
			 if(  $res !== FALSE ) {
				$response["successes"]=1;
			}else {
			    $response["successes"]=0;
			}
		
		echo json_encode($response);

		


?>