@ECHO OFF
REM ***************************************************************************
REM filename   : run-demo-04
REM description: Run Demonstration of GET a message and save to a file
REM souce-code : https://github.com/josemarsilva/mqseries-api-cli 
REM obs        :
REM ***************************************************************************

SET ACTION=get
SET HOST=127.0.0.1
SET PORT=1414
SET CHANNEL=DEV.APP.SVRCONN
SET QMGR=QM1
SET APP_PASSWORD=passw0rd
SET QUEUE_NAME=DEV.QUEUE.1
SET MESSAGE_FILE=message-file-received-example.txt

ECHO.
ECHO java -jar mqseries-api-cli.jar -A %ACTION% -H %HOST% -P %PORT% -C %CHANNEL% -Q %QMGR% -p %APP_PASSWORD% -q %QUEUE_NAME% -f %MESSAGE_FILE% 
ECHO.

java -jar mqseries-api-cli.jar -A %ACTION% -H %HOST% -P %PORT% -C %CHANNEL% -Q %QMGR% -p %APP_PASSWORD% -q %QUEUE_NAME% -f %MESSAGE_FILE%
