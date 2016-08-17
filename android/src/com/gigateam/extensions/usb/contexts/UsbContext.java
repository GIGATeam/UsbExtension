package com.gigateam.extensions.usb.contexts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.gigateam.extensions.usb.UsbEvent;
import com.gigateam.extensions.usb.functions.ClearFunction;
import com.gigateam.extensions.usb.functions.ConnectFunction;
import com.gigateam.extensions.usb.functions.GetAccessoriesFunction;
import com.gigateam.extensions.usb.functions.GetDebugStatusFunction;
import com.gigateam.extensions.usb.functions.GetDevicesFunction;
import com.gigateam.extensions.usb.functions.HasPermissionFunction;
import com.gigateam.extensions.usb.functions.RequestPermissionFunction;
import com.gigateam.extensions.usb.functions.SetActionUsbFunction;
import com.gigateam.extensions.usb.functions.StartListenFunction;
import com.gigateam.extensions.usb.functions.StopListenFunction;
import com.gigateam.extensions.usb.functions.WriteFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by TIGER on 10/8/2016.
 */
public class UsbContext extends FREContext implements IContext{
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    public static final String CTX_ID = "USB";
    public static final String USB_TYPE = "PLUGED_USB_TYPE";
    public static final Integer TYPE_DEVICE = 0;
    public static final Integer TYPE_ACCESSORY = 1;
    private String _actionUsbPermission;
    private String CTX_NAME;
    private UsbManager _usbManager;
    private ArrayList<UsbDevice> _usbDevices;
    private ArrayList<UsbAccessory> _usbAccessories;
    public String debugStatus = "";
    private UsbDevice _workingDevice;
    private UsbDeviceConnection _deviceConnection;
    private UsbSerialDevice _serialPort;
    private Integer seq = 0;
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            //UsbEvent usbEvent = new UsbEvent();
            //usbEvent.set_usbEnd(_workingDevice);
            try {
                //debugStatus = new String(arg0, "UTF-8");
                synchronized (this) {
                    debugStatus = new String(arg0);
                    dispatchStatusEventAsync(UsbEvent.SERIAL_DATA, debugStatus);
                }
            } catch (Exception e) {
            }
        }
    };
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbEvent usbEvent = new UsbEvent();
            if(ACTION_USB_STATE.equals(action)){
                synchronized (this){
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if(device!=null){
                        //do save device
                        usbEvent.endType = TYPE_DEVICE;
                        usbEvent.set_usbEnd(device);
                        dispatchStatusEventAsync(UsbEvent.USB_STATE, usbEvent.toString());
                    }
                }
            }else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        //do save device
                        usbEvent.endType = TYPE_DEVICE;
                        usbEvent.set_usbEnd(device);
                        dispatchStatusEventAsync(UsbEvent.USB_DETACHED, usbEvent.toString());
                    }
                }
            }else if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                synchronized (this){
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                        if(device != null) {
                            //do save device
                            usbEvent.endType = TYPE_DEVICE;
                            usbEvent.set_usbEnd(device);
                            dispatchStatusEventAsync(UsbEvent.USB_ATTACHED, usbEvent.toString());
                        }
                    }else{
                        //no permission
                    }
                }
            }else if(UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)){
                synchronized (this){
                    UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if(accessory!=null){
                        //do save device
                        usbEvent.endType = TYPE_ACCESSORY;
                        usbEvent.set_usbEnd(accessory);
                        dispatchStatusEventAsync(UsbEvent.USB_DETACHED, usbEvent.toString());
                    }
                }
            }else if(UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)){
                synchronized (this){
                    UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                        if(accessory != null) {
                            //do save device
                            usbEvent.endType = TYPE_ACCESSORY;
                            usbEvent.set_usbEnd(accessory);
                            dispatchStatusEventAsync(UsbEvent.USB_ATTACHED, usbEvent.toString());
                        }else{
                            //no permission
                        }
                    }
                }
            }else if(get_actionUsbPermission().equals(action)){
                synchronized (this){
                    Integer usbType = intent.getIntExtra(USB_TYPE, TYPE_DEVICE);
                    if(usbType!=TYPE_ACCESSORY){
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        usbEvent.endType = TYPE_DEVICE;
                        usbEvent.set_usbEnd(device);
                        dispatchStatusEventAsync(UsbEvent.GRANT_PERMISSION, usbEvent.toString());
                    }else{
                        UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                        usbEvent.endType = TYPE_ACCESSORY;
                        usbEvent.set_usbEnd(accessory);
                        dispatchStatusEventAsync(UsbEvent.GRANT_PERMISSION, usbEvent.toString());
                    }
                }
            }
        }
    };
    public void clear(){
        _usbDevices = null;
        _usbAccessories = null;
    }
    public UsbContext(String ext){
        CTX_NAME = ext;
    }
    public void dispose(){
        _usbManager = null;
        _usbDevices = null;
        _usbAccessories = null;
        stopListen();
    }
    public void set_actionUsbPermission(String actionUsbPermission){
        _actionUsbPermission = actionUsbPermission;
    }
    public String get_actionUsbPermission(){
        return _actionUsbPermission;
    }
    public UsbSerialDevice getSerialPort(){
        return _serialPort;
    }
    public Boolean connectSerial(UsbDevice device, Integer baud) {
        UsbDeviceConnection connection = connectDevice(device);
        UsbSerialDevice serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
        _workingDevice = device;
        if(!serialPort.open()){
            return false;
        }else {
            _serialPort = serialPort;
            serialPort.setBaudRate(baud);
            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
            serialPort.read(mCallback);
            return true;
        }
    }
    public UsbDeviceConnection connectDevice(UsbDevice device){
        _deviceConnection = getUsbManager().openDevice(device);
        return _deviceConnection;
    }
    public void startListen(){
        IntentFilter filter = new IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_STATE);
        filter.addAction(get_actionUsbPermission());
        //
        getActivity().registerReceiver(mUsbReceiver, filter);
    }
    public void stopListen(){
        getActivity().unregisterReceiver(mUsbReceiver);
    }
    public UsbManager getUsbManager(){
        if(_usbManager==null){
            _usbManager=(UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        }
        return _usbManager;
    }
    public ArrayList<UsbDevice> usbDevices(){
        if(_usbDevices==null){
            _usbDevices = new ArrayList<UsbDevice>();
            UsbManager usbManager = getUsbManager();
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            while(deviceIterator.hasNext()){
                _usbDevices.add(deviceIterator.next());
            }
        }
        return _usbDevices;
    }
    public ArrayList<UsbAccessory> usbAccessories(){
        if(_usbAccessories==null){
            _usbAccessories = new ArrayList<UsbAccessory>();
            UsbManager usbManager = getUsbManager();
            UsbAccessory accessories[] = usbManager.getAccessoryList();
            if(accessories==null){
                return _usbAccessories;
            }
            for(Integer i=0;i<accessories.length;i++){
                _usbAccessories.add(accessories[i]);
            }
        }
        return _usbAccessories;
    }
    public UsbDevice getDeviceByIndex(Integer index){
        return usbDevices().get(index);
    }
    public UsbAccessory getAccessoryByIndex(Integer index){
        return usbAccessories().get(index);
    }
    public Map<String, FREFunction> getFunctions(){
        Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();
        functionMap.put(GetDevicesFunction.KEY, new GetDevicesFunction() );
        functionMap.put(GetAccessoriesFunction.KEY, new GetAccessoriesFunction() );
        functionMap.put(RequestPermissionFunction.KEY, new RequestPermissionFunction() );
        functionMap.put(StartListenFunction.KEY, new StartListenFunction() );
        functionMap.put(StopListenFunction.KEY, new StopListenFunction() );
        functionMap.put(HasPermissionFunction.KEY, new HasPermissionFunction() );
        functionMap.put(SetActionUsbFunction.KEY, new SetActionUsbFunction() );
        functionMap.put(GetDebugStatusFunction.KEY, new GetDebugStatusFunction() );
        functionMap.put(ClearFunction.KEY, new ClearFunction() );
        functionMap.put(ConnectFunction.KEY, new ConnectFunction() );
        functionMap.put(WriteFunction.KEY, new WriteFunction() );

        return functionMap;
    }
    public String getIdentifier() {
        return CTX_ID+"/"+CTX_NAME;
    }
}
