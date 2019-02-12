Configuration
1. Install mongo db on local host
    SpringMongoConfig - spring mongo configuration class.
2. Build in favorite IDE or command line (using IDEA)


Command Processing

CommandRequest - stateful object that acts as a service facade to the command processor.  It decouples the
    command processor from the command.

Command Processor - processes the command and provides context.

Player's Visited Location

Player location is tracked by logging modifications to a Location in the PlayerLocations collection.  The
composite key is player.id and location.id (of the original location).   When a player is moved on the map, the
current location is checked for modifications.  If none our found, then use the current location otherwise use the
modified.