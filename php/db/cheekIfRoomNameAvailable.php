<?php
require_once('connection.php');
	$sth = $con->prepare("SELECT is_active FROM game where game_name='$req_Room_name'");
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC);
	if($result)
		return $result['is_active'];
?>