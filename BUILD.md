# Mod development
This quick reference is based on [official Forge tutorial](https://mcforge.readthedocs.io/en/1.12.x/gettingstarted/#from-zero-to-modding)

## Setup
1. Open console with current directory set to mod directory (CzikenMod)
2. Execute `gradlew setupDecompWorkspace`
3. You can now setup and import project to your IDE
   - For Eclipse run `gradlew eclipse` and import generated project to a workspace.
   - For InteliJ there are steps described in official Forge tutorial (link above).

If you encounter any difficulties in setting up the project, go to official tutorial site.

## Compilation
1. Open console with current directory set to mod directory
2. Execute `gradlew build`
3. Output files are located in CzikenMod/build/libs
