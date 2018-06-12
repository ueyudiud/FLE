@echo off
set JAVA_HOME="C:\Program Files\Java\jre1.8.0_171"
set GRADLE_HOME=D:\gradle
set GRADLE_USER_HOME=%GRADLE_HOME%\.gradle
gradlew -i setupDecompWorkspace eclipse
pause