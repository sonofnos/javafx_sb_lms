@echo off
REM API Test Script for Library Management System (Windows)
REM Tests all major endpoints using curl

setlocal enabledelayedexpansion

set BASE_URL=http://localhost:8080
set TEST_COUNT=0
set PASS_COUNT=0

echo Library Management System API Tests
echo.


REM Test 2: Get All Books
echo Test 2: Get All Books
curl -s %BASE_URL%/api/books > temp.json
findstr /C:"\"id\"" temp.json > nul
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Retrieved books successfully
    set /a PASS_COUNT+=1
) else (
    echo [FAIL] Failed to retrieve books
)
del temp.json
set /a TEST_COUNT+=1
echo.

REM Test 3: Get Book by ID
echo Test 3: Get Book by ID ^(ID=1^)
curl -s %BASE_URL%/api/books/1 > temp.json
findstr /C:"\"id\"" temp.json > nul
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Retrieved book by ID
    set /a PASS_COUNT+=1
) else (
    echo [FAIL] Failed to retrieve book by ID
)
del temp.json
set /a TEST_COUNT+=1
echo.

REM Test 4: Search Books
echo Test 4: Search Books ^(query='Java'^)
curl -s "%BASE_URL%/api/books?search=Java" > temp.json
findstr /C:"\"id\"" temp.json > nul
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Search returned results
    set /a PASS_COUNT+=1
) else (
    echo [FAIL] Search failed
)
del temp.json
set /a TEST_COUNT+=1
echo.

REM Test 5: Create New Book
echo Test 5: Create New Book
curl -s -X POST %BASE_URL%/api/books ^
    -H "Content-Type: application/json" ^
    -d "{\"title\":\"Test Book from Script\",\"author\":\"Test Author\",\"isbn\":\"978-1234567890\",\"publishedDate\":\"2025-01-01\"}" > temp.json
findstr /C:"\"id\"" temp.json > nul
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Created book successfully
    set /a PASS_COUNT+=1
    
    REM Extract ID for update/delete tests
    for /f "tokens=2 delims=:," %%a in ('findstr "\"id\"" temp.json') do set NEW_ID=%%a
    set NEW_ID=!NEW_ID: =!
    
    REM Test 6: Update Book
    echo.
    echo Test 6: Update Book ^(ID=!NEW_ID!^)
    curl -s -X PUT %BASE_URL%/api/books/!NEW_ID! ^
        -H "Content-Type: application/json" ^
        -d "{\"title\":\"Test Book Updated\",\"author\":\"Test Author\",\"isbn\":\"978-1234567890\",\"publishedDate\":\"2025-01-01\"}" > temp2.json
    findstr /C:"Updated" temp2.json > nul
    if !ERRORLEVEL! EQU 0 (
        echo [PASS] Updated book successfully
        set /a PASS_COUNT+=1
    ) else (
        echo [FAIL] Failed to update book
    )
    del temp2.json
    set /a TEST_COUNT+=1
    
    REM Test 7: Delete Book
    echo.
    echo Test 7: Delete Book ^(ID=!NEW_ID!^)
    curl -s -o nul -w "%%{http_code}" -X DELETE %BASE_URL%/api/books/!NEW_ID! > temp.txt
    set /p DEL_STATUS=<temp.txt
    del temp.txt
    if "!DEL_STATUS!"=="204" (
        echo [PASS] Deleted book successfully ^(HTTP !DEL_STATUS!^)
        set /a PASS_COUNT+=1
    ) else if "!DEL_STATUS!"=="200" (
        echo [PASS] Deleted book successfully ^(HTTP !DEL_STATUS!^)
        set /a PASS_COUNT+=1
    ) else (
        echo [FAIL] Failed to delete book ^(HTTP !DEL_STATUS!^)
    )
    set /a TEST_COUNT+=1
) else (
    echo [FAIL] Failed to create book
)
del temp.json
set /a TEST_COUNT+=1
echo.

echo Tests Completed: !PASS_COUNT! / !TEST_COUNT! passed

endlocal
