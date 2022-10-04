package com.feywild.quest_giver.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper {

    @OnlyIn(Dist.CLIENT)
    public static boolean isHoldingShift() {
        return isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.getKey());
    }

    private static boolean isKeyDown(long window, InputConstants.Key key) {
        int value = key.getValue();
        if(value != InputConstants.UNKNOWN.getValue()){
            try{
                if(key.getType() == InputConstants.Type.KEYSYM){
                    return InputConstants.isKeyDown(window, value);
                } else if (key.getType() == InputConstants.Type.MOUSE){
                    return GLFW.glfwGetMouseButton(window, value) == GLFW.GLFW_PRESS;
                }
            } catch (Exception ignored){}
        }
        return false;
    }
}
