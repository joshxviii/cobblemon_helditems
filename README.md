# Cobblemon - Show Held Items

This is a side mod for [Cobblemon](https://modrinth.com/mod/cobblemon) so it will not do anything without Cobblemon installed.

This mod allows Pokémon's Held Items render in the world.
When joining a server that does not have this mod installed, only your own Pokémon's items will be rendered.

![Cubebone and Marowak holding bones.](https://cdn.modrinth.com/data/cached_images/1cd933786f21a869aaabacfc9f88b72d57673116.png)

![Video Example](https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExOHBuejZzdXZ0YW9pbmh0eTN4endtYWMzNWIwdWhva2NobnJ0NGlmdyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/rSOM0Jg9JGPMtgmfjC/giphy.gif)

## Technical Information

### Server Configuration

The items that are hidden can be controlled by the server by adding a data pack to the server that replaces the "hidden_items.json" item tag file.

The location of the file should be:
"datapacks\[YOUR_SERVER'S_DATAPACK]\data\cobblemon_helditems\tags\items\hidden_items.json"

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
Any modded or vanilla items can be added here.

### Compatibility With Other Models

If there are Pokemon that do not render their held item or if you would like to have custom Fakemon show their held items, you can add a locator in the model named "held_item" or "held_item_fixed".

The "held_item" locator will render held items the same way that players render items. This mostly effects items like the spyglass, trident and other tools.
![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/6ce70e33d590ed0fe7a20af629d77e49a94c22e6.png)

Sometimes it doesnt look quite right to have an item held the same way as the player. So for bird or dog-like pokemon, it may be best to use "held_item_fixed". This will render the flat default item model.
![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/baddbdececeac2a690c5907b1e157e19606a542c.png)

To change the rotation of the item, simply change the rotation of the locator.

If you have any other concerns or questions, you can inquire [here](https://github.com/joshxviii/cobblemon_helditems/issues).
Thank you!


### Links

[Modrinth Page](https://modrinth.com/mod/cobblemon-held-items) \
[Discord](https://discord.gg/a2GbzTjF8V)
