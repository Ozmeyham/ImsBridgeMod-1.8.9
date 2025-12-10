# IMS Bridge Mod for Forge 1.8.9

IMS Bridge is a Minecraft mod designed for IMS guilds and their members. It facilitates communication between Discord
and the in-game guild chat.

- Key authentication and the Hypixel API is used to verify users are members of an IMS guild
- The mod only reads guild messages, which it sends to Discord.
- Users only see client-side messages, they are not sent through a bridge bot.
- A completely separate combined bridge chat can be used by all members of IMS guilds to communicate with each other both in-game and through Discord.

## Installation

Download the latest .jar from [releases](https://github.com/Ozmeyham/ImsBridgeMod-1.8.9/releases)
and place it in your mods folder. This is the Forge version for Minecraft 1.8.9 and requires
both Forge and Java to be installed. For the latest Fabric release (1.21 and up) see [here](https://github.com/Ozmeyham/ImsBridgeMod-1.21.5)

## Configuration

Once you have installed the mod, you will need to obtain a bridge key via /key on Discord.
This key is your bridge "password" and it is important that you do not accidentally send this in guild chat or share it with anyone.

Once you have your bridge key, you can run /bridge key \<key\> in-game (replace \<key\> with your bridge key). You should then be connected to the server
and can test this with /bridge online. 

For help w