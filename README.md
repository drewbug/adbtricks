![](hello-dalle.png)

First, install `adbtricks` from the Play Store or manually via:

```
$ adb install adbtricks.apk
```

then load it into any `adb shell` session with:

```
$ alias adbtricks="$(content read --uri content://adbtricks)"
```

or just use `adbtricks.sh` directly from your workstation:

```
$ source adbtricks.sh
```

```
===========================
welcome to adbtricks v1.0.0
===========================

Usage:

 adbtricks dump-wifi-keys
 (outputs all wifi passwords from device memory)

 adbtricks list-hidden-cameras
 (read https://source.android.com/docs/core/camera/system-cameras for info)

 adbtricks start-wifi-tethering [device-ip] [client-ip]
 (turns on wifi tethering with optional IPv4 configuration)

 adbtricks start-usb-tethering [device-ip] [client-ip]
 (turns on usb tethering with optional IPv4 configuration)

 adbtricks start-bluetooth-tethering [device-ip] [client-ip]
 (turns on bluetooth tethering with optional IPv4 configuration)

 adbtricks set-ringer-normal
 (toggles "normal" ringer mode on)

 adbtricks set-ringer-vibrate
 (toggles "vibrate" ringer mode on)

 adbtricks set-ringer-silent
 (toggles "silent" ringer mode on)

 adbtricks dump-debugging-info
 (outputs adb daemon information)
```

