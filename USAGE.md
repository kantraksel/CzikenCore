# How to use mod

## Definitions
- Whitelist - this mod :)
- Ghost Player - name is registered, but didn't connect (and send token). Internally referenced as New Player.
- Known Player - Ghost Player, but he sent his token
- Player Storage - lists, which contain Ghost Players and Known Players

## Commands
Commands listed below can execute operators with permission level 3 or higher or admin via console.

All you need to know:
- addCziken <player> - marks the player as Ghost Player.
When user connects as the player (and sends token), becomes Known Player
- removeCziken <player> - removes Ghost Player. Player marked as Known Player is not affected by this command
- reloadCzikens - reloads Player Storage. This command is useful when modifying files

## Configuration file
Server options:
- AuthenticationTime <number/20> - time in ticks (20 ticks = 1 second). Should be enought high not to timeout regular players (default Forge timeout is 30 seconds)
- AuthenticationTries <number/1> - how many times player can send invalid token before he gets banned
- CountNoMessage <true/false> - if player does not send token, should it be counted to AuthenticationTries? Does not affect on server world security, still player can do nothing :)
- EnableAutoban <true/false> - if an IP address fails to authenticate *AuthenticationTries* times, should server ban it?
- RemovePlayerOnBan <true/false> - if operator/console bans player, should server delete his token?
- EnableDirectoryStream <true/false> - experminetal feature. Changes way that server reads local storage. *Theoretically* it's cheaper, but unconfirmed
- SaveOnStop <true/false> - save storage on server stop. Disabled by default. Remember that storage is saved on every change

Client options:
- None

## Some internal wisdom
You can ommit this section :>

When player connects, all what he can do, is rotate and see. All other interactions are blocked.
During this short period (default 60 seconds) server is waiting for authentication token.
If token does not come (or comes, but is invalid), player is kicked.
Valid token unlocks player.

If an IP failes 3 times, it is banned (added to Minecraft ban list).
If operator/console bans player, player's token is removed from Player Storage.

There are several options in config, feel free to change them.
