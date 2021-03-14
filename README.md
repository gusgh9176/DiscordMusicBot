# DiscordMusicBot

It's a music bot that can be used in discord.

You can use your own music bot by issuing a discord bot token and entering it into the code. 
This is expected to be faster than the music bot written in Python.

---

![Plat Form](https://img.shields.io/badge/Platform-Java-red)
[![GitHub issues](https://img.shields.io/github/issues/gusgh9176/DiscordMusicBot)](https://github.com/gusgh9176/DiscordMusicBot/issues)
[![GitHub stars](https://img.shields.io/github/stars/gusgh9176/DiscordMusicBot)](https://github.com/gusgh9176/DiscordMusicBot/stargazers)
[![GitHub release](https://img.shields.io/badge/release-v0.1-green)](https://github.com/gusgh9176/DiscordMusicBot)


## Installation 
git clone https://github.com/gusgh9176/DiscordMusicBot.git

or

Download ZIP(https://github.com/gusgh9176/DiscordMusicBot/archive/master.zip)

---
## Usage
From creating bot accounts to executing commands
### 1. Create a bot and get its token
#### Please refer to the corresponding page. https://javacord.org/wiki/getting-started/creating-a-bot-account.html#create-a-bot-and-get-its-token
### 2. Set a your bot token in code
#### src/main/java/com/loginBot/LoginBot.java
![LoginBotImg](https://github.com/gusgh9176/DiscordMusicBot/blob/master/readmeImgs/LoginBot.PNG?raw=true)
![setTokenImg](https://github.com/gusgh9176/DiscordMusicBot/blob/master/readmeImgs/setToken.png?raw=true)
### 3. Invite a music bot to a discord server
#### https://discord.com/oauth2/authorize?client_id="INPUT YOUR DISCORD BOT CLIENT_ID"&scope=bot&permissions=0
#### Example) https://discord.com/oauth2/authorize?client_id=abcd4869efghijklmnopqrstuvwxyz&scope=bot&permissions=0
### 4. Run its project using your IDE
#### Use IntelliJ for example screens
![IntelliJImg](https://github.com/gusgh9176/DiscordMusicBot/blob/master/readmeImgs/IntelliJ.PNG?raw=true)
### 5. Execute features as command input in the server chat
![ExecuteCommandImg](https://github.com/gusgh9176/DiscordMusicBot/blob/master/readmeImgs/Execute%20command.PNG?raw=true)
---
## Contributing

### Step 1
- Option 1
  - Fork this repositories

- Option 2
  - Clone this repo to your local machine using [https://github.com/gusgh9176/DiscordMusicBot.git](https://github.com/gusgh9176/DiscordMusicBot.git "https://github.com/gusgh9176/DiscordMusicBot.git")
  
### Step 2
- Work Hard

### Step 3
- Create a new pull request using [https://github.com/gusgh9176/DiscordMusicBot/*](https://github.com/gusgh9176/DiscordMusicBot/* "https://github.com/gusgh9176/DiscordMusicBot/*")

---
## Used libraries
- JAVACORD(https://github.com/Javacord/Javacord)

- LavaPlayer(https://github.com/sedmelluq/lavaplayer)

---
## Known Bugs

---
## FAQ
- **Q: The bot doesn't respond to commands.**
  - A: Please make sure the project is running.
  
- **Q: The bot doesn't respond to "!music" commands.**
  - A: The user who entered the command must be in a voice channel
