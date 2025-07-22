# Maven build output
target/
dependency-reduced-pom.xml

# Log files
*.log
logs/

# IDE settings
.idea/
*.iml
.settings/
.classpath
.project

# OS generated files
.DS_Store
Thumbs.db

# Local environment variables file
.env

# VSCode local settings
# launch.jsonとsetting.jsonはテンプレートに含めるため、ここでは無視しない
# .vscode 全体を管理対象にするため、無視ルールを削除
!.vscode/
# 拡張子 .bat のファイルを明示的に管理対象にする
!*.bat
