package net.montoyo.wd.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.montoyo.wd.block.item.KeyboardItem;
import net.montoyo.wd.core.CraftComponent;
import net.montoyo.wd.core.DefaultUpgrade;
import net.montoyo.wd.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;

@SuppressWarnings({"unchecked", "unused"})
public class ItemRegistry {
    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("webdisplays");

    protected static final DeferredItem<Item>[] COMP_CRAFT_ITEMS = new DeferredItem[CraftComponent.values().length];
    protected static final DeferredItem<Item>[] UPGRADE_ITEMS = new DeferredItem[DefaultUpgrade.values().length];

    public static final DeferredHolder<Item, ItemScreenConfigurator> CONFIGURATOR = ITEMS.register("screencfg", () -> new ItemScreenConfigurator(new Item.Properties()));
    public static final DeferredHolder<Item, ItemOwnershipThief> OWNERSHIP_THEIF = ITEMS.register("ownerthief", () -> new ItemOwnershipThief(new Item.Properties()));
    public static final DeferredHolder<Item, ItemLinker> LINKER = ITEMS.register("linker", () -> new ItemLinker(new Item.Properties()));
    public static final DeferredHolder<Item, ItemMinePad2> MINEPAD = ITEMS.register("minepad", () -> new ItemMinePad2(new Item.Properties()));
    public static final DeferredHolder<Item, ItemLaserPointer> LASER_POINTER = ITEMS.register("laserpointer", () -> new ItemLaserPointer(new Item.Properties()));

    static {
        DefaultUpgrade[] defaultUpgrades = DefaultUpgrade.values();
        for (int i = 0; i < defaultUpgrades.length; i++) {
            DefaultUpgrade upgrade = defaultUpgrades[i];
            UPGRADE_ITEMS[i] = ITEMS.register("upgrade_" + upgrade.name().toLowerCase(Locale.ROOT), () -> new ItemUpgrade(upgrade));
        }

        CraftComponent[] components = CraftComponent.values();
        for (int i = 0; i < components.length; i++) {
            CraftComponent cc = components[i];
            COMP_CRAFT_ITEMS[i] = ITEMS.register("craftcomp_" + cc.name().toLowerCase(Locale.ROOT), () -> new ItemCraftComponent(new Item.Properties()));
        }
    }

    public static final DeferredItem<Item> SCREEN = ITEMS.register("screen", () -> new BlockItem(BlockRegistry.SCREEN_BLOCk.get(), new Item.Properties()/*.tab(WebDisplays.CREATIVE_TAB)*/));

    public static final DeferredItem<Item> KEYBOARD = ITEMS.register("keyboard", () -> new KeyboardItem(BlockRegistry.KEYBOARD_BLOCK.get(), new Item.Properties()/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredItem<Item> REDSTONE_CONTROLLER = ITEMS.register("redctrl", () -> new BlockItem(BlockRegistry.REDSTONE_CONTROL_BLOCK.get(), new Item.Properties()/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredItem<Item> REMOTE_CONTROLLER = ITEMS.register("rctrl", () -> new BlockItem(BlockRegistry.REMOTE_CONTROLLER_BLOCK.get(), new Item.Properties()/*.tab(WebDisplays.CREATIVE_TAB)*/));
    public static final DeferredItem<Item> SERVER = ITEMS.register("server", () -> new BlockItem(BlockRegistry.SERVER_BLOCK.get(), new Item.Properties()/*.tab(WebDisplays.CREATIVE_TAB)*/));

    public static DeferredItem<Item> getComputerCraftItem(int index) {
        return COMP_CRAFT_ITEMS[index];
    }

    public static DeferredItem<Item> getUpgradeItem(int index) {
        return UPGRADE_ITEMS[index];
    }

    public static int countCompCraftItems() {
        return COMP_CRAFT_ITEMS.length;
    }

    public static int countUpgrades() {
        return UPGRADE_ITEMS.length;
    }

    public static boolean isCompCraftItem(Item item) {
        for (DeferredItem<Item> itemRegistryObject : COMP_CRAFT_ITEMS)
            if (item == itemRegistryObject.get())
                return true;
        return false;
    }
}
