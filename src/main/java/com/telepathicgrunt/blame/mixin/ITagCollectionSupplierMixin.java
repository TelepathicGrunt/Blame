package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.main.TagChecker;
import com.telepathicgrunt.blame.main.WorldSettingsImportBlame;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldSettingsImport;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.fml.packs.ResourcePackLoader;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
@Mixin(ITagCollectionSupplier.class)
public interface ITagCollectionSupplierMixin {

    /**
     * @author TelepathicGrunt
     */
    @Overwrite
    static ITagCollectionSupplier reinjectOptionalTags(ITagCollectionSupplier tagCollectionSupplier) {
        TagChecker.checkTags(tagCollectionSupplier.getBlocks(), BlockTagsAccessor.getHELPER(), "BLOCKS");
        ITagCollection<Block> blockTagCollection = BlockTagsAccessor.getHELPER().reinjectOptionalTags(tagCollectionSupplier.getBlocks());

        TagChecker.checkTags(tagCollectionSupplier.getItems(), ItemTagsAccessor.getHELPER(), "ITEMS");
        ITagCollection<Item> itemTagCollection = ItemTagsAccessor.getHELPER().reinjectOptionalTags(tagCollectionSupplier.getItems());

        TagChecker.checkTags(tagCollectionSupplier.getFluids(), FluidTagsAccessor.getHELPER(), "FLUIDS");
        ITagCollection<Fluid> fluidTagCollection = FluidTagsAccessor.getHELPER().reinjectOptionalTags(tagCollectionSupplier.getFluids());

        TagChecker.checkTags(tagCollectionSupplier.getEntityTypes(), EntityTypeTagsAccessor.getHELPER(), "ENTITIES");
        ITagCollection<EntityType<?>> entityTypeTagCollection = EntityTypeTagsAccessor.getHELPER().reinjectOptionalTags(tagCollectionSupplier.getEntityTypes());

        return new ITagCollectionSupplier() {
            @Override
            public ITagCollection<Block> getBlocks() {
                return blockTagCollection;
            }

            @Override
            public ITagCollection<Item> getItems() {
                return itemTagCollection;
            }

            @Override
            public ITagCollection<Fluid> getFluids() {
                return fluidTagCollection;
            }

            @Override
            public ITagCollection<EntityType<?>> getEntityTypes() {
                return entityTypeTagCollection;
            }
        };
    }

}
