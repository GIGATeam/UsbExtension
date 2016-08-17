package com.gigateam.extensions.usb.functions;

import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 14/8/2016.
 */
public class ConnectFunction implements FREFunction{
    public static final String KEY = "connectFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        UsbContext usbCtx = (UsbContext) ctx;
        FREObject ret = null;
        Integer index;
        Integer usbType;
        Integer baud;
        try {
            index = args[0].getAsInt();
            usbType = args[1].getAsInt();
            baud = args[2].getAsInt();
        }catch (Exception e){
            return ret;
        }
        if(usbType==UsbContext.TYPE_DEVICE) {
            UsbDevice device = usbCtx.getDeviceByIndex(index);
            Boolean result = usbCtx.connectSerial(device, baud);
            try{
                ret = FREObject.newObject(result);
            }catch(FREWrongThreadException e){
            }
        }
        return ret;
    }
}
