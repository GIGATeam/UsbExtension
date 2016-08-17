package com.gigateam.extensions.usb.endpoint {
	
	public class UsbDevice extends UsbEndpoint {
		public static const TYPE:int = 0;
		public var vendorId:String;
		public var productId:String;
		public static const VENDOR_ID:String = "vendorId";
		public static const PRODUCT_ID:String = "productId";
		public function UsbDevice() {
			// constructor code
			type = TYPE;
		}
		override public function init(data:Object, listIndex:int):void{
			super.init(data, listIndex);
			vendorId = data[VENDOR_ID];
			productId = data[PRODUCT_ID];
		}
		public static function sieve(devicesOld:Vector.<UsbDevice>, devicesNew:Vector.<UsbDevice>):Vector.<UsbDevice>{
			var difference:Vector.<UsbDevice> = new Vector.<UsbDevice>();
			for(var i:int=0;i<devicesNew.length;i++){
				if(getDeviceByIds(devicesOld, devicesNew[i].productId, devicesNew[i].vendorId)==null){
					difference.push(devicesNew[i]);
				}
			}
			return difference;
		}
		private static function getDeviceByIds(devices:Vector.<UsbDevice>, productId:String, vendorId:String):UsbDevice{
			for(var i:int=0;i<devices.length;i++){
				if(devices[i].productId == productId && devices[i].vendorId==vendorId){
					return devices[i];
				}
			}
			return null;
		}
	}
	
}
