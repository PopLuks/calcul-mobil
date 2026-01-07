@echo off
echo ========================================
echo  Copiere si Redenumire Imagini
echo ========================================
echo.

REM Setare paths
set SOURCE=C:\Users\poplu\AndroidStudioProjects\lab7\app\Img
set DEST=C:\Users\poplu\AndroidStudioProjects\lab7\app\src\main\res\drawable

echo Copiere imagini din %SOURCE% in %DEST%...
echo.

REM Copiere imagini PNG
if exist "%SOURCE%\*.png" (
    echo Copiere imagini PNG...
    copy "%SOURCE%\*.png" "%DEST%\"
)

REM Copiere imagini JPG
if exist "%SOURCE%\*.jpg" (
    echo Copiere imagini JPG...
    copy "%SOURCE%\*.jpg" "%DEST%\"
)

REM Copiere imagini JPEG
if exist "%SOURCE%\*.jpeg" (
    echo Copiere imagini JPEG...
    copy "%SOURCE%\*.jpeg" "%DEST%\"
)

echo.
echo Redenumire imagini...
cd /d "%DEST%"

REM Redenumire imagini de la 0 la 8
if exist "0.png" ren "0.png" "img_0.png"
if exist "1.png" ren "1.png" "img_1.png"
if exist "2.png" ren "2.png" "img_2.png"
if exist "3.png" ren "3.png" "img_3.png"
if exist "4.png" ren "4.png" "img_4.png"
if exist "5.png" ren "5.png" "img_5.png"
if exist "6.png" ren "6.png" "img_6.png"
if exist "7.png" ren "7.png" "img_7.png"
if exist "8.png" ren "8.png" "img_8.png"

REM Redenumire JPG
if exist "0.jpg" ren "0.jpg" "img_0.jpg"
if exist "1.jpg" ren "1.jpg" "img_1.jpg"
if exist "2.jpg" ren "2.jpg" "img_2.jpg"
if exist "3.jpg" ren "3.jpg" "img_3.jpg"
if exist "4.jpg" ren "4.jpg" "img_4.jpg"
if exist "5.jpg" ren "5.jpg" "img_5.jpg"
if exist "6.jpg" ren "6.jpg" "img_6.jpg"
if exist "7.jpg" ren "7.jpg" "img_7.jpg"
if exist "8.jpg" ren "8.jpg" "img_8.jpg"

echo.
echo ========================================
echo  GATA! Imaginile au fost copiate si redenumite.
echo ========================================
echo.
echo Verificati ca exista urmatoarele fisiere in drawable:
echo - img_0.png (sau .jpg)
echo - img_1.png
echo - img_2.png
echo - img_3.png
echo - img_4.png
echo - img_5.png
echo - img_6.png
echo - img_7.png
echo - img_8.png
echo.
echo Acum puteti rula aplicatia in Android Studio!
echo.
pause

