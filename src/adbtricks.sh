#!/bin/sh

adbtricks() { adb shell '$(content read --uri content://adbtricks)' $@; }

if [ $# -ne 0 ]; then adbtricks $@; fi

