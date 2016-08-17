package com.gigateam.extensions.usb.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.UsbEvent;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 12/8/2016.
 */
public class GetDebugStatusFunction implements FREFunction {
    public static final String KEY = "getDebugStatusFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        FREObject ret = null;
        UsbContext usbCtx = (UsbContext) ctx;
        try{
            ret = FREObject.newObject(usbCtx.debugStatus);
        }catch (FREWrongThreadException e){
        }
        return ret;
    }
}
