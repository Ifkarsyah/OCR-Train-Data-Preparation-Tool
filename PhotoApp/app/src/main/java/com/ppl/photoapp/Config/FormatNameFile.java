package com.ppl.photoapp.Config;

public class FormatNameFile
{
    public static String SubFolder(int i){
        return Config.SUB_FOLDER + i ;
    }

    public static String RootFolder(String root){
        return root + "/" + Config.ROOT_FOLDER ;
    }

    public static boolean isAvaiableExtension(String extentsionFile){
        return extentsionFile.equals(Config.EXTENTION_FILE) ;
    }
}
