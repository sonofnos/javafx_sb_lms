#!/bin/bash

# Quick API Test Script for Library Management System
# Tests all major endpoints using curl

set -e

BASE_URL="http://localhost:8080"
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}🧪 Library Management System API Tests${NC}"
echo "=========================================="
echo ""


# Test 2: Get All Books
echo -e "${YELLOW}Test 2: Get All Books${NC}"
books=$(curl -s ${BASE_URL}/api/books | python3 -m json.tool)
count=$(echo "$books" | grep -c "\"id\":" || true)
echo -e "${GREEN}✅ Retrieved $count books${NC}"
echo ""

# Test 3: Get Book by ID
echo -e "${YELLOW}Test 3: Get Book by ID (ID=1)${NC}"
book=$(curl -s ${BASE_URL}/api/books/1)
if echo "$book" | grep -q "\"id\""; then
    title=$(echo "$book" | python3 -c "import sys, json; print(json.load(sys.stdin)['title'])" 2>/dev/null || echo "Unknown")
    echo -e "${GREEN}✅ Retrieved book: $title${NC}"
else
    echo -e "${RED}❌ Failed to retrieve book${NC}"
fi
echo ""

# Test 4: Search Books
echo -e "${YELLOW}Test 4: Search Books (query='Java')${NC}"
results=$(curl -s "${BASE_URL}/api/books?search=Java")
search_count=$(echo "$results" | grep -c "\"id\":" || true)
echo -e "${GREEN}✅ Found $search_count matching books${NC}"
echo ""

# Test 5: Create New Book
echo -e "${YELLOW}Test 5: Create New Book${NC}"
new_book='{
    "title": "Test Book from Script",
    "author": "Test Author",
    "isbn": "978-1234567890",
    "publishedDate": "2025-01-01"
}'
response=$(curl -s -X POST ${BASE_URL}/api/books \
    -H "Content-Type: application/json" \
    -d "$new_book")
if echo "$response" | grep -q "\"id\""; then
    new_id=$(echo "$response" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])" 2>/dev/null || echo "Unknown")
    echo -e "${GREEN}✅ Created book with ID: $new_id${NC}"
    
    # Test 6: Update the newly created book
    echo ""
    echo -e "${YELLOW}Test 6: Update Book (ID=$new_id)${NC}"
    updated_book='{
        "title": "Test Book from Script (Updated)",
        "author": "Test Author",
        "isbn": "978-1234567890",
        "publishedDate": "2025-01-01"
    }'
    update_response=$(curl -s -X PUT ${BASE_URL}/api/books/$new_id \
        -H "Content-Type: application/json" \
        -d "$updated_book")
    if echo "$update_response" | grep -q "Updated"; then
        echo -e "${GREEN}✅ Successfully updated book${NC}"
    else
        echo -e "${RED}❌ Failed to update book${NC}"
    fi
    
    # Test 7: Delete the book
    echo ""
    echo -e "${YELLOW}Test 7: Delete Book (ID=$new_id)${NC}"
    delete_status=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE ${BASE_URL}/api/books/$new_id)
    if [ "$delete_status" = "204" ] || [ "$delete_status" = "200" ]; then
        echo -e "${GREEN}✅ Successfully deleted book (HTTP $delete_status)${NC}"
    else
        echo -e "${RED}❌ Failed to delete book (HTTP $delete_status)${NC}"
    fi
else
    echo -e "${RED}❌ Failed to create book${NC}"
fi

echo ""
echo -e "${BLUE}=========================================${NC}"
echo -e "${GREEN}✅ All tests completed!${NC}"
