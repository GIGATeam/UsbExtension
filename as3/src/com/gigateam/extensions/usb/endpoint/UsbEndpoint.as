package com.gigateam.extensions.usb.endpoint {
	
	public class UsbEndpoint {
		public var index:int;
		public var type:int;
		protected var _data:Object;
		public var hashCode:int;
		public static const HASH_CODE:String="hashCode";
		public function UsbEndpoint() {
			// constructor code
		}
		public function init(data:Object, listIndex:int):void{
			_data = data;
			hashCode = data[HASH_CODE];
			index = listIndex;
		}
		public function get rawData():String{
			return JSON.stringify(_data);
		}
		public static function isDevice(data:Object):Boolean{
			return true;
		}
	}
	
}
