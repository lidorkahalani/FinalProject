package com.example.yosef.finalproject;

/**
 * Created by Yosef on 03-Nov-16.
 */

public class ServerUtils {

    /*localhost*/

    public static final String getMaxCategoryId_url="http://10.0.2.2/final_project/db/getMaxCategoryId.php";
    public static final String UPLOAD_URL = "http://10.0.2.2/final_project/db/AddCard.php";
    public static final String upload_series = "http://10.0.2.2/final_project/db/upload_series.php";
    public static final String setGameToActive = "http://10.0.2.2/final_project/db/setGameToActive.php";
    public static final String setGameToInactive = "http://10.0.2.2/final_project/db/setGameToInactive.php";
    public static final String imageRelativePat = "http://10.0.2.2/final_project/images/";
    public static final String get_all_card_url = "http://10.0.2.2/final_project/db/getAllCard.php";
    public static final String get4Cards = "http://10.0.2.2/final_project/db/giveMe4Cards.php";
    public static final String sendSelctedCard="http://10.0.2.2/final_project/db/sendSelctedCard.php";
    public static final String takeOneCardFromDeck = "http://10.0.2.2/final_project/db/takeOneCardFromDeck.php";
    public static final String setTurnOrder = "http://10.0.2.2/final_project/db/moveToNextPlayer.php";
    public static final String isMyTurn = "http://10.0.2.2/final_project/db/isMyTurn.php";
    public static final String refresh_all = "http://10.0.2.2/final_project/db/refresh_all.php";
    public static final String UpdateFinishSeries = "http://10.0.2.2/final_project/db/UpdateFinishSeries.php";
    public static final String reg_url = "http://10.0.2.2/final_project/db/register.php";
    public static final String login_url = "http://10.0.2.2/final_project/db/login.php";
    public static final String checkIfRoomFull = "http://10.0.2.2/final_project/db/checkIfRoomFull.php";
    public static final String checkIfImAdmin = "http://10.0.2.2/final_project/db/checkIfAdmin.php";
    public static final String checkIfGameIsActive = "http://10.0.2.2/final_project/db/checkIfGameIsActive.php";
    public static final String checkIfRoomNameAvailable = "http://10.0.2.2/final_project/db/cheekIfRoomNameAvailable.php";
    public static final String delete_series = "http://10.0.2.2/final_project/db/deleteSeries.php";
    public static final String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
    public static final String sendActiveSerie = "http://10.0.2.2/final_project/db/sendActiveSerie.php";
    public static final String getPlayerLIst="http://10.0.2.2/final_project/db/getListNameOfThePLayerInRoom.php";
    public static final String joinToRoom = "http://10.0.2.2/final_project/db/joinToRoom.php";
    public static final String getAllUserList="http://10.0.2.2/final_project/db/getAllUserList.php";



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

*/



}
