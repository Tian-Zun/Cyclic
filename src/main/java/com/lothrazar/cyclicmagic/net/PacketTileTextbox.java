package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.ITileTextbox;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileTextbox implements IMessage, IMessageHandler<PacketTileTextbox, IMessage> {
  private BlockPos pos;
  private String text;
  public PacketTileTextbox() {}
  public PacketTileTextbox(String pword, BlockPos p) {
    pos = p;
    text = pword;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    text = tags.getString("txt");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setString("txt", text);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileTextbox message, MessageContext ctx) {
    PacketTileTextbox.checkThreadAndEnqueue(message, ctx);
    return null;
  }
  private static void checkThreadAndEnqueue(final PacketTileTextbox message, final MessageContext ctx) {
    //copied in from my PacketSyncPlayerData
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
    thread.addScheduledTask(new Runnable() {
      public void run() {
        EntityPlayerMP player = ctx.getServerHandler().player;
        World world = player.getEntityWorld();
        TileEntity tile = world.getTileEntity(message.pos);
        if (tile != null && tile instanceof ITileTextbox) {
          //TODO: PacketTilePassword reusing this?
          ((ITileTextbox) tile).setText(message.text);
          tile.markDirty();
        }
      }
    });
  }
}