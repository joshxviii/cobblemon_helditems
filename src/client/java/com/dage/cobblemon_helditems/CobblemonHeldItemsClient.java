package com.dage.cobblemon_helditems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CobblemonHeldItemsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cobblemon_helditems");
    public static final HashMap<UUID, ItemStack> cachedServerHeldItems = new HashMap<>();
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

            client.execute(() -> removeItemCache(pokemonUuid));
        });
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

    public static void removeItemCache(UUID pokemonID) {
        if (cachedServerHeldItems.containsKey(pokemonID)) {
            //LOGGER.info("cacheRemoved");
            cachedServerHeldItems.remove(pokemonID);
        }
    }
}