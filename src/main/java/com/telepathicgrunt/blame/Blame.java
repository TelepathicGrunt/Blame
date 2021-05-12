package com.telepathicgrunt.blame;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Blame implements ModInitializer
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "N/A";

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MODID)
                .ifPresent(container -> VERSION = container.getMetadata().getVersion().toString());

        Blame.LOGGER.log(Level.ERROR, "Blame " + VERSION + " initialized");

        // Unregistered worldgen test
//        ConfiguredFeature<?,?> well = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("tgtr:fdgfg"), Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//
//        // Make the structure and features list mutable for modification later
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((GenerationStageMixin)BuiltinBiomes.PLAINS.getGenerationSettings()).getGSFeatures();
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableGenerationStages = new ArrayList<>();
//
//        // Fill in generation stages so there are at least 9 or else Minecraft crashes.
//        // (we need all stages for adding features/structures to the right stage too)
//        for(int currentStageIndex = 0; currentStageIndex < Math.max(10, tempFeature.size()); currentStageIndex++){
//            if(currentStageIndex >= tempFeature.size()){
//                mutableGenerationStages.add(new ArrayList<>());
//            }else{
//                mutableGenerationStages.add(new ArrayList<>(tempFeature.get(currentStageIndex)));
//            }
//        }
//
//        // Make the Structure and GenerationStages (features) list mutable for modification later
//        ((GenerationStageMixin)BuiltinBiomes.PLAINS.getGenerationSettings()).setGSFeatures(mutableGenerationStages);
//
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> well);
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> well);
    }

    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.locate.failed"));

    private static int execute(ServerCommandSource source, StructureFeature<?> structureFeature) throws CommandSyntaxException {
        BlockPos blockPos = new BlockPos(source.getPosition());
        BlockPos blockPos2 = source.getWorld().locateStructure(structureFeature, blockPos, 100, false);
        if (blockPos2 == null) {
            throw FAILED_EXCEPTION.create();
        } else {
            return sendCoordinates(source, structureFeature.getName(), blockPos, blockPos2, "commands.locate.success");
        }
    }

    public static int sendCoordinates(ServerCommandSource source, String structure, BlockPos sourcePos, BlockPos structurePos, String successMessage) {
        int i = MathHelper.floor(getDistance(sourcePos.getX(), sourcePos.getZ(), structurePos.getX(), structurePos.getZ()));
        Text text = Texts.bracketed(new TranslatableText("chat.coordinates", new Object[]{structurePos.getX(), "~", structurePos.getZ()})).styled((style) -> {
            return style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + structurePos.getX() + " ~ " + structurePos.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.coordinates.tooltip")));
        });
        source.sendFeedback(new TranslatableText(successMessage, new Object[]{structure, text, i}), false);
        return i;
    }

    private static float getDistance(int x1, int y1, int x2, int y2) {
        int i = x2 - x1;
        int j = y2 - y1;
        return MathHelper.sqrt((float)(i * i + j * j));
    }
}
