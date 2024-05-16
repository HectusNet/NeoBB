package net.hectus.bb.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder editItem(@NotNull Consumer<ItemStack> itemEdit) {
        itemEdit.accept(item);
        return this;
    }

    public ItemBuilder editMeta(@NotNull Consumer<ItemMeta> metaEdit) {
        metaEdit.accept(meta);
        return this;
    }

    public ItemBuilder name(Component name) {
        meta.displayName(name);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        meta.lore(lore);
        return this;
    }

    public ItemBuilder addLore(Component lore) {
        List<Component> newLore = meta.hasLore() ? new ArrayList<>(Objects.requireNonNull(meta.lore())) : new ArrayList<>();
        newLore.add(lore);
        meta.lore(newLore);

        return this;
    }

    public ItemBuilder amount(@Range(from = 0, to = 64) int amount) {
        item.setAmount(amount);
        return this;
    }

    @Deprecated
    public ItemBuilder material(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level, boolean ignoreLevelRestrictions) {
        meta.addEnchant(enchantment, level, ignoreLevelRestrictions);
        return this;
    }

    public ItemBuilder unEnchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder unEnchant() {
        meta.removeEnchantments();
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder damage(int damage) {
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(damage);
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
