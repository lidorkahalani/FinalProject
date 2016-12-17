<?php
try{
	include('connection.php');
	$category_id=$_POST['category_id'];
	$response=array();
	$deletFilesStatus=0;
	$allImageName=getImageNames($category_id);
	if(deletFIlesFromServer($allImageName[0]["card_img"])==1){
		if(deletFIlesFromServer($allImageName[1]["card_img"])==1){
			if(deletFIlesFromServer($allImageName[2]["card_img"])==1){
				if(deletFIlesFromServer($allImageName[3]["card_img"])==1){
					$deletFilesStatus=1;
				}
			}
		}
	}else {
			$response["succsses"]=-1;
	}
	if($deletFilesStatus==1){
		$deleteSeries = $con->exec("DELETE FROM cards WHERE category_id='$category_id'");
			if($deleteSeries === FALSE){
				$response["succsses"]=-1;
			}else{
			$deleteGame  = $con->exec("DELETE FROM category WHERE category_id='$category_id'");
			 if( $deleteGame !== FALSE ) {
					$response["succsses"]=1;
				} else {
					$response["succsses"]=0;
				}
			}
	}else {
			$response["succsses"]=0;
	}

    } catch (PDOException $e) {
       $response['succsses']=0;
    }
echo json_encode($response);


function deletFIlesFromServer($file_name){
		$res=0;
        $dir = "../images/";
        $dirHandle = opendir($dir);
        while ($file = readdir($dirHandle)) {
            if($file==$file_name) {
                unlink($dir."/".$file);//give correct path,
				$res=1;
            }
        }
        closedir($dirHandle);
		return $res;

}
function getImageNames($category_id){
	require('connection.php');
	$sth = $con->prepare("SELECT card_img FROM `cards` WHERE category_id='$category_id'");
	$sth->execute();
	$result = $sth->fetchAll(PDO::FETCH_ASSOC);

	if($result)
		return $result;
	else
		return null;
}

?>