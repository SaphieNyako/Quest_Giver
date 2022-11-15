package com.feywild.quest_giver.item;

import com.feywild.quest_giver.util.QuestGiverPlayerData;
import com.feywild.quest_giver.util.TooltipHelper;
import io.github.noeppi_noeppi.libx.base.ItemBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TradingContract extends ItemBase {


    private final String profession;

    public TradingContract(ModX mod, Properties properties, String profession) {
        super(mod, properties);
        this.profession = profession;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockPos clickedPos = context.getClickedPos();
        if(player instanceof ServerPlayer && context.getLevel().getBlockState(clickedPos).getBlock() instanceof LecternBlock){
            if(!isSignedByPlayer(player)) {
                QuestGiverPlayerData.get(player).putBoolean(this.profession, true);
                ((ServerPlayer) player).sendMessage(new TextComponent("You signed this contract"), player.getUUID());
                player.swing(hand,true);
            } else {
                ((ServerPlayer) player).sendMessage(new TextComponent("You have already signed this contract"), player.getUUID());
            }
            return InteractionResult.sidedSuccess(Objects.requireNonNull(player).level.isClientSide);
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        TooltipHelper.addTooltip(tooltip, new TextComponent("Shift-Right click on a Lectern to sign this contract, after it's signed you can trade with villagers."));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public boolean isSignedByPlayer(Player player){
        return QuestGiverPlayerData.get(player).getBoolean(this.profession);
    }

    public String getProfession() {
        return profession;
    }
}
