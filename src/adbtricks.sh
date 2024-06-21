adb shell -t 'export FPATH=$(mktemp -d) && content read --uri content://adbtricks > $FPATH/adbtricks && sh -i'
