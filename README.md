# InventorySaver
Bukkit plugin 1.7.10 for player NBT logging

save NBTs of all players to log file every 200 ticks

## Format

### Log files
inventory_saver/N.txt

where N = UNIX millis / 1000000

### String format in every file
M,NBT

where M = UNIX millis % 1000000 and NBT - JSON of player's NBT
