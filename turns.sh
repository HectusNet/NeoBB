#!/usr/bin/env bash

# This script is just a utility script for getting all existing turns in the project as a simple comma-seperated list.
# This is only useful for the DefaultGame's GameInfo and will only work on UNIX-Based, if not only Linux systems.

SEARCH_DIR="./src/main/java/net/hectus/neobb/turn"
EXCLUDE_FILES=("CounterFilter.java" "TExample.java" "TTimeLimit.java" "BlockTurn.java" "ItemTurn.java" "MobTurn.java" "ThrowableTurn.java" "OtherTurn.java" "StructureTurn.java" "Warp")

FIND_CMD="find \"$SEARCH_DIR\" -type d -name \"$EXCLUDE_DIR\" -prune -o -type f"
for file in "${EXCLUDE_FILES[@]}"; do
  FIND_CMD+=" -not -name \"$file\""
done
FIND_CMD+=" -exec basename {} \;"

eval "$FIND_CMD" | sed -e 's/\.java$/.class/' | paste -sd ',' | sed 's/,/, /g'
