# 1.4.24 changelog

## Fixes
- Fix gravity issue when switching dimension
- Fix gravity doesn't reset to normal with the gravity normalizer module
- Fix movement keys slowing down the lander
- Fix old oxygenated position are not always cleared
- Fix launchpad public/private label being inverted in the launchpad command
- Fix launchpad serialization
- Fix launchpad share owner check inverted in the launchpad command
- Fix launchpad integrity issues in multiplayer mode
- Fix private launchpads' data being sent to every player
- Fix launchpad teleport not checking if the player is allowed to use the launchpad
- Fix launchpad share crashing on launchpads loaded from a save
- Fix antennas being set to a launchpad the player doesn't own
- Fix launchpad owner name sometimes not matching the player, locking owners out of their own launchpad
- Fix launchpad creation not checking for duplicate names or ids
- Fix creating a launchpad without operator permission
- Fix removing a launchpad by id crashing when the id doesn't exist
- Fix launchpad share suggestions showing other players' private launchpads
- Fix the launch button using a previously selected launchpad after switching planet
- Fix an empty launchpad whitelist adding a blank entry
- Fix jet module draining the wrong fluid
- Fix jet suit normal and hover modes not consuming fuel
- Fix jet suit and jet module flight state impacting other players
- Fix suit ticking even if it's not equipped