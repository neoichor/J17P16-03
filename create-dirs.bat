@echo off
REM =================================================================
REM Maven Standard Directory & .gitkeep File Creator
REM =================================================================
REM
REM このバッチファイルは、現在のフォルダにMavenプロジェクトの
REM 標準的なディレクトリ構造を作成します(.gitkeepはダミーファイル)。
REM
REM 実行方法:
REM 1. このファイルをプロジェクトのルートフォルダに置きます。
REM    (例: C:\develop\java-postgres-template)
REM 2. ダブルクリックするか、コマンドプロンプトで実行します。
REM
echo.
echo Mavenの標準ディレクトリ構造と.gitkeepファイルを作成します...
echo.
mkdir .devcontainer
mkdir .vscode
mkdir postgres-config
mkdir sql
mkdir src\main\java
echo. > src\main\java\.gitkeep
mkdir src\main\resources
echo. > src\main\resources\.gitkeep
mkdir src\test\java
echo. > src\test\java\.gitkeep
mkdir src\test\resources
echo. > src\test\resources\.gitkeep
echo.
echo ディレクトリ構造が作成されました。
echo.
pause
REM treeコマンドで結果を視覚的に表示
tree /F .

echo.
echo 正常に完了しました。

REM 一時停止
pause