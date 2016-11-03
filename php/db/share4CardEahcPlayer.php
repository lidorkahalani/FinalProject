<?php
include('connection.php');
	 
$user_id=$_POST('user_id');
$game_id=$_POST('game_id');
		$cnt=0;
		  while($cnt<4){
			   $randCard=checkIfCardAvailable();
			    if($randCard!=0){
					$res = $con->exec("UPDATE games_cards SET user_id = '$user_id' WHERE game_id = '$game_id' AND card_id=$randCard");
					if($res==1 ){//not all player get 4 cards
					   $cnt++;
					}else 
					   return false;
				}else 
					 continue;
		  }

function checkIfCardAvailable(){
			include('connection.php');
			$randCard=rand(1, 32);
			$sth = $con->prepare("SELECT user_id FROM games_cards where card_id='$randCard'");
			$sth->execute();
			$result = $sth->fetchAll();
			if($result){
				return $randCard;
			}else{
				return 0;
			}
}
?>