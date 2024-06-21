adb shell -t 'exec 3>&0 && content read --uri content://adbtricks | ENV=/dev/fd/4 sh -i 4<&0 0<&3'
