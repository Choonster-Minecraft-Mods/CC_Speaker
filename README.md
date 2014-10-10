# ComputerCraft Speaker
This is a mod for Minecraft 1.6.4 that adds the Loudspeaker, a ComputerCraft peripheral block that allows chat messages to be sent by computers.

You will be able to download it from Curse when I release it.

## API Documentation
You can find the API documentation for the peripheral [here](https://github.com/Choonster/CC_Speaker/wiki/Peripheral-API-Documentation).

## Setting up Forge Gradle
To set up Forge Gradle, run `gradlew setupDecompWorkspace`.

To generate an IDE project, run `gradlew eclipse` (for Eclipse) or `gradlew idea` (for IntelliJ IDEA).

## Building
Place Waila in the libs folder and run `gradlew build`.

Waila 1.5.2a will prevent the mod from compiling due to its dependency on ModLoader. ProfMobius fixed this, but didn't release the fix for 1.6.4.

I've created a repository with the fixed version of Waila and a Gradle build [here](https://github.com/Choonster/Waila). This is what I use to compile the mod.

If you want to look at Waila's source code in your IDE without it complaining about missing classes, you'll probably need to add NEI, CodeChickenCore and CodeChickenLib to the libs folder as well.

## Running
To run in your development environment after building, you'll need ComputerCraft 1.63 and a dev version of CodeChickenCore (for runtime deobfuscation) in your mods folder.

If you have CodeChickenCore in the libs folder, you don't need it in the mods folder.

To run in a release environment, you'll need ComputerCraft and the mod itself in your mods folder. Waila is not required.