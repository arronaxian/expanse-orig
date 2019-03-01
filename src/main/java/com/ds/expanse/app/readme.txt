Configuration:
1. Install mongo db on local host
    SpringMongoConfig - spring mongo configuration class.
2. Build in favorite IDE or command line (using IDEA)
3. Run it and connect to http://localhost:8080/expanse

Command Processing

CommandRequest - stateful object that acts as a service facade to the command processor.  It decouples the
    command processor from the command.

Command Processor - processes the command and provides context.

Features:
* Transition from location to location.
* Player's Visited Location Tracking
* Player's Altered Location Tracking
- Pick up and drop items

Design:
* Location
The description of A point of interest on the map.
* LocationTransition
Describes movement from one location to another.  Both locations must exist.
* Player Visited Location
Tracks player visited and current location. Persists the PlayerVisitedLocationDO into the playervisitedlocation collection
each time a location is transitioned.
* Player Altered Location
Track player alterations to the current location.  Persists the PlayerAlteredLocationDO to the playeralteredlocation
collection each time the location is altered.  The altered location is always preferred to the original location.
* Multimap support




