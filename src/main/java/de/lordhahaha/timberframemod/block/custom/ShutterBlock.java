package de.lordhahaha.timberframemod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class ShutterBlock extends Block{
    public static final EnumProperty<Neighbour> NEIGHBOUR = EnumProperty.create("neighbour", Neighbour.class);
    public static final EnumProperty<Neighbour> CONNECTED_BLOCK = EnumProperty.create("connected_block", Neighbour.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty HAS_TOP = BooleanProperty.create("top");
    public static final BooleanProperty HAS_BOTTOM = BooleanProperty.create("bottom");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public ShutterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SHAPE_EAST = Block.box(14, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 0, 0, 2, 16, 16);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction = blockState.getValue(FACING);
        switch (direction){
            case NORTH:
            default:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_WEST;
            case SOUTH:
                return SHAPE_NORTH;
            case WEST:
                return SHAPE_EAST;
        }
    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity p_52752_, ItemStack p_52753_) {
        if(blockState != null) {
            level.setBlock(getConnectedShutter(blockPos, blockState), blockState
                            .setValue(FACING, blockState.getValue(FACING))
                            .setValue(ACTIVE, !blockState.getValue(ACTIVE))
                            .setValue(OPEN, blockState.getValue(OPEN))
                            .setValue(CONNECTED_BLOCK,(blockState.getValue(CONNECTED_BLOCK) == Neighbour.LEFT ? Neighbour.RIGHT : Neighbour.LEFT))
                            .setValue(NEIGHBOUR, blockState.getValue(NEIGHBOUR)),
                    3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        BlockPos blockPos = placeContext.getClickedPos();
        Level level = placeContext.getLevel();
        Neighbour connecedBlock = placeContext.getPlayer().isCrouching() ? Neighbour.LEFT : Neighbour.RIGHT;
        BlockState neighbourBlockState = level.getBlockState(getConnectedShutter(blockPos, connecedBlock, placeContext.getHorizontalDirection().getOpposite()));

        if(neighbourBlockState.canBeReplaced(placeContext)) {
            BlockState newBlock = this.defaultBlockState();
            newBlock = newBlock
                    .setValue(FACING, placeContext.getHorizontalDirection().getOpposite())
                    .setValue(ACTIVE, Boolean.TRUE)
                    .setValue(OPEN, Boolean.TRUE)
                    .setValue(CONNECTED_BLOCK, connecedBlock);

            boolean hasTop = hasTopShutterAndLink(newBlock, blockPos, level);
            boolean hasBottom = hasBottomShutterAndLink(newBlock, blockPos, level);
            newBlock = newBlock
                    .setValue(NEIGHBOUR, linkToNeighbourShutter(newBlock, blockPos, level))
                    .setValue(HAS_TOP, hasTop)
                    .setValue(HAS_BOTTOM, hasBottom);

            return newBlock;
        }

        else
            return null;
    }
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        toggleShutterRow(blockState, blockPos, level);
        toggleNeighbourShutter(blockState, blockPos, level); // Try to change another Shutter boundle
        level.levelEvent(player, 1006, blockPos, 0);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    private void toggleShutter(BlockState blockState, BlockPos pos, Level level) {
        Direction dir = blockState.getValue(FACING);

        // Update the clicked Block
        blockState = blockState.setValue(ACTIVE, !blockState.getValue(ACTIVE))
                               .setValue(OPEN, !blockState.getValue(OPEN));
        level.setBlockAndUpdate(pos, blockState);

        // Update the connected Block
        pos = getConnectedShutter(pos, blockState);
        blockState = level.getBlockState(pos)
                          .setValue(ACTIVE, !blockState.getValue(ACTIVE))
                          .setValue(OPEN, !blockState.getValue(OPEN));
        level.setBlockAndUpdate(pos, blockState);
    }

    private void toggleShutterRow(BlockState blockState, BlockPos blockPos,Level level){
        toggleShutterAbove(blockState, blockPos, level);
        toggleShutterBelow(blockState, blockPos, level);
        toggleShutter(blockState, blockPos, level);
    }

    private void toggleShutterAbove(BlockState blockState, BlockPos blockPos, Level level){
        if(blockState.getValue(HAS_TOP)){
            BlockPos blockPosAbove = blockPos.above();
            BlockState blockStateAbove = level.getBlockState(blockPosAbove);
            toggleShutterAbove(blockStateAbove, blockPosAbove, level);
        }
            toggleShutter(blockState, blockPos, level);
    }

    private void toggleShutterBelow(BlockState blockState, BlockPos blockPos, Level level){
        if(blockState.getValue(HAS_BOTTOM)){
            BlockPos blockPosBelow = blockPos.below();
            BlockState blockStateBelow = level.getBlockState(blockPosBelow);
            toggleShutterBelow(blockStateBelow, blockPosBelow, level);
        }
            toggleShutter(blockState, blockPos, level);
    }
    private Neighbour linkToNeighbourShutter(BlockState blockState, BlockPos blockPos, Level level){
        Direction facing = blockState.getValue(FACING);
        Neighbour connectedBlockDir = blockState.getValue(CONNECTED_BLOCK);
        BlockPos connectedBlockPos = getConnectedShutter(blockPos, blockState);
        BlockState connectedBlockState = level.getBlockState(connectedBlockPos);;
        // TODO Be smarter and remove the double code and only change the Neighbour Parameter depending on connected Block
        if(connectedBlockDir == Neighbour.LEFT){
            // Get the next Shutter bundel
            facing = facing.getOpposite(); //TODO ONLY DIFFERENTZ IN CODE EXCEPT THE OPPOSITE NEIGHBOUR PARAMETER AND RETURN VALUE
            BlockPos neighbourPos = getNextPosition(getNextPosition(blockPos, facing),facing);
            BlockState neighbourState = level.getBlockState(neighbourPos);
            if(neighbourState.getBlock() == blockState.getBlock()) {
                if (neighbourState.getValue(NEIGHBOUR) == Neighbour.NULL && !neighbourState.getValue(ACTIVE)) {
                    // Update Neighbour Shutter
                    level.setBlockAndUpdate(neighbourPos, neighbourState.setValue(NEIGHBOUR, Neighbour.RIGHT));
                    neighbourPos = getConnectedShutter(neighbourPos, neighbourState);
                    neighbourState = level.getBlockState(neighbourPos);
                    level.setBlockAndUpdate(neighbourPos, neighbourState.setValue(NEIGHBOUR, Neighbour.RIGHT));
                    return Neighbour.LEFT; // Return Neighbour to new Block
                }
            }
        } else if (connectedBlockDir == Neighbour.RIGHT) {
            // Get the next Shutter bundel
            BlockPos neighbourPos = getNextPosition(getNextPosition(blockPos, facing),facing);
            BlockState neighbourState = level.getBlockState(neighbourPos);
            if(neighbourState.getBlock() == blockState.getBlock()) {
                if (neighbourState.getValue(NEIGHBOUR) == Neighbour.NULL && !neighbourState.getValue(ACTIVE)) {
                    // Update Neighbour Shutter
                    level.setBlockAndUpdate(neighbourPos, neighbourState.setValue(NEIGHBOUR, Neighbour.LEFT));
                    neighbourPos = getConnectedShutter(neighbourPos, neighbourState);
                    neighbourState = level.getBlockState(neighbourPos);
                    level.setBlockAndUpdate(neighbourPos, neighbourState.setValue(NEIGHBOUR, Neighbour.LEFT));
                    return Neighbour.RIGHT; // Return Neighbour to new Block
                }
            }
        }
        return Neighbour.NULL;
    }
    private BlockPos getNeighbourShutterPos(BlockState blockState, BlockPos blockPos){
        Neighbour neighbour = blockState.getValue(NEIGHBOUR);
        Direction facing = blockState.getValue(FACING);
        if(neighbour != Neighbour.NULL){
            if(neighbour == Neighbour.LEFT){
                facing = facing.getOpposite();
            }
            BlockPos neighbourShutterPos = getNextPosition(blockPos, facing);
            if(blockState.getValue(NEIGHBOUR) == blockState.getValue(CONNECTED_BLOCK)) // go to another next block to skip Connected Block
                neighbourShutterPos = getNextPosition(neighbourShutterPos, facing);

            return neighbourShutterPos;
        }
        return null;
    }
    private void toggleNeighbourShutter(BlockState blockState, BlockPos blockPos, Level level){
        BlockPos neighbourShutterPos = getNeighbourShutterPos(blockState, blockPos);
        if(neighbourShutterPos != null) {
            BlockState neighbourShutterState = level.getBlockState(neighbourShutterPos);
            toggleShutterRow(neighbourShutterState, neighbourShutterPos, level);
        }
    }
    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player p_49855_) {
        BlockPos toDelete = getConnectedShutter(blockPos, blockState);
        Block air = Blocks.AIR;
        level.setBlock(toDelete, air.defaultBlockState(), 3);
        //level.destroyBlock(toDelete, false);

        // remove Neighbourshutter link in neighbour
        if(blockState.getValue(NEIGHBOUR) != Neighbour.NULL){
            BlockPos neighborShutterPos = getNeighbourShutterPos(blockState, blockPos);
            if(neighborShutterPos != null){
                BlockState neighbourShutterState = level.getBlockState(neighborShutterPos);
                level.setBlockAndUpdate(neighborShutterPos, neighbourShutterState.setValue(NEIGHBOUR, Neighbour.NULL));
                neighborShutterPos = getConnectedShutter(neighborShutterPos, neighbourShutterState);
                level.setBlockAndUpdate(neighborShutterPos, level.getBlockState(neighborShutterPos).setValue(NEIGHBOUR, Neighbour.NULL));
            }
        }

        // unlink Top / Bottom Block
        if(blockState.getValue(HAS_TOP)){
            BlockPos blockAbovePos = blockPos.above();
            BlockState blockAboveState = level.getBlockState(blockAbovePos);
            level.setBlockAndUpdate(blockAbovePos, blockAboveState.setValue(HAS_BOTTOM, false));
        }
        if(blockState.getValue(HAS_BOTTOM)){
            BlockPos blockBelowPos = blockPos.below();
            BlockState blockBelowState = level.getBlockState(blockBelowPos);
            level.setBlockAndUpdate(blockBelowPos, blockBelowState.setValue(HAS_TOP, false));
        }
        super.playerWillDestroy(level, blockPos, blockState, p_49855_);
    }
    private BlockPos getConnectedShutter(BlockPos blockPos, BlockState blockState){
        return getConnectedShutter(blockPos, blockState.getValue(CONNECTED_BLOCK), blockState.getValue(FACING));
    }
    private BlockPos getConnectedShutter(BlockPos blockPos, Neighbour neighbour, Direction facing){
        if(neighbour == Neighbour.LEFT)
            facing = facing.getOpposite();

        return getNextPosition(blockPos, facing);
    }

    private BlockPos getNextPosition(BlockPos blockPos, Direction facing){
        switch (facing){
            default:
            case NORTH:
                return blockPos.west();
            case EAST:
                return blockPos.north();
            case SOUTH:
                return blockPos.east();
            case WEST:
                return blockPos.south();
        }
    }

    private boolean hasBottomShutterAndLink(BlockState blockState, BlockPos blockPos, Level level) {
        BlockPos blockBelowPos = blockPos.below();
        BlockState blockBelowState = level.getBlockState(blockBelowPos);
        if(blockBelowState.getBlock() == blockState.getBlock()){
            if(blockBelowState.getValue(CONNECTED_BLOCK) == blockState.getValue(CONNECTED_BLOCK)){
                level.setBlockAndUpdate(blockBelowPos, blockBelowState.setValue(HAS_TOP, true));
                blockBelowPos = getConnectedShutter(blockBelowPos, blockBelowState);
                blockBelowState = level.getBlockState(blockBelowPos);
                level.setBlockAndUpdate(blockBelowPos, blockBelowState.setValue(HAS_TOP, true));
                return true;
            }
        }
        return false;
    }
    private boolean hasTopShutterAndLink(BlockState blockState, BlockPos blockPos, Level level){
        BlockPos blockAbovePos = blockPos.above();
        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        if(blockAboveState.getBlock() == blockState.getBlock()){
            if(blockAboveState.getValue(CONNECTED_BLOCK) == blockState.getValue(CONNECTED_BLOCK)) {
                level.setBlockAndUpdate(blockAbovePos, blockAboveState.setValue(HAS_BOTTOM, true));
                blockAbovePos = getConnectedShutter(blockAbovePos, blockAboveState);
                blockAboveState = level.getBlockState(blockAbovePos);
                level.setBlockAndUpdate(blockAbovePos, blockAboveState.setValue(HAS_BOTTOM, true));
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockState rotate(@NotNull BlockState p_48722_, Rotation rotation) {
        return p_48722_.setValue(FACING, rotation.rotate(p_48722_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_48719_, Mirror mirror) {
        return p_48719_.rotate(mirror.getRotation(p_48719_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, ACTIVE, CONNECTED_BLOCK, NEIGHBOUR, HAS_TOP, HAS_BOTTOM);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public enum Neighbour implements StringRepresentable{
        LEFT("left"), RIGHT("right"), NULL("null");

        private final String name;

        Neighbour(final String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
