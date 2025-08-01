package org.loveroo.pibblexp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import org.loveroo.pibblexp.PibbleXP;
import org.loveroo.pibblexp.event.HudRender;

public class PibbleXPClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRender armorHud = new HudRender();

        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.of(PibbleXP.MOD_ID, "pibblexp"), armorHud);
        ClientTickEvents.END_CLIENT_TICK.register(armorHud::update);
    }
}
