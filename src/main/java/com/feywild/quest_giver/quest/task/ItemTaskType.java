package com.feywild.quest_giver.quest.task;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public abstract class ItemTaskType implements TaskType<Ingredient, ItemStack> {
    @Override
    public Class<Ingredient> element() {
        return Ingredient.class;
    }

    @Override
    public Class<ItemStack> testType() {
        return ItemStack.class;
    }

    @Override
    public boolean checkCompleted(ServerPlayer player, Ingredient element, ItemStack match) {
        return element.test(match);
    }

    @Override
    public Ingredient fromJson(JsonObject json) {
        return Ingredient.fromJson(json.get("item"));
    }

    @Override
    public JsonObject toJson(Ingredient element) {
        JsonObject json = new JsonObject();
        json.add("item", element.toJson());
        return json;
    }

    @Nullable
    @Override
    public Item icon(Ingredient element) {
        ItemStack[] matching = element.getItems();
        if (matching.length == 1 && !matching[0].isEmpty()) {
            return matching[0].getItem();
        } else {
            return null;
        }
    }
}
