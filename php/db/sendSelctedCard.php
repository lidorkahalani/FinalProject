<?php
 require('connection.php');
 $response=array();
 $game_id=$_POST['$game_id'];
 $card_id=$_POST['$card_id'];
 $get_the_card_user_id=getActivePlayerId();
 
 if($get_the_card_user_id!=0){
	   $res = $con->exec("UPDATE games_cards SET user_id = '$get_the_card_user_id' WHERE game_id = '$game_id' AND card_id='$card_id'");
        if(  $res !== FALSE ) {
          $response["successes"]=1;
        }else {
           $response["successes"]=0;
        }
 }else
	 $response["successes"]=0;
		echo json_encode($response);
		
function getActivePlayerId($game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT user_id FROM game_users where game_id='$game_id' AND my_turn=1");
			$sth->execute();
			$result = $sth->fetch(PDO::FETCH_ASSOC);
			if($result['user_id']!=0){
				return $result['user_id'];
			}else{
				return 0;
			}
}
		
<?