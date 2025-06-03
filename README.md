# Cobblemon - Show Held Items

This mod is available on [Modrinth](https://modrinth.com/mod/show-held-items) and [Curseforge](https://www.curseforge.com/minecraft/mc-mods/cobblemon-show-held-items)!

This is a side mod for [Cobblemon](https://modrinth.com/mod/cobblemon) so it will not do anything without Cobblemon installed.

This mod allows Pokémon's Held Items render in the world.
When joining a server that does not have this mod installed, only your own Pokémon's items will be rendered.

![Cubebone and Marowak holding bones.](https://cdn.modrinth.com/data/cached_images/1cd933786f21a869aaabacfc9f88b72d57673116.png)

![Video Example](https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExOHBuejZzdXZ0YW9pbmh0eTN4endtYWMzNWIwdWhva2NobnJ0NGlmdyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/rSOM0Jg9JGPMtgmfjC/giphy.gif)
</br>
## Technical Information</br>

### Server Configuration

The items that are hidden or shown as hats/glasses can be controlled by the server by adding a data pack to the server that replaces the "hidden_items.json", "wearable_eye_items.json" or "wearable_hat_items.json" item tag file. Hidden items will still be visible to the Pokémon's owner.

The path for hidden items is:</br>
```datapacks\[YOUR_SERVER'S_DATAPACK]\data\show-held-items\tags\item\hidden_items.json```

These are the default settings of the file:
```json
{
  "replace": true,
  "values": [
    "#cobblemon:held/is_held_item",
    "#cobblemon:berries"
  ]
}
```
Any modded or vanilla items can be added here. 
</br></br>
The paths for eye items and hats are:</br>
```datapacks\[YOUR_SERVER'S_DATAPACK]\data\show-held-items\tags\item\wearable_eye_items.json```
</br>
```datapacks\[YOUR_SERVER'S_DATAPACK]\data\show-held-items\tags\item\wearable_hat_items.json```

Here is an example using [Villager Hats](https://modrinth.com/mod/villager-hats) and [Simple Hats](https://modrinth.com/mod/simple-hats):

<img src="https://cdn.modrinth.com/data/cached_images/9dab171c54f44c4cb3a53fc8692c98cea9d3f6fc.png" alt="Chimchar with glasses from Simple Hats" width="200"/>

wearable_eye_items.json:
```json
{
  "replace": false,
  "values": [
    "simplehats:clockface",
    "simplehats:sunglasses"
  ]
}
```
<img src="https://cdn.modrinth.com/data/cached_images/d8555c0d11cfaba15ec1bdfb645351582ef4b4c8.png" alt="Piplup with with a hat from Villager Hats" width="200"/>

wearable_hat_items.json:
```json
{
  "replace": false,
  "values": [
    "#villagerhats:hats"
  ]
}
```
</br></br>
### Compatibility With Other Models

If there are Pokemon that do not render their held item or if you would like to have custom Fakemon show their held items, you can add a locator in the model named "held_item" or "held_item_fixed".

The "held_item" locator will render held items the same way that players render items. This is most effective for items like the spyglass, trident and other tools.
![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/6ce70e33d590ed0fe7a20af629d77e49a94c22e6.png)

Sometimes it doesnt look quite right to have an item held the same way as the player. So for bird or dog-like pokemon, it may be best to use "held_item_fixed". This will render the flat default item model.
![Customizing how held items are rendered.](https://cdn.modrinth.com/data/cached_images/baddbdececeac2a690c5907b1e157e19606a542c.png)

To change the rotation of the item, simply change the rotation of the locator.</br>
</br>
To support wearable hat or eye items add locators "held_item_head" and "held_item_eyes" onto the model either near the eyes or on top of the head.
![Wearable Items in Blockbench](https://cdn.modrinth.com/data/cached_images/099fb42e63e87ca208bb377ae4b8ef06b816c958.png)

</br>

If the held item isn't quite the right size you can add modifiers to the locators using Null Objects. Give the Null Object the name of the locator you wish to modify followed by square brackets and a scale value, e.g., "held_item_head[scale=2.25]".</br>
![Scaling Wearable Items](https://cdn.modrinth.com/data/cached_images/396c2c8157d8dafd1548b372e4e01b4e671f13dd.png)

If you have any other concerns or questions, you can inquire [here](https://github.com/joshxviii/cobblemon_helditems/issues).
Thank you!
