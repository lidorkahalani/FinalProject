package com.example.yosef.finalproject;

/**
 * Created by Yosef on 03-Nov-16.
 */

public class ServerUtils {


    public static String server ="http://10.0.2.2/final_project/db/";
    public static String imageLoadServer="http://10.0.2.2/final_project/";

    //public static String server="http://mysite.lidordigital.co.il/Quertets/php/db/";
   //public static String imageLoadServer="http://mysite.lidordigital.co.il/Quertets/php/";



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


    /*server*/
/*
    public static final String getMaxCategoryId_url="http://mysite.lidordigital.co.il/Quertets/db/php/getMaxCategoryId.php";
    public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/db/php/AddCard.php";
    public static final String upload_series = "http://mysite.lidordigital.co.il/Quertets/php/db/upload_series.php";
    public static final String setGameToActive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToActive.php";
    public static final String setGameToInactive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToInactive.php";
    public static final String imageRelativePat = "http://mysite.lidordigital.co.il/Quertets/php/images/";
    public static final String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/php/db/getAllCard.php";
    public static final String get4Cards = "http://mysite.lidordigital.co.il/Quertets/php/db/giveMe4Cards.php";
    public static final String sendSelctedCard="http://mysite.lidordigital.co.il/Quertets/php/db/sendSelctedCard.php";
    public static final String takeOneCardFromDeck = "http://mysite.lidordigital.co.il/Quertets/php/db/takeOneCardFromDeck.php";
    public static final String setTurnOrder = "http://mysite.lidordigital.co.il/Quertets/php/db/moveToNextPlayer.php";
    public static final String isMyTurn = "http://mysite.lidordigital.co.il/Quertets/php/db/isMyTurn.php";
    public static final String refresh_all = "http://mysite.lidordigital.co.il/Quertets/php/db/refresh_all.php";
    public static final String UpdateFinishSeries = "http://mysite.lidordigital.co.il/Quertets/php/db/UpdateFinishSeries.php";
    public static final String reg_url = "http://mysite.lidordigital.co.il/Quertets/php/db/register.php";
    public static final String login_url = "http://mysite.lidordigital.co.il/Quertets/php/db/login.php";
    public static final String checkIfRoomFull = "http://mysite.lidordigital.co.il/Quertets/php/db/checkIfRoomFull.php";
    public static final String checkIfImAdmin = "http://mysite.lidordigital.co.il/Quertets/php/db/checkIfAdmin.php";
    public static final String checkIfGameIsActive = "http://mysite.lidordigital.co.il/Quertets/php/db/checkIfGameIsActive.php";
    public static final String checkIfRoomNameAvailable="http://mysite.lidordigital.co.il/Quertets/php/db/cheekIfRoomNameAvailable.php";
    public static final String delete_series = "http://mysite.lidordigital.co.il/Quertets/php/db/deleteSeries.php";
    public static final String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/php/db/getMySeries.php";
    public static final String sendActiveSerie = "http://mysite.lidordigital.co.il/Quertets/php/db/sendActiveSerie.php";
    public static final String getPlayerLIst="http://mysite.lidordigital.co.il/Quertets/php/db/getListNameOfThePLayerInRoom.php";
    public static final String joinToRoom = "http://mysite.lidordigital.co.il/Quertets/php/db/joinToRoom.php";
    public static final String getAllUserList="http://mysite.lidordigital.co.il/Quertets/php/db/getAllUserList.php";
    public static final String GetWinnerName = "http://mysite.lidordigital.co.il/Quertets/php/db/GetWinnerName.php";
    public static final String UpdateScore = "http://mysite.lidordigital.co.il/Quertets/php/db/UpdateScore.php";
    */





}
