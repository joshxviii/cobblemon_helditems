package dage.visualhelditems;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientBox;
import com.cobblemon.mod.common.client.storage.ClientPC;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.client.storage.ClientStorageManager;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class CobblemonHeldItemsClient implements ClientModInitializer {

    public static final String MOD_ID = "cobblemon_helditems";
    public static final Logger LOGGER = LoggerFactory.getLogger("cobblemon_helditems");
    /**
     * A list of pokemon entities this client player is currently tracking.
     */
    public static final HashMap<UUID, ItemStack> cachedHeldItems = new HashMap<>();

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
        if (cachedHeldItems.containsKey(pokemonID)) {
            //LOGGER.info("cacheUpdated");
            cachedHeldItems.put(pokemonID, heldItem);
        }
    }

    public static void addItemCache(UUID pokemonID, ItemStack heldItem) {
        //LOGGER.info("cacheAdded");
        cachedHeldItems.putIfAbsent(pokemonID, heldItem);
    }

    public static void removeItemCache(UUID pokemonID) {
        //LOGGER.info("cacheRemoved");
        cachedHeldItems.remove(pokemonID);
    }


    public static ItemStack getHeldItem(UUID pokemonID) {
        //TODO Make the client storage search not so insanely bad..

        //check server item cache
        HashMap<UUID, ItemStack> cache = cachedHeldItems;
        if (cache.containsKey(pokemonID)) {
            LOGGER.info("WTF");
            return cache.get(pokemonID);
        }
        //if not in cache check client storage
        else {
            ClientStorageManager storage = CobblemonClient.INSTANCE.getStorage();
            ClientParty party = storage.getMyParty();
            Collection<ClientPC> pcs = storage.getPcStores().values();
            //See if the pokemon that is being rendered is part of client users party
            for (Pokemon p : party) {
                if (p == null) continue;
                PokemonEntity partyEntity = p.getEntity();
                if (partyEntity == null) continue;
                if (partyEntity.getUuid() == pokemonID) return p.heldItem();
            }
            //If not then check PC **this is from pokemon roaming around from the pasture block**
            //AKA triple for-loop nightmare
            for (ClientPC pc : pcs) {
                for (ClientBox box : pc.getBoxes()) {
                    for (Pokemon p : box.getSlots()) {
                        if (p == null) continue;
                        PokemonEntity partyEntity = p.getEntity();
                        if (partyEntity == null) continue;
                        if (partyEntity.getUuid() == pokemonID) return p.heldItem();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }


}