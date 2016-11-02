<?php
static $rangeCnt=0;
$counter=0;
require_once('connection.php');
$roomName=$_POST['room_name']; //game_name=room_name
$game_id=getGameId($roomName);
$allRoomPlayer=array();
$allRoomPlayer=getAllPlayerInCurrentRoom($game_id);

if(countPlayerInRoom($game_id)==4){// minnig start the game
	while($counter<4){
		randomeShareCardsForEachPlayer($allRoomPlayer[$counter],$game_id);
		$counter++;
	}
}else
	echo json_encode(0);

function getGameId($roomName){
	require('connection.php');
	$sth = $con->prepare("SELECT game_id FROM game where game_name='$roomName' AND is_active=1");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result['game_id'];
	}
	else{
		return null;
		}
}
function countPlayerInRoom($game_id){
	require('connection.php'); 
	$result = $con->query("SELECT COUNT(*) FROM game_users WHERE game_id=game_id")->fetchColumn();	
	return $result;
}
function randomeShareCardsForEachPlayer($user_id,$game_id){
	require('connection.php');
	$cnt=0;
	$numbers = range(1, (32-$rangeCnt));
	while($cnt<4){
		$randCard=shuffle($numbers);
		$res = $con->exec("UPDATE games_cards SET user_id = '$user_id' WHERE game_id = '$game_id' AND card_id='$randCard'");
        if(  $res !== FALSE ) {
           $cnt++;
        }else {
           echo -1;
        }	
	}
	$rangeCnt+=4;
}
function getAllPlayerInCurrentRoom($game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT user_id FROM game_users where game_id='$game_id'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result;
	}else{
		return null;
	}
}
function moveCardToCurrentPlayerFromOtherPlayer($getCardPlayerId,$card_id,$game_id){
	   require('connection.php');
	   $res = $con->exec("UPDATE games_cards SET user_id = '$getCardPlayerId' WHERE game_id = '$game_id' AND card_id='$card_id'");
        if(  $res !== FALSE ) {
           echo 1;
        }else {
           echo -1;
        }	
}
function takeCardFromDeck($user_id,$game_id){
	  $card_id=getunUsedCard();
	   require('connection.php');
	   $res = $con->exec("UPDATE games_cards SET user_id = '$user_id' WHERE game_id = '$game_id' AND card_id='$card_id'");
        if(  $res !== FALSE ) {
           echo 1;
        }else {
           echo -1;
        }	
}
function getunUsedCard($game_id){
	require('connection.php');
	$sth = $con->prepare("SELECT card_id FROM games_cards where game_id='$game_id' AND user_id=0");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result){
		return $result[0];
	}else{
		return null;
	}
}
?