#!/usr/bin/env bash

case "$(uname -s)" in

   CYGWIN*|MINGW*|MSYS*)
     csc src/*.cs -out:server.exe;;
     

   *)
     mcs src/*.cs -pkg:wcf -out:server.exe;;
     
esac