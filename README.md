# CzikenCore 4

Finally I've decided to release the mod. After year of development. After 2 major reworks.
This is a Forge mod, which is essentially a better whitelist mod.
The mods is currently under development, a lot of things hasn't been implemented yet.

## Installation and Configuration
1. Install the mod like any other Forge mod
2. Configuration can be changed in 
   - in-game mod properties or 
   - in MINECRAFT_DIR/config/czikencore.cfg (after launching game with mod)
   - * Default values are recommended *

## Important notes
- **The mod is not recommended for premium servers.** Mojang authentication is very secure and does not require any external improvements.
- Docs are still under development. Right now you can only read source code or experiment with commands.
- Okay. I created small guildline. [Here](./USAGE.md)
- Currently authentication is not secured against brute force attacks and token capture. I'll add it as *paranoid mode*.
- Identity tokens are generated random DES-keys.

## License and other stuff
This mod is licensed under **MIT License**, which can be found in [LICENSE](./LICENSE) file

## Mod development
See [BUILD.md](./BUILD.md)

## Info about another Forge releases
The mod has been designed for Forge 1.12.2, but it does not mean it can't be ported. Just make an issue, i'll try to port it to chosen Forge version.
