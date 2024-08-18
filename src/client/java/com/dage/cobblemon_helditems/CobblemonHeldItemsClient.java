package com.dage.cobblemon_helditems;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CobblemonHeldItemsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cobblemon_helditems");

    private static final HashMap<UUID, ItemStack> cachedServerHeldItems = new HashMap<>();
    @Override
    public void onInitializeClient() {
        //Receive events from server
        ClientPlayNetworking.registerGlobalReceiver(CobblemonHeldItems.HELD_ITEM_UPDATED, (client, handler, buf, responseSender) -> {
            UUID pokemonUuid = buf.readUuid();
            ItemStack heldItem = buf.readItemStack();

            client.execute(() -> updatedItemCache(pokemonUuid, heldItem));
        });

        ClientPlayNetworking.registerGlobalReceiver(CobblemonHeldItems.ENTITY_START_TRACKING, (client, handler, buf, responseSender) -> {
            UUID pokemonUuid = buf.readUuid();
            ItemStack heldItem = buf.readItemStack();

            client.execute(() -> addItemCache(pokemonUuid, heldItem));
        });

        ClientPlayNetworking.registerGlobalReceiver(CobblemonHeldItems.ENTITY_STOP_TRACKING, (client, handler, buf, responseSender) -> {
            UUID pokemonUuid = buf.readUuid();
            ItemStack heldItem = buf.readItemStack();

            client.execute(() -> removeItemCache(pokemonUuid, heldItem));
        });
    }

    public static ItemStack getCachedHeldItem(UUID pokemonID) {

        Pokemon fromMyParty = null;
        //try to see if the pokemon that is being rendered is part of client users party
        //TODO Make the client storage search not suck so bad
        for (Pokemon p : CobblemonClient.INSTANCE.getStorage().getMyParty()) {
            if (p==null) continue;
            PokemonEntity entity = p.getEntity();
            if (entity!=null) {
                if (entity.getUuid() == pokemonID) fromMyParty = p;
            }
        }
        if (fromMyParty != null) return fromMyParty.heldItem();//if yes, then get held item from client storage

        //otherwise check server cache for held item
        return cachedServerHeldItems.getOrDefault(pokemonID, ItemStack.EMPTY);
    }

    public static void updatedItemCache(UUID pokemonID, ItemStack heldItem) {
        if (cachedServerHeldItems.containsKey(pokemonID)) {
            //LOGGER.info("cacheUpdated");
            cachedServerHeldItems.put(pokemonID, heldItem);
        }
    }

    public static void addItemCache(UUID pokemonID, ItemStack heldItem) {
        //LOGGER.info("cacheAdded");
        cachedServerHeldItems.putIfAbsent(pokemonID, heldItem);
    }

    public static void removeItemCache(UUID pokemonID, ItemStack heldItem) {
        if (cachedServerHeldItems.containsKey(pokemonID)) {
            //LOGGER.info("cacheRemoved");
            cachedServerHeldItems.remove(pokemonID, heldItem);
        }
    }

}