# ■■■ 1. 引数のチェック ■■■
param (
    [string]$NewProjectName
)

if ([string]::IsNullOrEmpty($NewProjectName)) {
    Write-Host ""
    Write-Host "[エラー] プロジェクト名が指定されていません。" -ForegroundColor Red
    Write-Host ""
    Write-Host "使い方: .\replace_pjname.ps1 <新しいプロジェクト名>"
    Write-Host "例:     .\replace_pjname.ps1 my-new-project"
    Write-Host ""
    pause
    exit 1
}

# ■■■ 2. 変数の設定と引数の表示 ■■■
$placeholder = "REPLACE_WITH_YOUR_PROJECT_NAME"
$fileList = @("docker-compose.yml", "pom.xml", ".vscode\launch.json")
$successFiles = @()
$failedFiles = @()

Write-Host ""
Write-Host "================================================================"
Write-Host " プロジェクト名の置換を開始します"
Write-Host "================================================================"
Write-Host "  置換対象の文字列: $placeholder"
Write-Host "  新しいプロジェクト名: $NewProjectName"
Write-Host "----------------------------------------------------------------"
Write-Host ""

# ■■■ 3. 各ファイルの置換処理 ■■■
foreach ($file in $fileList) {
    Write-Host "Processing [$file]..."

    if (-not (Test-Path $file)) {
        Write-Host "  -> [失敗] ファイルが見つかりません。" -ForegroundColor Yellow
        $failedFiles += "$file (Not Found)"
    } else {
        try {
            # ファイルの内容を読み込み、置換を実行して、同じファイルに書き戻す
            (Get-Content $file -Raw) -replace $placeholder, $NewProjectName | Set-Content $file
            Write-Host "  -> [成功] ファイルを置換しました。" -ForegroundColor Green
            $successFiles += $file
        } catch {
            Write-Host "  -> [失敗] ファイルの書き換え中にエラーが発生しました。" -ForegroundColor Red
            $failedFiles += "$file (Error)"
        }
    }
}

# ■■■ 4. 最終結果の表示 ■■■
Write-Host ""
Write-Host "================================================================"
Write-Host " 置換処理 結果レポート"
Write-Host "================================================================"

if ($successFiles.Count -gt 0) {
    Write-Host "[成功したファイル]" -ForegroundColor Green
    $successFiles | ForEach-Object { Write-Host "  $_" }
} else {
    Write-Host "[成功したファイル]"
    Write-Host "  (なし)"
}

if ($failedFiles.Count -gt 0) {
    Write-Host ""
    Write-Host "[失敗したファイル]" -ForegroundColor Red
    $failedFiles | ForEach-Object { Write-Host "  $_" }
} else {
    Write-Host ""
    Write-Host "[失敗したファイル]"
    Write-Host "  (なし)"
}
Write-Host "================================================================"
Write-Host ""

pause