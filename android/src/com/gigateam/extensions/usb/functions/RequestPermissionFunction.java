package com.gigateam.extensions.usb.functions;

import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.usb.UsbManager;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.UsbEvent;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 12/8/2016.
 */
public class RequestPermissionFunction implements FREFunction {
    public static final String KEY = "requestPermissionFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        FREObject ret = null;
        UsbContext usbCtx = (UsbContext) ctx;
        Integer index;
        Integer usbType;
        try {
            index = args[0].getAsInt();
            usbType = args[1].getAsInt();
        }catch (Exception e){
            return ret;
        }
        Intent passIntent = new Intent(usbCtx.get_actionUsbPermission());
        PendingIntent pi;
        passIntent.putExtra(UsbContext.USB_TYPE, usbType);
        if(usbType==UsbContext.TYPE_DEVICE) {
            pi = PendingIntent.getBroadcast(ctx.getActivity(), 0, passIntent, 0);
            usbCtx.getUsbManager().requestPermission(usbCtx.getDeviceByIndex(index), pi);
        }else{
            pi = PendingIntent.getBroadcast(ctx.getActivity(), 0, passIntent, 0);
            usbCtx.getUsbManager().requestPermission(usbCtx.getAccessoryByIndex(index), pi);
        }
        try{
            ret = FREObject.newObject(true);
        } catch(FREWrongThreadException e){
        }
        return ret;
    }
}
