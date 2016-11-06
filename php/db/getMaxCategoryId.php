<?php
require_once('connection.php');
$sth = $con->prepare("SELECT MAX(category_id) FROM cards");
			$sth->execute();
			$result = $sth->fetchColumn();
			echo json_encode($result);
?>