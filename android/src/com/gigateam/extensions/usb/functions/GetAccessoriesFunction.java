package com.gigateam.extensions.usb.functions;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by TIGER on 10/8/2016.
 */
public class GetAccessoriesFunction implements FREFunction {
    public static final String KEY = "getAccessoriesFunctionKey";
    public FREObject call(FREContext ctx, FREObject[] args){
        UsbContext usbCtx = (UsbContext) ctx;
        ArrayList<UsbAccessory> accessories = usbCtx.usbAccessories();
        Iterator<UsbAccessory> accessoryIterator = accessories.iterator();
        JSONArray jsonArray = new JSONArray();
        while (accessoryIterator.hasNext()) {
            UsbAccessory accessory = accessoryIterator.next();
            JSONObject jsonObject = new JSONObject();
            UsbEvent.addUsbToJson(accessory, jsonObject);
            jsonArray.put(jsonObject);
        }
        try{
            return FREObject.newObject(jsonArray.toString());
        }catch(FREWrongThreadException e){
        }
        return null;
    }
}
