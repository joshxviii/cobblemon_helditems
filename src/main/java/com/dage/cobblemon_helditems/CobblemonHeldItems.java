package com.dage.cobblemon_helditems;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.UUID;


public class CobblemonHeldItems implements ModInitializer {

	//CobblemonHeldItemsConfig config = new CobblemonHeldItemsConfig(new File( FabricLoader.getInstance().getConfigDir().resolve(CobblemonHeldItems.MOD_ID + ".properties").toString() ));

	public static final String MOD_ID = "cobblemon_helditems";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final HashMap<UUID, ServerPlayerEntity> cachedTrackedPokemon = new HashMap<>();

	public static final TagKey<Item> HIDDEN_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier("cobblemon_helditems", "hidden_items"));
	public static final Identifier ENTITY_START_TRACKING = new Identifier(MOD_ID, "pokemon_item_start");
	public static final Identifier ENTITY_STOP_TRACKING = new Identifier(MOD_ID, "pokemon_item_stop");
	public static final Identifier HELD_ITEM_UPDATED = new Identifier(MOD_ID, "pokemon_item_updated");

	@Override
	public void onInitialize() {
		//if (1==1) return;//TODO ADD CONFIG CHECK TO EACH EVENT CALL

		CobblemonEvents.HELD_ITEM_POST.subscribe(Priority.NORMAL, post -> {
			PokemonEntity pokemonEntity = post.getPokemon().getEntity();

			if (pokemonEntity != null) {
				if (cachedTrackedPokemon.containsKey(pokemonEntity.getUuid())) {
					for (ServerPlayerEntity player : cachedTrackedPokemon.values()){
						MinecraftServer server = player.getServer();
						ItemStack heldItem = post.getReceived();
						if(heldItem.isIn(HIDDEN_ITEMS)) heldItem = ItemStack.EMPTY;;

						PacketByteBuf data = PacketByteBufs.create();
						data.writeUuid(pokemonEntity.getUuid());
						data.writeItemStack(heldItem);

						// Send a packet to the client
						ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
						server.execute(() -> {
							ServerPlayNetworking.send(playerEntity, HELD_ITEM_UPDATED, data);
						});
					}
				}
			}
			return null;
		});

		EntityTrackingEvents.START_TRACKING.register( (trackedEntity, player) -> {
			if (trackedEntity.getClass() == PokemonEntity.class) {
				if ( ((PokemonEntity) trackedEntity).getPokemon().getOwnerUUID() == player.getUuid() ) return;//exit early if entity can be rendered client side.

				PokemonEntity pokemonEntity = (PokemonEntity)trackedEntity;
				ItemStack heldItem = pokemonEntity.getPokemon().heldItem();
				if(heldItem.isIn(HIDDEN_ITEMS)) heldItem = ItemStack.EMPTY;

				MinecraftServer server = player.getServer();

				PacketByteBuf data = PacketByteBufs.create();
				data.writeUuid(pokemonEntity.getUuid());
				data.writeItemStack(heldItem);

				// Send a packet to the client
				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
				server.execute(() -> {
					ServerPlayNetworking.send(playerEntity, ENTITY_START_TRACKING, data);

					cachedTrackedPokemon.putIfAbsent(pokemonEntity.getUuid(), playerEntity);
				});
			}
		});

		EntityTrackingEvents.STOP_TRACKING.register((trackedEntity, player) -> {
			if (trackedEntity.getClass() == PokemonEntity.class) {
				if ( ((PokemonEntity) trackedEntity).getPokemon().getOwnerUUID() == player.getUuid() ) return;//exit early if entity can be rendered client side.
				PokemonEntity pokemonEntity = (PokemonEntity)trackedEntity;

				MinecraftServer server = player.getServer();

				PacketByteBuf data = PacketByteBufs.create();
				data.writeUuid(pokemonEntity.getUuid());

				// Send a packet to the client
				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
				server.execute(() -> {
					ServerPlayNetworking.send(playerEntity, ENTITY_STOP_TRACKING, data);

					if (cachedTrackedPokemon.containsKey(pokemonEntity.getUuid())) {
						cachedTrackedPokemon.remove(pokemonEntity.getUuid(), playerEntity);
					}
				});
			}
		});
	}
}