package com.feywild.quest_giver.quest.task;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.tags.ITag;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class TaggableTaskType<T extends IForgeRegistryEntry<T>, X>
        implements TaskType<TaggableTaskType.Taggable<T>, X> {

    private final IForgeRegistry<T> registry;
    private final String key;

    protected TaggableTaskType(String key, IForgeRegistry<T> registry) {
        this.key = key;
        this.registry = registry;
    }

    @Override
    public Class<Taggable<T>> element() {
        //noinspection unchecked
        return (Class<Taggable<T>>) (Class<?>) Taggable.class;
    }

    @Override
    public Taggable<T> fromJson(JsonObject json) {
        var value = json.get(key).getAsString();
        if (value.startsWith("#")) {
            var location = ResourceLocation.tryParse(value.substring(1));
            if (location != null)
                return new Taggable<>(
                        Objects.requireNonNull(Objects.requireNonNull(registry).tags())
                                .createTagKey(location),
                        registry);
            else return null;
        }

        var location = ResourceLocation.tryParse(value);
        if (location != null) {
            var entry = registry.getValue(location);
            if (entry != null) return new Taggable<>(entry);
        }

        return null;
    }

    @Override
    public JsonObject toJson(Taggable<T> element) {
        var json = new JsonObject();
        if (element.tag.isPresent()) {
            json.addProperty(key, "#" + element.tag.get().getKey().location());
        } else {
            json.addProperty(
                    key,
                    Objects.requireNonNull(element.value.orElseThrow().getRegistryName())
                            .toString());
        }
        return json;
    }

    record Taggable<T extends IForgeRegistryEntry<T>>(Optional<T> value, Optional<ITag<T>> tag)
            implements Predicate<T> {

        public Taggable(T value) {
            this(Optional.of(value), Optional.empty());
        }

        public Taggable(TagKey<T> tagKey, IForgeRegistry<T> registriy) {
            this(
                    Optional.empty(),
                    Optional.of(Objects.requireNonNull(registriy.tags()).getTag(tagKey)));
        }

        @Override
        public boolean test(T input) {
            return tag.map(it -> it.contains(input))
                    .orElseGet(() -> value.orElseThrow().equals(input));
        }
    }
}
