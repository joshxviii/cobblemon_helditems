# Cobblemon - Show Held Items

This is a side mod for [Cobblemon](https://modrinth.com/mod/cobblemon) so it will not do anything without Cobblemon installed.

This mod allows Pokemon's Held Items render in the world.
When joining a server that does not have this mod installed, only your own party's items will be rendered.
The items that are hidden can be controlled by the server by adding a data pack to the server that replaces the "hidden_items.json" item tag file.

The location of the file should be:
"datapacks\[YOUR_SERVERS_DATAPACK]\data\cobblemon_helditems\tags\items\hidden_items.json"

These are the default settings of the file:
```
{
  "replace": false,
  "values": [
	"#cobblemon:held/is_held_item",
    "#cobblemon:berries"
  ]
}
```

If there are Pokemon that do not render their held item or if you would like to have custom Fakemon show their held items, you can add a locator in the model named "held_item" or "held_item_fixed".

![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/6ce70e33d590ed0fe7a20af629d77e49a94c22e6.png)

![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/baddbdececeac2a690c5907b1e157e19606a542c.png)


### Links

[Modrinth Page](https://modrinth.com/mod/cobblemon-held-items) \
[Discord](https://discord.gg/a2GbzTjF8V)
