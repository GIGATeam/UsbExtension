package com.gigateam.extensions.usb {
	import com.gigateam.extensions.IExtensionLibrary;
	import flash.system.Capabilities;
	import flash.events.EventDispatcher;
	import com.gigateam.extensions.usb.endpoint.UsbDevice;
	import com.gigateam.extensions.usb.endpoint.UsbAccessory;
	import com.gigateam.extensions.usb.endpoint.UsbEndpoint;
	import com.gigateam.extensions.usb.events.UsbEvent;
	
	public class UsbExtension extends EventDispatcher{
		private static const extensionID:String = "com.gigateam.extensions.usb.UsbExtension";
		private static var _instance:UsbExtension;
		private static var _usbInterface:UsbInterface;
		private var actionUsbCode:String;
		public function UsbExtension() {
			// constructor code
			if(_instance!=null){
				throw new Error("Singleton.")
			}
			_instance = this;
		}
		public static function init(code:String):UsbExtension{
			usbExtension.setCode(code);
			return usbExtension;
		}
		private function setCode(code:String):void{
			actionUsbCode = code;
			usbInterface.setActionCode(code);
			usbInterface.startListen();
			usbInterface.addEventListener(UsbEvent.DATA, onEvent);
			usbInterface.addEventListener(UsbEvent.USB_DETACHED, onEvent);
			usbInterface.addEventListener(UsbEvent.USB_ATTACHED, onEvent);
			usbInterface.addEventListener(UsbEvent.USB_STATE, onEvent);
			usbInterface.addEventListener(UsbEvent.GRANT_PERMISSION, onEvent);
		}
		public static function get usbExtension():UsbExtension{
			if(_instance==null){
				new UsbExtension();
			}
			return _instance;
		}
		public function get isSupported():Boolean{
			return (Capabilities.version.indexOf("AND")>=0);
		}
		public function dispose():void{
			_instance = null;
			usbInterface.removeEventListener(UsbEvent.DATA, onEvent);
			usbInterface.removeEventListener(UsbEvent.USB_DETACHED, onEvent);
			usbInterface.removeEventListener(UsbEvent.USB_ATTACHED, onEvent);
			usbInterface.removeEventListener(UsbEvent.USB_STATE, onEvent);
			usbInterface.removeEventListener(UsbEvent.GRANT_PERMISSION, onEvent);
			usbInterface.dispose();
		}
		private function onEvent(e:UsbEvent):void{
			var usbEvent:UsbEvent = new UsbEvent(e.type, e.rawData, e.data);
			if(e.data!=null || e.data.hasOwnProperty(UsbEvent.END_POINT)){
				if(UsbEndpoint.isDevice(e.data[UsbEvent.END_POINT])){
					usbEvent.endpoint = getDevice(e.data[UsbEvent.END_POINT], true);
				}else{
					usbEvent.endpoint = getAccessory(e.data[UsbEvent.END_POINT], true);
				}
			}
			dispatchEvent(usbEvent);
		}
		private function get usbInterface():UsbInterface{
			if(_usbInterface==null){
				_usbInterface=new UsbInterface(extensionID);
			}
			return _usbInterface;
		}
		public function getDevice(data:Object, flush:Boolean=false):UsbDevice{
			if(data.hasOwnProperty(UsbDevice.PRODUCT_ID)){
				return getDeviceByIds(data[UsbDevice.PRODUCT_ID], data[UsbDevice.VENDOR_ID], flush);
			}
			return null;
		}
		public function getAccessory(data:Object, flush:Boolean=false):UsbAccessory{
			if(data.hasOwnProperty(UsbEndpoint.HASH_CODE)){
				return getAccessoryByHashCode(data[UsbEndpoint.HASH_CODE], flush);
			}
			return null;
		}
		public function getDeviceByIds(productId:String, vendorId:String, flush:Boolean=false):UsbDevice{
			var list:Vector.<UsbDevice> = getDevices(flush);
			for(var i:int=0;i<list.length;i++){
				if(list[i].productId==productId && list[i].vendorId==vendorId){
					return list[i];
				}
			}
			return null;
		}
		public function getAccessoryByHashCode(hashCode:int, flush:Boolean=false):UsbAccessory{
			var list:Vector.<UsbAccessory> = getAccessories(flush);
			for(var i:int=0;i<list.length;i++){
				if(list[i].hashCode==hashCode){
					return list[i];
				}
			}
			return null;
		}
		public function getDevices(flush:Boolean=true):Vector.<UsbDevice>{
			var vec:Vector.<UsbDevice> = new Vector.<UsbDevice>();
			if(flush){
				usbInterface.clear();
			}
			var str:String = usbInterface.getDevices();
			var list:Array = JSON.parse(usbInterface.getDevices()) as Array;
			for(var i:int=0;i<list.length;i++){
				var device:UsbDevice = new UsbDevice();
				device.init(list[i], i);
				vec.push(device);
			}
			return vec;
		}
		public function getAccessories(flush:Boolean=true):Vector.<UsbAccessory>{
			var vec:Vector.<UsbAccessory> = new Vector.<UsbAccessory>();
			if(flush){
				usbInterface.clear();
			}
			var str:String = usbInterface.getAccessories();
			var list:Array = JSON.parse(str) as Array;
			for(var i:int=0;i<list.length;i++){
				var accessory:UsbAccessory = new UsbAccessory();
				accessory.init(list[i], i);
				vec.push(accessory);
			}
			return vec;
		}
		public function hasPermission(endpoint:UsbEndpoint):Boolean{
			return usbInterface.hasPermission(endpoint);
		}
		public function requestPermission(endpoint:UsbEndpoint):void{
			usbInterface.reqeustPermission(endpoint);
		}
		public function connect(device:UsbDevice):Boolean{
			return usbInterface.connect(device.index, device.type);
		}
		public function write(data:String):void{
			usbInterface.write(data);
		}
	}
}