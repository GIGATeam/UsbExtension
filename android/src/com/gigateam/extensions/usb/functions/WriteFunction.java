package com.gigateam.extensions.usb.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 15/8/2016.
 */
public class WriteFunction implements FREFunction {
    public static final String KEY = "writeFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args) {
        UsbContext usbCtx = (UsbContext) ctx;
        FREObject ret = null;
        String data;
        try {
            data = args[0].getAsString();
        }catch (Exception e){
            return ret;
        }
        synchronized (this) {
            usbCtx.getSerialPort().write(data.getBytes());
            try {
                ret = FREObject.newObject(true);
            } catch (FREWrongThreadException e) {
            }
        }
        return ret;
    }
}
