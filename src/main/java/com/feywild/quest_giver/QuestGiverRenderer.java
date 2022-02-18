package com.feywild.quest_giver;

import com.feywild.quest_giver.renderer.ExclamationMarkerRenderer;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class QuestGiverRenderer {

    @SubscribeEvent
    public static void onRenderNamePlate(RenderNameplateEvent event) {
        ExclamationMarkerRenderer.renderExclamationMarker(event.getEntityRenderer(), event.getEntity(), event.getContent(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
    }
}
