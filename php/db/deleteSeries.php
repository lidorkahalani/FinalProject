<?php
try{
	include('connection.php');
	$category_id=$_POST['category_id'];
	
	$response=array();	
		 $deleteSeries = $con->exec("DELETE FROM cards WHERE category_id='$category_id'");
			if($deleteSeries === FALSE){
				$response["succsses"]=-1;
			}else{
			// if all is right when deleting questions, procceed to deleting the game itself
			$deleteGame  = $con->exec("DELETE FROM category WHERE category_id='$category_id'");
			 if( $deleteGame !== FALSE ) {
					$response["succsses"]=1;
				} else {
					$response["succsses"]=0;
				}
			}

    } catch (PDOException $e) {
       $response['succsses']=0;
    }
echo json_encode($response);
	
?>