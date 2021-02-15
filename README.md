# CzikenCore 4

This is a Forge mod, which is essentially a better whitelist mod.

The mod is currently under development, a lot of things haven't been implemented yet.

## Installation and Configuration
1. Install the mod like any other Forge mod
2. Configuration can be changed in 
   - in-game mod properties or 
   - in MINECRAFT_DIR/config/czikencore.cfg (after launching game with mod)
   - *Default values are ok*

## Important notes
- **The mod is not recommended for premium servers.** Mojang authentication is very secure and does not require any external improvements.
- I created small guildline [here](./USAGE.md)
- Currently authentication is not secured against token capture. I'll add it as *paranoid mode*.
- Identity tokens are generated random 64-bit keys.

## License and other stuff
This mod is licensed under **MIT License**, which can be found in [LICENSE](./LICENSE) file

## Mod development
See [BUILD.md](./BUILD.md)

## Info about another Forge releases
The mod has been designed for Forge 1.12.2, but it does not mean it can't be ported. Just make an issue, i'll try to port it to chosen Forge version.

Due to it's nature, it can't be ported to server-side only Minecraft mod frameworks (like CraftBukkit, Spigot, PaperMC).
