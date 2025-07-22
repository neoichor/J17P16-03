@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo [ERROR] New project name not specified.
    pause
    exit /b 1
)

set "PROJECT_NAME=%~1"
set "DOCKER_COMPOSE_FILE=docker-compose.yml"
set "POM_FILE=pom.xml"
set "LAUNCH_FILE=.vscode\launch.json"

echo === Replacing placeholders...
echo   New Project Name: %PROJECT_NAME%

powershell -Command "(Get-Content -Path '%DOCKER_COMPOSE_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%DOCKER_COMPOSE_FILE%'"
powershell -Command "(Get-Content -Path '%POM_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%POM_FILE%'"
powershell -Command "(Get-Content -Path '%LAUNCH_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%LAUNCH_FILE%'"

echo All replacements completed successfully.
pause
endlocal
exit /b 0
