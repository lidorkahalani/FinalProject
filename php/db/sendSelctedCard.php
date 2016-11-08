<?php
 require('connection.php');
 $response=array();
 $game_id=$_POST['game_id'];
 $card_id=$_POST['card_id'];
 $current_player_user_id=getActivePlayerId($game_id);
 
 if($current_player_user_id!=0){
	   $res = $con->exec("UPDATE games_cards SET user_id = '$current_player_user_id' WHERE game_id = '$game_id' AND card_id='$card_id'");
        if(  $res !== FALSE ) {
          $response["successes"]=1;
        }else {
           $response["successes"]=0;
        }
 }else
	 $response["successes"]=3;
		echo json_encode($response);
		
function getActivePlayerId($game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT my_turn_user_id FROM game where game_id='$game_id'");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['my_turn_user_id']!=0){
				return $result['my_turn_user_id'];
			}else{
				return 0;
			}
}
		
?>