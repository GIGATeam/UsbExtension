package com.gigateam.extensions.usb;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TIGER on 12/8/2016.
 */
public class UsbEvent extends JSONObject {
    public static final String SERIAL_DATA = "serialData";
    public static final String USB_STATE = "usbState";
    public static final String USB_ATTACHED = "usbAttached";
    public static final String USB_DETACHED = "usbDetached";
    public static final String GRANT_PERMISSION = "grantPermission";
    public Integer endType;
    private UsbDevice usbDevice;
    private UsbAccessory usbAccessory;
    @Override
    public String toString(){
        try {
            put("endpoint", usbEndToJson());
            put("type", endType);
        }catch(JSONException e){
        }
        return super.toString();
    }
    private JSONObject usbEndToJson(){
        JSONObject end = new JSONObject();
        if(usbDevice!=null) {
            addUsbToJson(usbDevice, end);
        }else if(usbAccessory!=null){
            addUsbToJson(usbAccessory, end);
        }
        return end;
    }
    public static void addUsbToJson(UsbDevice usbDevice, JSONObject json){
        try {
            json.put("id", usbDevice.getDeviceId());
            json.put("vendorId", usbDevice.getVendorId());
            json.put("productId", usbDevice.getProductId());
            json.put("hashCode", usbDevice.hashCode());
        }catch(JSONException e){
        }
    }
    public static void addUsbToJson(UsbAccessory usbAccessory, JSONObject json){
        try {
            json.put("serial", usbAccessory.getSerial());
            json.put("uri", usbAccessory.getUri());
            json.put("hashCode", usbAccessory.hashCode());
        }catch(JSONException e){
        }
    }
    public void set_usbEnd(UsbDevice end){
        usbDevice = end;
        usbAccessory = null;
    }
    public void set_usbEnd(UsbAccessory end){
        usbAccessory = end;
        usbDevice = null;
    }
}
