@echo off
set conffile="config\TestConfiguration.properties"
set conffileorg="config\TestConfiguration-ORG.properties"
set envpropfile="config\config.properties"
echo "Copy"
copy build_param.xml build.xml
set SCREENSHOT=%1
set APPNAME=%2
set BROWSER=%3
set MODULE=%4
set ENV=%5
set RUNTESTENV=%6

MOVE %conffile% %conffileorg%

echo CaptureScreenShot=%SCREENSHOT%> %conffile%
echo RunTestApp=%APPNAME%>> %conffile%
echo TestBrowser=%BROWSER%>> %conffile%
echo RunTest=%MODULE%>> %conffile%
echo Env=%ENV%>> %conffile%
echo RunTestEnv=%RUNTESTENV%>> %conffile%