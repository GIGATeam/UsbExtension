package  {
	import flash.display.Sprite;
	import com.gigateam.extensions.usb.UsbExtension;
	import com.gigateam.extensions.usb.events.UsbEvent;
	import flash.utils.Timer;
	import flash.events.TimerEvent;
	import com.gigateam.extensions.usb.endpoint.UsbDevice;
	import com.gigateam.extensions.usb.Baud;
	
	public class UsbExample extends Sprite{
		private var _devices:Vector.<UsbDevice>;
		public function UsbExample() {
			// constructor code
			var ext:UsbExtension = UsbExtension.init("com.gigateam.extensions.usb.ACTION_USB_GRANT");
			if(ext.isSupported){
				ext.addEventListener(UsbEvent.USB_DETACHED, onDetached);
				ext.addEventListener(UsbEvent.USB_ATTACHED, onAttached);
			
				// Bad idea to detect attached event by polling, but USB_DEVICE_ATTACHED suppose never fire.
				var poller:Timer = new Timer(1000, 0);
				poller.addEventListener(TimerEvent.TIMER, onPoll);
				poller.start();
			}
		}
		private function onPoll(e:TimerEvent=null):void{
			var ext:UsbExtension = UsbExtension.usbExtension;
			var devices:Vector.<UsbDevice> = ext.getDevices();
			if(devices.length>_devices.length){
				var difference:Vector.<UsbDevice> = UsbDevice.sieve(_devices, devices);
				//Example only show how to handle when exact 1 device attached
				if(difference.length==1){
					//check if has permission granted with specific device
					if(ext.hasPermission(difference[0])){
						establishConnection(difference[0]);
					}else{
						ext.addEventListener(UsbEvent.GRANT_PERMISSION, onGranted);
						ext.requestPermission(difference[0]);
					}
				}
			}
			_devices = devices;
		}
		private function establishConnection(device:UsbDevice):void{
			var ext:UsbExtension = UsbExtension.usbExtension;
			if(ext.connect(device, Baud.BAUD_9600)){
				ext.addEventListener(UsbEvent.DATA, onData);
				ext.write("Hello");
			}
		}
		private function onData(e:UsbEvent):void{
			if(e.rawData != ""){
				trace("Received data from device, ",e.rawData);
			}
		}
		private function onGranted(e:UsbEvent):void{
			var ext:UsbExtension = UsbExtension.usbExtension;
			ext.removeEventListener(UsbEvent.GRANT_PERMISSION, onGranted);
			
			establishConnection(e.endpoint as UsbDevice);
		}
		private function onDetached(e:UsbEvent):void{
			onPoll();
		}
		private function onAttached(e:UsbEvent):void{
			//USB_DEVICE_ATTACHED suppose never fire is like a bug from Android
		}
	}
	
}
