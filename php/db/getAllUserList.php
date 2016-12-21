<?php
require_once('connection.php');
$response=array();
$query = $con->query("SELECT users.user_id,users.user_name,users.user_password, score_tabel.score
						FROM users 
						INNER JOIN score_tabel
						ON users.user_id=score_tabel.user_id AND score_tabel.score>0 score_tabel.score desc LIMIT 10");
$query->execute();
$result = $query->fetchAll(PDO::FETCH_ASSOC);
$response["usersList"]=$result;
echo json_encode($response);
?>