#!/usr/bin/env bash

mkdir -p ./structures

for file in ./structures-old/*.json; do
  filename=$(basename "$file")

  jq '
  {
    name: .name,
    materials: ([.blocks[][][] | select(. != null) | .material] | group_by(.) | map({key: .[0], value: length}) | from_entries),
    blocks: {
      x: .blocksX,
      y: .blocksY,
      z: .blocksZ,
      blocks: .blocks
    }
  }' "$file" > "./structures/$filename"

  echo "Processed: $filename"
done

echo "Conversion complete!"