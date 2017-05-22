#!/bin/bash
###########################################################################
#
# Copyright (c) 2016 Yawg project contributors.
#
#
# Utility functions. This script is not meant to be executed by
# itself. Instead it is tipically sourced from within other scripts
# using the Bash "source" operator.
#
#########################################################################





###########################################################################
#
# Displays a message to stderr and exits the process.
#
# Arguments:
#
# 1. The message to be displayed.
#
###########################################################################

function yawgError () {

    echo "ERROR" $1 >&2
    exit 1
}





###########################################################################
#
# Displays a message to stdout prefixed with the current time.
#
# Arguments:
#
# 1. The message to be displayed.
#
###########################################################################

function yawgLog () {

    echo $(date "+%Y-%m-%d %H:%M:%S.%3N") "$@"
}





###########################################################################
#
# Checks if a set of required tools is available. If any is missing
# then an error message is output and the current process is
# terminated.
#
# Arguments:
#
# 1. Tools to look for in the PATH or by path.
#
###########################################################################

function yawgCheckForTools () {

    local toolList="$@"

    for tool in ${toolList} ; do
        if type $tool > /dev/null 2>&1 ; then
            : # We found the tool. All is ok
        else
            yawgError "Missing \"${tool}\" tool. Please install this tool."
        fi
    done
}





###########################################################################
#
# In a Cygwin environment, converts the given path to a Windows
# path. This will be needed when passing paths to Java, because Java
# is a native Windows app (so we assume).
#
###########################################################################

function yawgNormalizePath () {

    case "$OSTYPE" in
    cygwin )
        cygpath --mixed --absolute "$1"
        ;;
    * )
        echo $1
    esac
}

