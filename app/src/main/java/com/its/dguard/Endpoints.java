package com.its.dguard;

public class Endpoints {
    private static final String ROOT_URL = "http://bryanyehuda.my.id/upload/";
    public static final String UPLOAD_IMAGE_URL = ROOT_URL + "image.php?apicall=uploadpic";
    public static final String GET_PICS_URL = ROOT_URL + "image.php?apicall=getpics";
    public static final String UPLOAD_FILE_URL = ROOT_URL + "file.php?apicall=uploadfile";
    public static final String GET_FILE_URL = ROOT_URL + "file.php?apicall=getfile";
}