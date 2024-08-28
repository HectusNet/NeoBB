#!/usr/bin/env bash

# This script is just a utility script for getting all existing turns in the project as a simple comma-seperated list.
# This is only useful for the DefaultGame's GameInfo and will only work on UNIX-Based, if not only Linux systems.

SEARCH_DIR="./src/main/java/net/hectus/neobb/turn"
EXCLUDE_FILES=("Turn" "CounterFilter" "TExample" "FlowerTurn" "GlassWallTurn" "TTimeLimit" "TDefaultWarp" "BlockTurn" "ItemTurn" "MobTurn" "ThrowableTurn" "OtherTurn" "StructureTurn" "Warp")
EXCLUDE_DIR="attributes"

FIND_CMD="find \"$SEARCH_DIR\" -type d -name \"$EXCLUDE_DIR\" -prune -o -type f"
for file in "${EXCLUDE_FILES[@]}"; do
  FIND_CMD+=" -not -name \"$file\".java"
done
FIND_CMD+=" -exec basename {} \;"

eval "$FIND_CMD" | sed -e 's/\.java$/.class/' | paste -sd ',' | sed 's/,/, /g'
