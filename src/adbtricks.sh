#!/bin/sh

adbtricks() {
	adb shell '$(content read --uri content://adbtricks)' $@
}

adbtricks $@

