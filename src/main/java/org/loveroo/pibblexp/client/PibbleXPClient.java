package org.loveroo.pibblexp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.loveroo.pibblexp.event.HudRender;

public class PibbleXPClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRender armorHud = new HudRender();

        HudRenderCallback.EVENT.register(armorHud);
        ClientTickEvents.END_CLIENT_TICK.register(armorHud::update);
    }
}
