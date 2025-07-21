@echo off
setlocal

:: プロジェクト名が引数として指定されているかを確認
if "%~1"=="" (
    echo.
    echo エラー: プロジェクト名が指定されていません。
    echo.
    echo 使用例: replace_pjname.bat ^<実際のプロジェクト名^>
    echo 例: replace_pjname.bat my-new-project
    echo.
    pause
    exit /b 1
)

:: 引数からプロジェクト名を取得
set "PROJECT_NAME=%~1"
set "DOCKER_COMPOSE_FILE=docker-compose.yml"
set "POM_FILE=pom.xml"
set "LAUNCH_FILE=.vscode\launch.json"

echo.
echo === %DOCKER_COMPOSE_FILE%, %POM_FILE%, %LAUNCH_FILE% のプレースホルダーを置換します ===
echo.
echo 対象ファイル: %DOCKER_COMPOSE_FILE%, %POM_FILE%, %LAUNCH_FILE%
echo 置換するプロジェクト名: %PROJECT_NAME%
echo.

:: ファイル存在チェック
if not exist "%DOCKER_COMPOSE_FILE%" (
    echo エラー: %DOCKER_COMPOSE_FILE% が見つかりません。
    echo バッチファイルは %CD% 現在のディレクトリ で実行されています。
    pause
    exit /b 1
)
if not exist "%POM_FILE%" (
    echo エラー: %POM_FILE% が見つかりません。
    echo バッチファイルは %CD% 現在のディレクトリ で実行されています。
    pause
    exit /b 1
)
if not exist "%LAUNCH_FILE%" (
    echo エラー: %LAUNCH_FILE% が見つかりません。
    echo バッチファイルは %CD% 現在のディレクトリ で実行されています。
    pause
    exit /b 1
)


echo --- 置換処理を開始します ---
echo.

:: PowerShellを使って各ファイルを一括置換
powershell -Command "(Get-Content -Path '%DOCKER_COMPOSE_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%DOCKER_COMPOSE_FILE%'"
powershell -Command "(Get-Content -Path '%POM_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%POM_FILE%'"
powershell -Command "(Get-Content -Path '%LAUNCH_FILE%') -replace 'REPLACE_WITH_YOUR_PROJECT_NAME', '%PROJECT_NAME%' | Set-Content -Path '%LAUNCH_FILE%'"

echo.
echo すべての置換が正常に完了しました。
echo.
pause
endlocal
exit /b 0