package primal_tech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FireSticksMessage implements IMessage {

	public int dimension, entityID, buttonID;
	public BlockPos tilePos;

	public FireSticksMessage() {
	}

	public FireSticksMessage(EntityPlayer player, BlockPos pos) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		tilePos = pos;
	}

	public FireSticksMessage(EntityPlayer player, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		tilePos = PacketUtils.readBlockPos(buf);
	}
}