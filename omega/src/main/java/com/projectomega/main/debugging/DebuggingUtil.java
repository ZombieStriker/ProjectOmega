package com.projectomega.main.debugging;

public class DebuggingUtil {

    public static final boolean DEBUG = false;
    public static boolean enableDebugPlugin = true;

    public static boolean isCharacter(char c){
        switch(c){
            case'q':
            case'w':
            case'e':
            case'r':
            case't':
            case'y':
            case'u':
            case'i':
            case'o':
            case'p':
            case'a':
            case's':
            case'd':
            case'f':
            case'g':
            case'h':
            case'j':
            case'k':
            case'l':
            case'z':
            case'x':
            case'c':
            case'v':
            case'b':
            case'n':
            case'm':
            case'_':
            case'-':
            case':':
            case'/':
            case'.':
                return true;
            default:
                return false;
        }
    }

    public static String dumpBytes(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++){
            char c = (char)bytes[i];
            if(isCharacter(c)){
                sb.append(c);
            }else{
                sb.append(bytes[i]+" ");
            }
        }
        return sb.toString();
    }
}
