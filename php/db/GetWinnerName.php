<?php
include('connection.php');
$game_id=$_POST['game_id'];
$response=array();
/*
$query = $con->query("Select distinct user_id,COUNT(series_complete) FROM games_cards Where series_complete=1 AND game_id=53 group by user_id");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);*/

//$query = $con->query("SELECT * FROM users WHERE user_id=( Select distinct user_id FROM games_cards Where series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete) ORDER by COUNT(series_complete) desc limit 1)");
$query = $con->query("Select distinct user_id FROM games_cards Where series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete) ORDER by COUNT(series_complete) desc limit 2;");
	$query->execute();
	$result = $query->fetchAll(PDO::FETCH_ASSOC);
	if($result!=null){
		$id1=$result[0]["user_id"];
		$id2=$result[1]["user_id"];
	
		$sth = $con->prepare("Select count(series_complete) as score FROM games_cards Where user_id='$id1' and series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete)");
		$sth->execute();
		$score1 = $sth->fetch(PDO::FETCH_ASSOC);
		
		$sth = $con->prepare("Select count(series_complete) as score FROM games_cards Where user_id='$id2' and series_complete=1 AND game_id='$game_id' group by user_id HAVING COUNT(series_complete)");
		$sth->execute();
		$score2 = $sth->fetch(PDO::FETCH_ASSOC);
		
		if($score1["score"]>$score2["score"]){
			$sth = $con->prepare("SELECT * FROM users WHERE user_id='$id1'");
			$sth->execute();
			$res = $sth->fetch(PDO::FETCH_ASSOC);
			$response["equals"]=0;
			$response["winner"]=$res;
		}else
			$response["equals"]=1;
		$response["successes"]=1;
	}else{
		$response["successes"]=0;
		$response["winner"]=-1;
	}
echo json_encode($response);
?>