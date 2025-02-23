0.35 ms avrage FrameTime (game loop)
____________________________________
0.02 ms avrage update time
0.20 ms avrage render time
0.02 ms avrage delay from benchmarker

if u wanna play around with difrent update fucntions follow the comments in PlatformerGame.cpp function GameLoop

to build up LvL's check out the functions LvL0 && LvL1 && LvL2 in LvLManager.cpp

to add more enemies simply create a class and have it enherit from enemy.h as it's the base enemy

(there is a slight problem with collition handleing atm alowwing player to phase through enemies sometimes) (player still can't kill enemies...)

## Folder Structure

The workspace contains three folders:

- `bin`: the folder where binary files are stored
- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies