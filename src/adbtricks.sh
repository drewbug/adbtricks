#!/bin/sh

adbtricks() {
	adb shell -t '$(content read --uri content://adbtricks)' $@
}

adbtricks $@

