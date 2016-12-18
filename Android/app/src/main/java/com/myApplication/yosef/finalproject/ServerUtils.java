package com.myApplication.yosef.finalproject;

/**
 * Created by Yosef on 03-Nov-16.
 */

public class ServerUtils {


     //public static String server ="http://192.168.1.13/final_project/db/";
    //public static String imageLoadServer="http://192.168.1.13/final_project/";

    //public static String server ="http://10.0.2.2/final_project/db/";
    //public static String imageLoadServer="http://10.0.2.2/final_project/";

    public static String server="http://mysite.lidordigital.co.il/Quertets/php/db/";
    public static String imageLoadServer="http://mysite.lidordigital.co.il/Quertets/php/";



    /*localhost*/

    public static final String imageRelativePat = imageLoadServer+"images/";
    public static final String getMaxCategoryId_url= server+"getMaxCategoryId.php";
    public static final String UPLOAD_URL = server +"AddCard.php";
    public static final String upload_series = server +"upload_series.php";
    public static final String setGameToActive = server +"setGameToActive.php";
    public static final String setGameToInactive = server +"setGameToInactive.php";
    public static final String get_all_card_url = server +"getAllCard.php";
    public static final String get4Cards = server +"giveMe4Cards.php";
    public static final String sendSelctedCard= server +"sendSelctedCard.php";
    public static final String takeOneCardFromDeck = server +"takeOneCardFromDeck.php";
    public static final String setTurnOrder = server +"moveToNextPlayer.php";
    public static final String isMyTurn = server +"isMyTurn.php";
    public static final String refresh_all = server +"refresh_all.php";
    public static final String UpdateFinishSeries = server +"UpdateFinishSeries.php";
    public static final String reg_url = server +"register.php";
    public static final String login_url = server +"login.php";
    public static final String checkIfRoomFull = server +"checkIfRoomFull.php";
    public static final String checkIfImAdmin = server +"checkIfAdmin.php";
    public static final String checkIfGameIsActive = server +"checkIfGameIsActive.php";
    public static final String checkIfRoomNameAvailable = server +"cheekIfRoomNameAvailable.php";
    public static final String delete_series = server +"deleteSeries.php";
    public static final String GetMySeries = server +"getMySeries.php";
    public static final String sendActiveSerie = server +"sendActiveSerie.php";
    public static final String getPlayerLIst= server +"getListNameOfThePLayerInRoom.php";
    public static final String joinToRoom = server +"joinToRoom.php";
    public static final String getAllUserList= server +"getAllUserList.php";
    public static final String GetWinnerName = server +"GetWinnerName.php";
    public static final String UpdateScore = server +"UpdateScore.php";
    public static final String UpdateSeries=server+"Update_series.php";
    public static final String setdeckIsOver=server+"setdeckIsOver.php";

    public static final String checkIfAllseriesComplite=server+"checkIfAllseriesComplite.php";


}
