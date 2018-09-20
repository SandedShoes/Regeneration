package me.sub.network.packets;

import io.netty.buffer.ByteBuf;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageRegenerationStyle implements IMessage {
    private NBTTagCompound style;

    public MessageRegenerationStyle() {
    }

    public MessageRegenerationStyle(NBTTagCompound nbtTagCompound) {
        this.style = nbtTagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.style = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.style);
    }

    public static class Handler implements IMessageHandler<MessageRegenerationStyle, IMessage> {

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx
         * @return an optional return message
         */
        @Override
        public IMessage onMessage(MessageRegenerationStyle message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration capability = CapabilityRegeneration.get(ctx.getServerHandler().player);
                if (capability != null)
                    capability.setStyle(message.style);
                capability.sync();
            });
            return null;
        }
    }
}
