@echo off
setLocal EnableDelayedExpansion
set MyCLASSPATH=%CLASSPATH%
for /R lib %%a in (*.jar) do (
   set MyCLASSPATH=!MyCLASSPATH!;%%a
)
for /R ../lib %%a in (*.jar) do (
   set MyCLASSPATH=!MyCLASSPATH!;%%a
)
java -cp .;../owllink-bin.jar;../lib/owlapi-bin.jar;%MyCLASSPATH% org.semanticweb.owlapi.owllink.server.serverfactory.HermiTServerFactory -port 8080
set MyClassPath=