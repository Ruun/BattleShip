:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Fall 2023
:: ---------------------------------------------------------------------
:: Begin of Script (Labs - W24)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

:: Some of the below variables will need to be changed.
:: Remember to always use RELATIVE paths.

:: If your code needs no external libraries, remove all references to LIBDIR
:: in this script.

SET LIBDIR=/Users/thelion/Downloads/javafx-sdk-22.0.1/lib
SET SRCDIR=src/main/java
SET BINDIR=target/classes/com/isruan/battleshipz
SET BINERR=labs-javac.err
SET JARNAME=JAPLabsSwing.jar
SET JAROUT=labs-jar.out
SET JARERR=labs-jar.err
SET DOCDIR=doc
SET DOCPACK=com.isruan.battleshipz
SET DOCERR=labs-javadoc.err
SET MAINCLASSSRC=src/main/java/com/isruan/battleshipz/GameApplication.java
SET MAINCLASSBIN=main.java.com.isruan.battleshipz.GameApplication


@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @         SPRING  -  2 0 2 4         @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%;%LIBDIR%/*" --module-path "%LIBDIR%" --add-modules javafx.controls,javafx.fxml %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%

ECHO "2. Creating Jar ..................."
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;../%LIBDIR%/*" --module-path "%LIBDIR%" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% 2> %DOCERR%

cd bin
ECHO "4. Running Jar ...................."
start java --module-path "../%LIBDIR%" -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Labs - W24)
:: ---------------------------------------------------------------------
