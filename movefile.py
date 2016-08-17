'''

This script is part of fullbuild batch,
usually no need to run this script seperately.

'''
from shutil import copyfile
import os.path

androidLib = "C:/Users/TIGER/AndroidStudioProjects/UsbExtension/app/release/androidLib.jar"

def cpfile(src, dst):
    if os.path.isfile(dst):
        os.remove(dst)
    copyfile(src, dst)

cpfile(androidLib, "build/platform/Android-ARM/androidLib.jar")
cpfile(androidLib, "build/platform/Android-x86/androidLib.jar")

cpfile("as3/library.swc", "build/library.swc")
cpfile("as3/library.swf", "build/platform/default/library.swf")
cpfile("as3/library.swf", "build/platform/Android-ARM/library.swf")
cpfile("as3/library.swf", "build/platform/Android-x86/library.swf")
cpfile("as3/library.swf", "build/platform/iPhone-ARM/library.swf")
