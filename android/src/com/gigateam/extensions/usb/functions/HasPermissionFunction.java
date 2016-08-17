package com.gigateam.extensions.usb.functions;

import android.hardware.usb.UsbManager;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 12/8/2016.
 */
public class HasPermissionFunction implements FREFunction {
    public static final String KEY = "hasPermissionFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        FREObject ret = null;
        Integer index;
        Integer usbType;
        Boolean answer;
        UsbContext usbCtx = (UsbContext) ctx;
        UsbManager usbManager = usbCtx.getUsbManager();
        try {
            index = args[0].getAsInt();
            usbType = args[1].getAsInt();
        }catch (Exception e){
            return ret;
        }

        if(usbType==UsbContext.TYPE_DEVICE){
            answer = usbManager.hasPermission(usbCtx.getDeviceByIndex(index));
        }else{
            answer = usbManager.hasPermission(usbCtx.getAccessoryByIndex(index));
        }
        try{
            ret = FREObject.newObject(answer);
        } catch(FREWrongThreadException e){
        }
        return ret;
    }
}
