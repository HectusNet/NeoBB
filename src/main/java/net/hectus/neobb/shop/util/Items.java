package net.hectus.neobb.shop.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;
import java.util.function.BiConsumer;

public class Items {
    public static final ItemStack WHITE_BACKGROUND = new net.hectus.neobb.shop.util.ItemBuilder(Material.WHITE_STAINED_GLASS).name(Component.empty()).build();
    public static final ItemStack GRAY_BACKGROUND = new net.hectus.neobb.shop.util.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).build();
    public static final ItemStack BLACK_BACKGROUND = new net.hectus.neobb.shop.util.ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).build();

    public static class ClickItem extends AbstractItem {
        protected final ItemStack item;
        protected final BiConsumer<Player, InventoryClickEvent> clickConsumer;

        public ClickItem(ItemStack item, BiConsumer<Player, InventoryClickEvent> clickConsumer) {
            this.item = item;
            this.clickConsumer = clickConsumer;
        }

        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(item);
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            clickConsumer.accept(player, event);
        }
    }
    
    public static class ScrollItem extends xyz.xenondevs.invui.item.impl.controlitem.ScrollItem {
        protected final boolean up;

        public ScrollItem(boolean up) {
            super(up ? -1 : 1);
            this.up = up;
        }

        @Override
        public ItemProvider getItemProvider(ScrollGui<?> gui) {
            try {
                return new SkullBuilder("MHF_Arrow" + (up ? "Up" : "Down")).setDisplayName(new AdventureComponentWrapper(Component.text("Scroll " + (up ? "Up" : "Down"))));
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PageItem extends xyz.xenondevs.invui.item.impl.controlitem.PageItem {
        protected final boolean forward;

        public PageItem(boolean forward) {
            super(forward);
            this.forward = forward;
        }

        @Override
        public ItemProvider getItemProvider(PagedGui<?> gui) {
            try {
                return new SkullBuilder("MHF_Arrow" + (forward ? "Right" : "Left")).setDisplayName(new AdventureComponentWrapper(Component.text((forward ? "Next" : "Last") + " Page")));
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
