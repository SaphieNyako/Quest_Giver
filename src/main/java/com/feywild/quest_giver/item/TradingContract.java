package com.feywild.quest_giver.item;

import com.feywild.quest_giver.util.TooltipHelper;
import io.github.noeppi_noeppi.libx.base.ItemBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class TradingContract extends ItemBase {

    public UUID assignedToPlayer;
    public String name;

    public TradingContract(ModX mod, Properties properties) {
        super(mod, properties);
    }


    public void assignedToPlayer(Player player) {
        this.assignedToPlayer = player.getUUID();
    }

    public void signedByPlayer(Player player){
        this.name = player.getName().getString();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        if(name == null){
            TooltipHelper.addTooltip(tooltip, new TextComponent("This contract is signed by: " + " UNSIGNED"));
        } else {
            TooltipHelper.addTooltip(tooltip, new TextComponent("This contract is signed by: " + " " +  name));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
