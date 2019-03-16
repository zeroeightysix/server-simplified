package me.zeroeightsix.basicstaffmod.mixin;

import com.mojang.brigadier.CommandDispatcher;
import me.zeroeightsix.basicstaffmod.BasicStaffMod;
import me.zeroeightsix.basicstaffmod.commands.HealCommand;
import me.zeroeightsix.basicstaffmod.commands.MuteCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Shadow
	private ServerCommandManager commandManager;

	@Inject(at = @At("HEAD"), method = "start()V")
	private void start(CallbackInfo info) {
		CommandDispatcher dispatcher = commandManager.getDispatcher();

		HealCommand.register(dispatcher);
		MuteCommand.register(dispatcher);
	}

	@Inject(at = @At("HEAD"), method = "shutdown()V")
	private void shutdown(CallbackInfo info) {
		BasicStaffMod.save();
	}

}
