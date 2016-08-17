package com.gigateam.extensions {
	import flash.events.EventDispatcher;
	import flash.external.ExtensionContext;
	
	public class ContextInterface extends EventDispatcher{
		public var extensionID:String;
		public var contextType:String;
		protected var _context:ExtensionContext;
		private static var _contextTypes:Vector.<String> = new Vector.<String>();
		private static var _extensionContexts:Vector.<ExtensionContext> = new Vector.<ExtensionContext>();
		public function ContextInterface(extId:String, context:String) {
			// constructor code
			extensionID = extId;
			contextType = context;
		}
		protected function get context():ExtensionContext{
			if(_context==null){
				_context = getContext(extensionID, contextType);
			}
			return _context;
		}
		public function get isSupported():Boolean{
			return true;
		}
		private function contextInit(ctx:ExtensionContext):void{
		}
		protected function get index():int{
			return _contextTypes.indexOf(contextType);
		}
		protected function getContext(extID:String, ctxType:String):ExtensionContext{
			var ind:int = index;
			if(ind>=0){
				return _extensionContexts[ind];
			}
			_contextTypes.push(ctxType);
			var _ctx:ExtensionContext = ExtensionContext.createExtensionContext(extID, ctxType);
			_extensionContexts.push(_ctx);
			return _ctx;
		}
		protected function callFuncBool(functionKey:String, args:Array=null):Boolean{
			var obj:Object = callFunc(functionKey, args);
			if(obj==null){
				return null;
			}
			return obj as Boolean;
		}
		protected function callFuncString(functionKey:String, args:Array=null):String{
			var obj:Object = callFunc(functionKey, args);
			if(obj==null){
				return null;
			}
			return obj.toString();
		}
		protected function callFuncInt(functionKey:String, args:Array=null):int{
			var obj:Object = callFunc(functionKey, args);
			if(obj==null){
				return null;
			}
			return obj as int;
		}
		protected function callFuncNumber(functionKey:String, args:Array=null):Number{
			var obj:Object = callFunc(functionKey, args);
			if(obj==null){
				return null;
			}
			return obj as Number;
		}
		protected function callFunc(functionKey:String, args:Array=null):Object{
			if(args!=null){
				args.unshift(functionKey);
			}else{
				args = [functionKey];
			}
			return context.call.apply(this, args);
		}
		public function dispose():void{
			_extensionContexts[index].dispose();
			_extensionContexts[index] = null;
			if(_context!=null){
				_context.dispose();
			}
			_context = null;
		}
		public static function disposeInterfaces():int{
			var count:int = _extensionContexts.length;
			for(var i:int=0;i<count;i++){
				if(_extensionContexts[i]!=null){
					_extensionContexts[i].dispose();
					_extensionContexts[i]=null;
				}
			}
			_contextTypes = new Vector.<String>();
			_extensionContexts = new Vector.<ExtensionContext>();
			return count;
		}
		/*public function getKey():String{
			return callFuncString("contextKeyFunctionKey");
		}*/
	}
}