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
	}
	
}
