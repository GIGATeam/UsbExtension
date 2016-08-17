package com.gigateam.extensions.usb;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.gigateam.extensions.usb.contexts.UsbContext;

/**
 * Created by TIGER on 10/8/2016.
 */
public class UsbExtension implements FREExtension {
    private FREContext context;
    public FREContext createContext(String contextType){
        context = new UsbContext(contextType);
        return context;
    }
    public void initialize(){

    }
    public void dispose(){
        context = null;
    }
}
