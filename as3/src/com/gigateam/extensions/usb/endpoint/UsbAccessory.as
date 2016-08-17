package com.gigateam.extensions.usb.endpoint {
	
	public class UsbAccessory extends UsbEndpoint {
		public static const TYPE:int = 1;
		public static const URI:String = "uri";
		public static const SERIAL:String = "serial";
		public var uri:String;
		public var serial:String;
		public function UsbAccessory() {
			// constructor code
			type = TYPE;
		}
		override public function init(data:Object, listIndex:int):void{
			super.init(data, listIndex);
			serial = data[SERIAL];
			uri = data[URI];
		}
	}
	
}
