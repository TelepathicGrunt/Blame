package com.telepathicgrunt.blame.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* @author - TelepathicGrunt
 *
 */
@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {

	@Accessor("structureManager")
	TemplateManager blame_getStructureManager();
}
