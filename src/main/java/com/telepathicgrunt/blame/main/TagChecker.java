package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.stream.Collectors;

public class TagChecker {
    public static <T> void checkTags(ITagCollection<T> tagCollection, TagRegistry<T> tagRegistry, String tagType) {
        Map<ResourceLocation, ITag<T>> currentTags = tagCollection.getAllTags();
        tagRegistry.getWrappers().stream()
                .filter(e -> e.getClass().getTypeName().contains("OptionalNamedTag") && !currentTags.containsKey(e.getName()))
                .collect(Collectors.toMap(ITag.INamedTag::getName, namedTag -> namedTag,
                (e, v) -> {
                    TagRegistry.NamedTag<T> optionalNamedTag = (TagRegistry.NamedTag<T>) e;
                    TagRegistry.NamedTag<T> optionalNamedTag2 = (TagRegistry.NamedTag<T>) v;
                    Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report ITagCollectionSupplier " + tagType + Blame.VERSION + " ******************" +
                            "\n tag original: " + optionalNamedTag.getName() + " - " + optionalNamedTag.getValues() +
                            "\n tag duplicate: " + optionalNamedTag2.getName() + " - " + optionalNamedTag2.getValues() +
                            "\n Collection to check against for duplicates   : " + currentTags.keySet() +
                            "\n TagRegistry used for checking for duplicates : " + tagRegistry.getWrappers().stream().map(ITag.INamedTag::getName).collect(Collectors.toSet()) +
                            "\n");

                    throw new IllegalStateException(String.format("Duplicate key %s", e));
                }));

    }
}
