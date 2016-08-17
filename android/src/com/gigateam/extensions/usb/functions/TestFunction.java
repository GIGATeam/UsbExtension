package com.gigateam.extensions.usb.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

/**
 * Created by TIGER on 10/8/2016.
 */
public class TestFunction implements FREFunction {
    public static final String KEY = "testFunctionKey";
    public FREObject call(FREContext arg0, FREObject[] arg1){
        try{
            return FREObject.newObject(true);
        }catch(Exception e){
        }
        return null;
    }
}
