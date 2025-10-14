#!/bin/bash

# API Test Script - Library Management System
# Tests all major endpoints

set -e

BASE_URL="http://localhost:8080"
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo "API Tests - Library Management System"
echo ""

# Get All Books
echo -e "${YELLOW}Get All Books${NC}"
books=$(curl -s ${BASE_URL}/api/books | python3 -m json.tool)
count=$(echo "$books" | grep -c "\"id\":" || true)
echo -e "${GREEN}Retrieved $count books${NC}"
echo ""

# Get Book by ID
echo -e "${YELLOW}Get Book by ID (ID=1)${NC}"
book=$(curl -s ${BASE_URL}/api/books/1)
if echo "$book" | grep -q "\"id\""; then
    title=$(echo "$book" | python3 -c "import sys, json; print(json.load(sys.stdin)['title'])" 2>/dev/null || echo "Unknown")
    echo -e "${GREEN}Retrieved: $title${NC}"
else
    echo -e "${RED}Failed${NC}"
fi
echo ""

# Search Books
echo -e "${YELLOW}Search Books (query='Java')${NC}"
results=$(curl -s "${BASE_URL}/api/books?search=Java")
search_count=$(echo "$results" | grep -c "\"id\":" || true)
echo -e "${GREEN}Found $search_count matches${NC}"
echo ""

# Create New Book
echo -e "${YELLOW}Create New Book${NC}"
new_book='{"title":"Test Book","author":"Test Author","isbn":"978-1234567890","publishedDate":"2025-01-01"}'
response=$(curl -s -X POST ${BASE_URL}/api/books -H "Content-Type: application/json" -d "$new_book")
if echo "$response" | grep -q "\"id\""; then
    new_id=$(echo "$response" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])" 2>/dev/null || echo "Unknown")
    echo -e "${GREEN}Created book ID: $new_id${NC}"
    
    # Update Book
    echo ""
    echo -e "${YELLOW}Update Book (ID=$new_id)${NC}"
    updated_book='{"title":"Test Book (Updated)","author":"Test Author","isbn":"978-1234567890","publishedDate":"2025-01-01"}'
    update_response=$(curl -s -X PUT ${BASE_URL}/api/books/$new_id -H "Content-Type: application/json" -d "$updated_book")
    if echo "$update_response" | grep -q "Updated"; then
        echo -e "${GREEN}Updated successfully${NC}"
    else
        echo -e "${RED}Failed${NC}"
    fi
    
    # Delete Book
    echo ""
    echo -e "${YELLOW}Delete Book (ID=$new_id)${NC}"
    delete_status=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE ${BASE_URL}/api/books/$new_id)
    if [ "$delete_status" = "204" ] || [ "$delete_status" = "200" ]; then
        echo -e "${GREEN}Deleted successfully (HTTP $delete_status)${NC}"
    else
        echo -e "${RED}Failed (HTTP $delete_status)${NC}"
    fi
else
    echo -e "${RED}Failed to create book${NC}"
fi

echo ""
echo -e "${GREEN}Tests completed${NC}"
