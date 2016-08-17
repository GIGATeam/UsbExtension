package com.gigateam.extensions.usb.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 12/8/2016.
 */
public class SetActionUsbFunction implements FREFunction {
    public static final String KEY = "setActionUsbFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        FREObject ret = null;
        UsbContext usbCtx = (UsbContext) ctx;
        String action;
        try {
            action = args[0].getAsString();
        }catch (Exception e){
            return ret;
        }
        usbCtx.set_actionUsbPermission(action);
        try{
            ret = FREObject.newObject(true);
        } catch(FREWrongThreadException e){
        }
        return ret;
    }
}
