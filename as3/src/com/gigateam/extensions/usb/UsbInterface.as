package com.gigateam.extensions.usb {
	import com.gigateam.extensions.ContextInterface;
	import com.gigateam.extensions.usb.endpoint.UsbEndpoint;
	import flash.events.StatusEvent;
	import com.gigateam.extensions.usb.events.UsbEvent;
	import flash.external.ExtensionContext;
	
	public class UsbInterface extends ContextInterface{
		public static const KEY:String = "USB";
		public static var staticContext:ExtensionContext;
		public function UsbInterface(extId:String) {
			// constructor code
			super(extId, KEY);
			staticContext = context;
		}
		override public function dispose():void{
			super.dispose();
		}
		
		public function getDevices():String{
			return callFuncString("getDevicesFunctionKey");
		}
		public function getAccessories():String{
			return callFuncString("getAccessoriesFunctionKey");
		}
		public function setActionCode(code:String):void{
			callFunc("setActionUsbFunctionKey", [code]);
		}
		public function hasPermission(endpoint:UsbEndpoint):Boolean{
			return callFuncBool("hasPermissionFunctionKey", [endpoint.index, endpoint.type]);
		}
		public function reqeustPermission(endpoint:UsbEndpoint):void{
			callFunc("requestPermissionFunctionKey", [endpoint.index, endpoint.type]);
		}
		public function startListen():void{
			context.addEventListener(StatusEvent.STATUS, onStatus);
			callFunc("startListenFunctionKey");
		}
		public function stopListen():void{
			context.removeEventListener(StatusEvent.STATUS, onStatus);
			callFunc("stopListenFunctionKey");
		}
		private function onStatus(e:StatusEvent):void{
			trace("status");
			var usbEvent:UsbEvent;
			var obj:Object = null;
			switch(e.code){
				case UsbEvent.DATA:
					break;
				default:
					obj = JSON.parse(e.level);
				break;
			}
			usbEvent = new UsbEvent(e.code, e.level, obj);
			dispatchEvent(usbEvent);
		}
		public function getDebugMessage():String{
			return callFuncString("getDebugStatusFunctionKey");
		}
		public function clear():void{
			callFunc("clearFunctionKey");
		}
		public function connect(index:int, type:int):Boolean{
			return callFuncBool("connectFunctionKey", [index, type]);
		}
		public function write(data:String):void{
			callFunc("writeFunctionKey", [data]);
		}
	}
}