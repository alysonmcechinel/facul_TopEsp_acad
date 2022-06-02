@echo off
set path_old=%path%
set path=C:\wamp64\bin\mysql\mysql5.7.26\bin
startmysqldump.exe  –uroot_backup academy > "D:\faculdade\fase-5\topicos-1\sistema-academia\resource\backup\dump.sql"
set path=%path_old%
exit