@echo off
setlocal enabledelayedexpansion
set "first=1"
set "rootDir=%cd%"

del "%rootDir%\ALL-ENV.TXT" 2>nul

rem Process current directory files
call :ProcessFiles "%rootDir%"

rem Process .devcontainer directory if exists
if exist "%rootDir%\.devcontainer" (
    call :ProcessDirectory "%rootDir%\.devcontainer"
)

rem Process .gemini directory if exists
if exist "%rootDir%\.gemini" (
    call :ProcessDirectory "%rootDir%\.gemini"
)

rem Process postgres-config directory if exists
if exist "%rootDir%\postgres-config" (
    call :ProcessDirectory "%rootDir%\postgres-config"
)

endlocal
exit /b

:ProcessDirectory
set "currentDir=%~1"
pushd "%currentDir%"

rem Process files in current directory
call :ProcessFiles "%currentDir%"

rem Recurse into subdirectories
for /d %%d in (*) do (
    call :ProcessDirectory "%currentDir%\%%d"
)

popd
exit /b

:ProcessFiles
set "dir=%~1"
pushd "%dir%"

for %%f in (*.json *.yml *.dev *.conf .env .gitignore pom.xml) do (
    if exist "%%f" (
        if !first! == 1 (
            echo 【%dir%\%%f】 >> "%rootDir%\ALL-ENV.TXT"
            type "%%f" >> "%rootDir%\ALL-ENV.TXT"
            set "first=0"
        ) else (
            echo. >> "%rootDir%\ALL-ENV.TXT"
            echo // ==================== >> "%rootDir%\ALL-ENV.TXT"
            echo 【%dir%\%%f】 >> "%rootDir%\ALL-ENV.TXT"
            type "%%f" >> "%rootDir%\ALL-ENV.TXT"
        )
    )
)

popd
exit /b
