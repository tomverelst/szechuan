#!/bin/bash

if [ "$1" == "meeseeks" ]; then
  cat "meeseeks.txt" | perl -MList::Util=shuffle -e 'print shuffle(<STDIN>);' | head -n 1;
else
  random=$$$(date +%s)
  characters=("rick" "morty");
  selected=${characters[$random % 2 ]}
  cat "$selected.txt" | perl -MList::Util=shuffle -e 'print shuffle(<STDIN>);' | head -n 1;
fi;
