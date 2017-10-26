package com.example.champ.soothe_away;

import com.squareup.okhttp.MediaType;

/**
 * Created by CHAMP on 21/10/2560.
 */

public class Constant {
    //Shared Preferences
    final public static String MY_PREFERENCES = "prefs";
    final public static String USER_ID = "id";
    final public static String EMAIL = "email";





    final public static String OK = "OK";

    //login
    final public static String USERNAME_REQUIRE = "The email field is required";
    final public static String PASSWORD_REQUIRE = "The password field is required";
    final public static String PLEASE_WAIT = "Please wait";
    final public static String LOGIN_INCOMPLETE = "The  username or password is incorrect";
    final public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    //register
    final public static String FILED_REQUIRE = "Please provide all of information";
    final public static String PASSWORD_NOT_MATCH = "Password does not match the confirm password";
    final public static String REGISTER_INCOMPLETE = "Register incomplete";



    //web service
//    final public static String LOGIN = "http://rakjangs.is-by.us:2222/api/login/authenticate";
    final public static String URL = "http://172.20.10.13:8000";
    final public static String LOGIN = URL+"/api/login/authenticate";
    final public static String CONTENT_LIST = URL+"/api/massagecontent/getmassagecontentlist";
    final public static String CONTENT = URL+"/api/massagecontent/getmassagecontentbyid/{id}";
    final public static String MASSAGE_SHOP_LIST = URL+"/api/massageshop/getmassageshoplist";
    final public static String MASSAGE_SHOP = URL+"/api/massageshop/getmassageshopbyid/{id}";
    final public static String COMMENT = URL+"/api/massageshop/createcomment";
    final public static String CREATE_MEMBER = URL+"/api/member/store";
    final public static String EDIT_MEMBET = URL+"/api/member/update";




}
