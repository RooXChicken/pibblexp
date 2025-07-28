package org.loveroo.pibblexp.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.loveroo.pibblexp.PibbleXP;

import java.util.ArrayList;

public class HudRender implements HudLayerRegistrationCallback {

    private final float scale = 2.0f/3.0f;
    private final Identifier cooldownTexture = Identifier.of(PibbleXP.MOD_ID, "textures/armor_display/cooldown.png");

    private int ticks = 0;
    private int flashColor = 0xFFFF5656;

    @Override
    public void register(LayeredDrawerWrapper layeredDrawer) {
        layeredDrawer.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, Identifier.of(PibbleXP.MOD_ID, "pibblexp"), this::draw);
    }

    public void update(MinecraftClient client) {
        if(++ticks % 8 == 0) {
            ticks = 0;

            if(flashColor == 0xFFFFFFFF) {
                flashColor = 0xFFFF5656;
            }
            else {
                flashColor = 0xFFFFFFFF;
            }
        }
    }

    public void draw(DrawContext context, RenderTickCounter ticks) {
        var client = MinecraftClient.getInstance();

        var x = client.getWindow().getScaledWidth()/2.0;
        var y = client.getWindow().getScaledHeight() - 50.0;

        var matrix = context.getMatrices();

        matrix.push();
        matrix.translate(x, y, 0.0);
        matrix.scale(scale, scale, 1.0f);

        var text = client.textRenderer;

        var items = new ArrayList<ItemStack>();
        items.add(client.player.getEquippedStack(EquipmentSlot.HEAD));
        items.add(client.player.getEquippedStack(EquipmentSlot.CHEST));
        items.add(client.player.getEquippedStack(EquipmentSlot.LEGS));
        items.add(client.player.getEquippedStack(EquipmentSlot.FEET));

        var offset = 10;
        for(var i = 0; i < 4; i++) {
            var item = items.get(i);

            if(item != ItemStack.EMPTY) {
                var progress = client.player.getItemCooldownManager().getCooldownProgress(item, ticks.getTickDelta(true));
                var cooldown = (int)Math.ceil(progress * 10);

                // TODO: method is named fill :3
                context.drawTexture(RenderLayer::getGuiTexturedOverlay, cooldownTexture, -10, offset*i + 2 - cooldown - 13, 0, 0, 20, cooldown, 20, cooldown);
                context.drawCenteredTextWithShadow(text, (item.getMaxDamage() - item.getDamage()) + "", 0, offset*i - 20, getColor(item));
            }
        }

        matrix.pop();
    }

    private int getColor(ItemStack item) {
        var percentage = (item.getMaxDamage() - item.getDamage() + 0.0) / item.getMaxDamage();

        var color = 0xFF2BEE00;

        if(item.getDamage() == 0) {
            color = 0xFF099A00;
        }
        if(percentage <= 0.7) {
            color = 0xFFFF8022;
        }
        if(percentage <= 0.5) {
            color = 0xFFFFFC36;
        }
        if(percentage <= 0.25) {
            color = 0xFFD50000;
        }
        if(percentage <= 0.15) {
            color = 0xFF970000;
        }
        if(percentage <= 0.08) {
            color = flashColor;
        }

        return color;
    }
}
