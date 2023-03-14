package de.lordhahaha.timberframemod.block;

import de.lordhahaha.timberframemod.Timberframemod;
import de.lordhahaha.timberframemod.block.custom.RotationalBlock;
import de.lordhahaha.timberframemod.block.custom.TimberstationBlock;
import de.lordhahaha.timberframemod.block.custom.ShutterBlock;
import de.lordhahaha.timberframemod.tab.ModCreativeModeTab;
import de.lordhahaha.timberframemod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Timberframemod.MOD_ID);

    // Define Material with values
    private static final BlockBehaviour.Properties TimberframeMaterial = BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);


    public static final RegistryObject<Block> BLOCK_TIMBERSTATION = registerBlock("timberstation", () -> new TimberstationBlock(TimberframeMaterial.noOcclusion()) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_SHUTTER = registerBlock("block_shutter", () -> new ShutterBlock(TimberframeMaterial.noOcclusion()) {}, ModCreativeModeTab.TIMBERFRAME_TAB);

    // Register all new blocks
    public static final RegistryObject<Block> BLOCK_PLASTER = registerBlock("block_plaster", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_BASIC = registerBlock("block_basic", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_DOWN = registerBlock("block_down", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_UP = registerBlock("block_up", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CROSS = registerBlock("block_cross", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CROSS_SMALL_STRAIGHT = registerBlock("block_cross_small_straight", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CROSS_SMALL_CURVED = registerBlock("block_cross_small_curved", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CROSS_RING = registerBlock("block_cross_ring", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_STRAIGHT_SINGLE = registerBlock("block_straight_single", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_STRAIGHT_DOUBLE = registerBlock("block_straight_double", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_TOP_LEFT = registerBlock("block_top_left", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_TOP_RIGHT = registerBlock("block_top_right", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_BOTTOM_LEFT = registerBlock("block_bottom_left", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_BOTTOM_RIGHT = registerBlock("block_bottom_right", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_EDGE = registerBlock("block_edge", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CEILING_EDGE = registerBlock("block_ceiling_edge", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_CEILING = registerBlock("block_ceiling", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_HEXA = registerBlock("block_hexa", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_MAN_LEFT = registerBlock("block_man_left", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_MAN_RIGHT = registerBlock("block_man_right", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_PEDIMENT_LEFT = registerBlock("block_pediment_left", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_PEDIMENT_RIGHT = registerBlock("block_pediment_right", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_SHUTTER_LEFT = registerBlock("block_shutter_left", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);
    public static final RegistryObject<Block> BLOCK_SHUTTER_RIGHT = registerBlock("block_shutter_right", () -> new RotationalBlock(TimberframeMaterial) {}, ModCreativeModeTab.TIMBERFRAME_TAB);

    // --- Helper-functions ---
    // Register a new Block with Blockitem
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    // Create and Register the Blockitem for a given Block
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

    }
}
