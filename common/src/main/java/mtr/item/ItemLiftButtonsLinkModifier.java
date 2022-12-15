package mtr.item;

import mtr.CreativeModeTabs;
import mtr.block.BlockLiftButtons;
import mtr.block.BlockLiftPanelBase;
import mtr.block.BlockLiftTrackFloor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemLiftButtonsLinkModifier extends ItemBlockClickingBase {

	private final boolean isConnector;

	public ItemLiftButtonsLinkModifier(boolean isConnector) {
		super(CreativeModeTabs.ESCALATORS_LIFTS, properties -> properties.stacksTo(1));
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
		final Level world = context.getLevel();
		final BlockPos posStart = context.getClickedPos();
		final Block blockStart = world.getBlockState(posStart).getBlock();
		final Block blockEnd = world.getBlockState(posEnd).getBlock();

		if (blockStart instanceof BlockLiftTrackFloor && blockEnd instanceof BlockLiftButtons || blockStart instanceof BlockLiftButtons && blockEnd instanceof BlockLiftTrackFloor || blockStart instanceof BlockLiftTrackFloor && blockEnd instanceof BlockLiftPanelBase || blockStart instanceof BlockLiftPanelBase && blockEnd instanceof BlockLiftTrackFloor) {
			final BlockPos posFloor;
			final BlockPos posButtons;
			if (blockStart instanceof BlockLiftTrackFloor) {
				posFloor = posStart;
				posButtons = posEnd;
			} else {
				posFloor = posEnd;
				posButtons = posStart;
			}

			final BlockEntity blockEntity = world.getBlockEntity(posButtons);
			if (blockEntity instanceof BlockLiftButtons.TileEntityLiftButtons) {
				((BlockLiftButtons.TileEntityLiftButtons) blockEntity).registerFloor(posFloor, isConnector);
			}

			if (blockEntity instanceof BlockLiftPanelBase.TileEntityLiftPanel1Base) {
				((BlockLiftPanelBase.TileEntityLiftPanel1Base) blockEntity).registerFloor(posFloor, isConnector);
			}
		}
	}

	@Override
	protected boolean clickCondition(UseOnContext context) {
		final Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
		return block instanceof BlockLiftTrackFloor || block instanceof BlockLiftButtons || block instanceof BlockLiftPanelBase;
	}
}
