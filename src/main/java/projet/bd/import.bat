@echo off
set DB_NAME=bd_aviation

for %%F in (json\*.json) do (
    set "file=%%F"
    setlocal enabledelayedexpansion
    for %%I in (!file!) do (
        set "name=%%~nI"
        echo Importing !name!
        mongoimport --db %DB_NAME% --collection !name! --file "%%F" --jsonArray --drop
    )
    endlocal
)