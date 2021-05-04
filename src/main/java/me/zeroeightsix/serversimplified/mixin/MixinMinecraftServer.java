package me.zeroeightsix.serversimplified.mixin;

import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Inject(at = @At("HEAD"), method = "shutdown()V")
	private void shutdown(CallbackInfo info) {
		ServerSimplified.shutdown();
	}

}
