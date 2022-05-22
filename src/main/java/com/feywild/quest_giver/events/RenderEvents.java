package com.feywild.quest_giver.events;

import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.QuestGuardVillager;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.quest.Quest;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.util.RenderEnum;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tallestegg.guardvillagers.entities.Guard;

import java.util.HashMap;
import java.util.Locale;

@OnlyIn(value = Dist.CLIENT)
public class RenderEvents {

    public static HashMap<String, RenderEnum> renders;

    private static final float ICON_SCALE = 1.0F;
    private static final double MAX_DISTANCE = 64.0; // MAX BLOCK DISTANCE
    private static final double FADE_PERCENTAGE = 50.0; //this cannot be 100.0.
    private static final int Y_POS = -18;
    private static final int X_POS = -8;

    @SubscribeEvent
    public static void onRenderNamePlate(RenderNameplateEvent event) {
        Entity entity = event.getEntity();

            if ((entity instanceof QuestVillager questVillager && questVillager.getVillagerData().getProfession() != VillagerProfession.NONE) ||
                    (entity instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get()) ||
                            entity instanceof  QuestGuardVillager) {

                RenderEnum icon = RenderEnum.NONE;

                if (entity instanceof QuestVillager questVillager && renders.containsKey(questVillager.getQuestNumber().id.toLowerCase(Locale.ROOT))) {
                    icon = renders.get(questVillager.getQuestNumber().id.toLowerCase(Locale.ROOT));
                }
                if (entity instanceof QuestGuardVillager guard && renders.containsKey(guard.getQuestNumber().id.toLowerCase(Locale.ROOT))){
                    icon = renders.get(guard.getQuestNumber().id.toLowerCase(Locale.ROOT));
                }
                if ((entity instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get())){
                    icon = renders.get(QuestNumber.QUEST_0014.id.toLowerCase(Locale.ROOT));
                }
                if (icon != RenderEnum.NONE) {
                    EntityRenderer<?> renderer = event.getEntityRenderer();
                    PoseStack poseStack = event.getPoseStack();

                    double squareDistance = renderer.entityRenderDispatcher.distanceToSqr(entity);
                    double fadeDistance = ((1.0 - (FADE_PERCENTAGE / 100.0)) * MAX_DISTANCE);
                    double opacityDistance = Mth.clamp(1.0 - ((Math.sqrt(squareDistance) - fadeDistance) / (MAX_DISTANCE - fadeDistance)), 0.0, 1.0);
                    float markerHeight = entity.getBbHeight() + 0.5F;

                    if (squareDistance > MAX_DISTANCE * MAX_DISTANCE) {
                        return; //STOP RENDERING MARK IF TO FAR AWAY FROM ENTITY
                    }

                    poseStack.pushPose();
                    poseStack.translate(0.0D, markerHeight, 0.0D);
                    poseStack.mulPose(renderer.entityRenderDispatcher.cameraOrientation());
                    poseStack.scale(-0.025F, -0.025F, 0.025F);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    RenderSystem.enableDepthTest();

                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) opacityDistance);  // 1.0 is full

                    renderMarker(icon.getRender(), poseStack);

                    RenderSystem.disableBlend();
                    RenderSystem.disableDepthTest();

                    poseStack.popPose();
                }
            }

    }


    private static void renderMarker(ResourceLocation resource, PoseStack poseStack){
        poseStack.pushPose();
        poseStack.scale(ICON_SCALE, ICON_SCALE, 1.0F);
        renderIcon(resource, poseStack);
        poseStack.popPose();
    }

    private static void renderIcon(ResourceLocation resource, PoseStack poseStack)
    {
        Matrix4f matrix = poseStack.last().pose();

        Minecraft.getInstance().getTextureManager().getTexture(resource).setFilter(false, false);
        RenderSystem.setShaderTexture(0, resource);

        RenderSystem.setShader(GameRenderer:: getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix, X_POS, Y_POS + 16,0).uv( 0,  1).endVertex();
        bufferbuilder.vertex(matrix, X_POS + 16, Y_POS + 16, 0).uv( 1,  1).endVertex();
        bufferbuilder.vertex(matrix, X_POS + 16, Y_POS,	0).uv( 1,  0).endVertex();
        bufferbuilder.vertex(matrix, X_POS, Y_POS, 0).uv( 0,  0).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }
}
