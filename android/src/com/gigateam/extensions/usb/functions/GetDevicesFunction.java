package com.gigateam.extensions.usb.functions;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.gigateam.extensions.usb.UsbEvent;
import com.gigateam.extensions.usb.contexts.UsbContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by TIGER on 10/8/2016.
 */
public class GetDevicesFunction implements FREFunction {
    public static final String KEY = "getDevicesFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        UsbContext usbCtx = (UsbContext) ctx;
        Iterator<UsbDevice> deviceIterator = usbCtx.usbDevices().iterator();
        JSONArray jsonArray = new JSONArray();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            JSONObject jsonObject = new JSONObject();
            UsbEvent.addUsbToJson(device, jsonObject);
            jsonArray.put(jsonObject);
        }
        try{
            return FREObject.newObject(jsonArray.toString());
        }catch(FREWrongThreadException e){
        }
        return null;
    }
}
