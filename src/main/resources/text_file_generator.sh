#!/bin/bash

# Check if the user provided the desired file size as an argument
if [ -z "$1" ]; then
    echo "Usage: $0 <size_in_bytes>"
    exit 1
fi

# Target size in bytes
TARGET_SIZE=$1
OUTPUT_FILE="output.txt"

# Create or clear the output file
> "$OUTPUT_FILE"

# Generate random English words using 'awk' and add them to the file
while [ $(stat -f%z "$OUTPUT_FILE") -lt $TARGET_SIZE ]; do
     awk 'BEGIN { srand(); } { if (rand() <= 1.0 / NR) line = $0 } END { print line }' /usr/share/dict/words >> "$OUTPUT_FILE"
done

echo "Generated $OUTPUT_FILE with size $(stat -f%z "$OUTPUT_FILE") bytes."
