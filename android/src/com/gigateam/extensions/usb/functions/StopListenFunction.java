package com.gigateam.extensions.usb.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 12/8/2016.
 */
public class StopListenFunction implements FREFunction {
    public static final String KEY = "stopListenFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        FREObject ret = null;
        UsbContext usbCtx = (UsbContext) ctx;
        usbCtx.stopListen();
        try{
            ret = FREObject.newObject(true);
        }catch (FREWrongThreadException e){
        }
        return ret;
    }
}
