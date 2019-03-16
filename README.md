# Server Simplified

A fabric mod that adds basic commands for server owners

## Commands
`/feed [<target>]` Restores players' health. Will target yourself if no target is specified.   
`/heal [<target>]` Restore players' hunger. Will target yourself if no target is specified.  
`/mute <target> [<time>]` Mutes players. See [this section](#Mute time) for the formatting of `time`  
`/unmute <target>` Unmutes players.  
`/seekinv <target>` Opens the specified player's inventory.  
`/staffchat [<message>]` Toggle, or send a message to, staff chat. See [staff chat](#Staff chat)  
`/vanish [<target>]` Vanishes or appears players. Will target yourself if no target is specified.

## Mute time

When muting someone, it is possible to specify the duration of the mute. Durations are specified in this format:
```
<years>y <days>d <hours>h <minutes>m <seconds>s
```
You don't have to specify every variable, but they have to be in the same order as above. Spaces are ignored.

### Examples
One year and 5 days: `1y 5d`  
Five hours: `5h`  
1 day, 1 minute and 1 second: `1d1m 1s`

## Staff chat
Staff chat is a seperate channel of chat only staff are allowed to send to or receive messages from.
When running `/staffchat` with no other arguments, your staff chat mode will be toggled.
While in staff chat mode, all messages sent are sent to staff chat.

Using `/staffchat` with arguments (e.g. `/staffchat Hello!`) will not toggle your staff chat mode, but send your message straight to staff chat.

## Permissions
Every command has a permission bound to it. For all commands this is just the name of the command (e.g. `/heal`: `heal`)

For staffchat, the `staffchat.view` permission will allow a player to view, but not send to [staff chat](#staff chat).