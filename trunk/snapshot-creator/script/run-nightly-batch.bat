@echo off

setlocal
rem read directory paths from properties file
FOR /F "tokens=*" %%i in ('type snapshot-creator.properties') do SET %%i

rem create the snapshot for each ndpi in the directory
FOR %%i IN (%ndpi.new.directory%\*.ndpi) DO java %java.opts% -jar snapshot-creator.jar "%%i"

rem move new snapshots to the 'chosen snapshots' directory
move %snapshots.holding.directory%\*.jpg %snapshots.publishing.directory%\.

rem for each snapshot to be added, update the Caisis DB
java %java.opts% -jar caisis-updater.jar

rem run deep zoom converter for all snapshots that were succesfully added to the db
IF EXIST %deepzoom.work.directory% GOTO END2
MD %deepzoom.work.directory%
:END2
deepzoom\Dzconvert.exe %snapshots.publishing.directory%\*.jpg %deepzoom.work.directory% /tq:%deepzoom.jpeg.quality%

rem move the used snapshots to the 'used snapshots' directory
move %snapshots.publishing.directory%\*.jpg %snapshots.used.directory%\.

rem move deepzoom files to the public website
XCOPY "%deepzoom.work.directory%\*" "%website.snapshots.publishing.directory%\" /E /C /I /K /Y

rem delete the deepzoom work directory 
RD "%deepzoom.work.directory%" /Q /S

endlocal