package mtr;

import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.BlockTactileMap;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.item.ItemBlockClickingBase;
import mtr.packet.IPacket;
import mtr.render.*;
import mtr.servlet.Webserver;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MTRClient implements IPacket {

	private static boolean isReplayMod;
	private static boolean isVivecraft;
	private static boolean isPehkui;
	private static float gameTick = 0;
	private static float lastPlayedTrainSoundsTick = 0;

	private static int tick;
	private static long startSampleMillis;
	private static float startSampleGameTick;
	private static float gameTickTest;
	private static int skipTicks;
	private static int lastSkipTicks;

	public static final int TICKS_PER_SPEED_SOUND = 4;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");
	private static final int SAMPLE_MILLIS = 1000;

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_DOOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.APG_GLASS_END.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CABLE_CAR_NODE_LOWER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CABLE_CAR_NODE_UPPER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.CLOCK.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CIO.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CKT.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_HEO.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_MOS.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_PLAIN.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_SHM.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STAINED.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STW.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_TSH.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_WKS.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LOGO.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1_INDENTED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_DOOR_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.RUBBISH_BIN_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.SIGNAL_LIGHT_1.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_WALL.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_ENTRANCE_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_EXIT_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_MACHINE.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENTRANCE.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_EXIT.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENQUIRY.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_ANNOUNCER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_LOADER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_UNLOADER.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_REDSTONE_SENSOR.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TRAIN_SCHEDULE_SENSOR.get());

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_20_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_40_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_60_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_80_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_120_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_160_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_200_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_300_ONE_WAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_PLATFORM.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_SIDING.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_TURN_BACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_CABLE_CAR.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_REMOVER.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_WHITE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_ORANGE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_MAGENTA.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_YELLOW.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIME.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PINK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_CYAN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PURPLE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BROWN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GREEN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_RED.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_WHITE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_ORANGE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_MAGENTA.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_YELLOW.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIME.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PINK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_GRAY.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_CYAN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PURPLE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLUE.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BROWN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GREEN.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_RED.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLACK.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.BRIDGE_CREATOR_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_4_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_5_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_CREATOR_6_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_4_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_5_9.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_3.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_5.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_7.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.TUNNEL_WALL_CREATOR_6_9.get(), ItemBlockClickingBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.BOAT_NODE_TILE_ENTITY.get(), RenderBoatNode::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY.get(), RenderClock::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_2_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 1));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY.get(), RenderPSDTop::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY.get(), RenderAPGGlass::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, PIDSType.PIDS, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, PIDSType.PIDS, 0xFF9900, 0xFF9900));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, PIDSType.PIDS, 0xFF9900, 0x33CC00, 1.25F, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_1.get(), dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2.get(), dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3.get(), dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4.get(), dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY.get(), RenderStationNameTall::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_WHITE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_GRAY_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_BLACK_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));

		RegistryClient.registerEntityRenderer(EntityTypes.SEAT.get(), RenderTrains::new);

		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_ANDESITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BEDROCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BIRCH_WOOD.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BONE_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CLAY.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COAL_ORE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COBBLESTONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_POWDER.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DARK_PRISMARINE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DIORITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_GRAVEL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_IRON_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_METAL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PLANKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_ANDESITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_DIORITE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_PILLAR.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_PILLAR.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_QUARTZ.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_STONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SNOW_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STAINED_GLASS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_BRICKS.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_WOOL.get());

		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_ANDESITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BEDROCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BIRCH_WOOD_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_BONE_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CLAY_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COAL_ORE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_COBBLESTONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_DIORITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_GRAVEL_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_IRON_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_METAL_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PLANKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SMOOTH_STONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_SNOW_BLOCK_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_STONE_BRICKS_SLAB.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_WOOL_SLAB.get());

		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_BLOCK.get());
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get());
		RegistryClient.registerBlockColors(Blocks.STATION_NAME_TALL_WALL.get());
		RegistryClient.registerBlockColors(Blocks.STATION_COLOR_POLE.get());

		MTRClientLifts.init();

		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_ACCELERATE);
		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_BRAKE);
		RegistryClient.registerKeyBinding(KeyMappings.TRAIN_TOGGLE_DOORS);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_1_NEGATIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_2_NEGATIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_3_NEGATIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_1_POSITIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_2_POSITIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_3_POSITIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_ROTATE_CATEGORY_NEGATIVE);
		RegistryClient.registerKeyBinding(KeyMappings.DEBUG_ROTATE_CATEGORY_POSITIVE);

		BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
			}
		};

		Webserver.init();

		RegistryClient.registerPlayerJoinEvent(player -> {
			Config.refreshProperties();

			isReplayMod = player.getClass().toGenericString().toLowerCase(Locale.ENGLISH).contains("replaymod");
			try {
				Class.forName("org.vivecraft.main.VivecraftMain");
				isVivecraft = true;
			} catch (Exception ignored) {
				isVivecraft = false;
			}
			try {
				Class.forName("virtuoel.pehkui.Pehkui");
				isPehkui = true;
			} catch (Exception ignored) {
				isPehkui = false;
			}

			System.out.println(isReplayMod ? "Running in Replay Mod mode" : "Not running in Replay Mod mode");
			System.out.println(isVivecraft ? "Vivecraft detected" : "Vivecraft not detected");
			System.out.println(isPehkui ? "Pehkui detected" : "Pehkui not detected");

			final Minecraft minecraft = Minecraft.getInstance();
			if (!minecraft.hasSingleplayerServer()) {
				Webserver.callback = minecraft::execute;
				Webserver.getWorlds = () -> minecraft.level == null ? new ArrayList<>() : Collections.singletonList(minecraft.level);
				Webserver.getRoutes = railwayData -> ClientData.ROUTES;
				Webserver.getDataCache = railwayData -> ClientData.DATA_CACHE;
				Webserver.start(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("mtr_webserver_port.txt"));
			}
		});
		Registry.registerPlayerQuitEvent(player -> Webserver.stop());
	}

	public static boolean isReplayMod() {
		return isReplayMod;
	}

	public static boolean isVivecraft() {
		return isVivecraft;
	}

	public static boolean isPehkui() {
		return isPehkui;
	}

	public static float getGameTick() {
		return gameTick;
	}

	public static void incrementGameTick() {
		final float lastFrameDuration = getLastFrameDuration();
		gameTickTest += lastFrameDuration;
		if (isReplayMod || tick == 0) {
			gameTick += lastFrameDuration;
		}
		tick++;
		if (tick >= skipTicks) {
			tick = 0;
		}

		final long millis = System.currentTimeMillis();
		if (millis - startSampleMillis >= SAMPLE_MILLIS) {
			skipTicks = Math.round((gameTickTest - startSampleGameTick) * 50 / (millis - startSampleMillis));
			startSampleMillis = millis;
			startSampleGameTick = gameTickTest;
			if (skipTicks != lastSkipTicks) {
				System.out.println("Tick skip updated to " + skipTicks);
			}
			lastSkipTicks = skipTicks;
		}
		ClientData.tick();
	}

	public static float getLastFrameDuration() {
		return MTRClient.isReplayMod ? 20F / 60 : Minecraft.getInstance().getDeltaFrameTime();
	}

	public static boolean canPlaySound() {
		if (gameTick - lastPlayedTrainSoundsTick >= TICKS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsTick = gameTick;
		}
		return gameTick == lastPlayedTrainSoundsTick && !Minecraft.getInstance().isPaused();
	}

	@FunctionalInterface
	public interface RegisterItemModelPredicate {
		void accept(String id, Item item, String tag);
	}
}
