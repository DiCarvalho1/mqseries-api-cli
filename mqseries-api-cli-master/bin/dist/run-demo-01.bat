@ECHO OFF
REM ***************************************************************************
REM filename   : run-demo-01
REM description: Run Demonstration of PUT
REM souce-code : https://github.com/josemarsilva/mqseries-api-cli 
REM obs        :
REM ***************************************************************************

SET ACTION=put
SET HOST=127.0.0.1
SET PORT=1414
SET CHANNEL=DEV.APP.SVRCONN
SET QMGR=QM1
SET APP_PASSWORD=passw0rd
SET QUEUE_NAME=DEV.QUEUE.1
SET MESSAGE=MSG-RUN-DEMO-01

ECHO.
ECHO java -jar mqseries-api-cli.jar -A %ACTION% -H %HOST% -P %PORT% -C %CHANNEL% -Q %QMGR% -p %APP_PASSWORD% -q %QUEUE_NAME% -m %MESSAGE%
ECHO.

java -jar mqseries-api-cli.jar -A %ACTION% -H %HOST% -P %PORT% -C %CHANNEL% -Q %QMGR% -p %APP_PASSWORD% -q %QUEUE_NAME% -m %MESSAGE%