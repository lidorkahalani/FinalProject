<?php
require_once('connection.php');
$game_id=$_POST['game_id'];
$response=array();
$query = $con->query("SELECT * FROM users WHERE users.user_id IN(SELECT game_users.user_id FROM game_users WHERE game_users.game_id='$game_id')");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);
$response["winner"]=$result;
echo json_encode($response);
?>