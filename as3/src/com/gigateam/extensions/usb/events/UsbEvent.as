package com.gigateam.extensions.usb.events {
	import flash.events.Event;
	import com.gigateam.extensions.usb.endpoint.UsbEndpoint;
	
	public class UsbEvent extends Event {
		public static const DATA:String = "serialData";
		public static const END_POINT:String = "endpoint";
		public static const USB_STATE:String = "usbState";
		public static const USB_ATTACHED:String = "usbAttached";
		public static const USB_DETACHED:String = "usbDetached";
		public static const GRANT_PERMISSION:String = "grantPermission";
		public var endpoint:UsbEndpoint;
		public var data:Object;
		public var rawData:String;
		public function UsbEvent(type:String, dataString:String, parsed:Object=null) {
			// constructor code
			super(type);
			rawData = dataString;
			data = parsed;
		}
	}
	
}
