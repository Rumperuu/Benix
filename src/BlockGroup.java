/******************************************************************************
 *                             Benix 0.8                                      *
 *                  Copyright © 2015 Ben Goldsworthy (rumps)                  *
 *                                                                            *
 * A program to simulate an OS and read a file store.                         *
 *                                                                            *
 * This file is part of Benix.                                                *
 *                                                                            *
 * Benix is free software: you can redistribute it and/or modify              *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * Benix is distributed in the hope that it will be useful,                   *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License          *
 * along with Benix.  If not, see <http://www.gnu.org/licenses/>.             *
 ******************************************************************************/

/**
 ** This class represents a block group.
 **/
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class BlockGroup {
   public final int BLOCK_SIZE = 1024;
   public final int INODE_SIZE = 128;
   private final int INODES_PER_BLOCK = BLOCK_SIZE/INODE_SIZE; // 8
   private final int INODE_TABLE_SIZE = 218; // inodes per group/inodes per block
   
   ByteBuffer blockGroup;
   private SuperBlock superBlock;
   private GroupDesc groupDesc;
   private Block inodeTable;
   private Inode rootInode;
   private Directory rootDir;
    
   /**
    **   Constructor method.
    **
    **   @param block The blockgroup byte dump.
    **   @param debug Whether debug mode is active or not.
    **/
   public BlockGroup(ByteBuffer block, boolean debug) {
      this.blockGroup = block;
      
      // could probably get rid of this, but for now gets blocks holding
      // information on rest of volume
      this.superBlock = new SuperBlock(this.getBlock(1), debug);
      this.groupDesc = new GroupDesc(this.getBlock(2), debug);
      
      // gets the first block of the inode table, and the inode for root
      this.inodeTable = new Block(this.getBlock(this.groupDesc.getInodeTablePointer()), debug);
      this.rootInode = getInode(2, debug);   
      
      // gets the root directory
      int numofRootBlocks = (int)Math.ceil(this.rootInode.getFileSizeLower()/BLOCK_SIZE);
      Block[] rootDirBlocks = new Block[numofRootBlocks];
      for(int i = 0; i < numofRootBlocks; i++) 
         rootDirBlocks[i] = new Block(this.getBlock(rootInode.getBlockPointer(0)), false);
      this.rootDir = new Directory(rootDirBlocks, debug);
   }
   
	/**
	 *	Accessor function.
    *
    * @param id The index of the block.
	 * @return A Block.
	 */
   public ByteBuffer getBlock(int id) {
      byte block[] = new byte[BLOCK_SIZE];
      try {
         this.blockGroup.position(BLOCK_SIZE * --id);
         this.blockGroup.get(block);
         
         ByteBuffer theBlock = ByteBuffer.allocate(BLOCK_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        
         theBlock.put(block);
         theBlock.flip();  
         
         return theBlock;         
      } catch (BufferOverflowException ex) {
         System.out.println("BOFE");
      } catch (BufferUnderflowException ex) {
         System.out.println("BOFE");
      } catch (ReadOnlyBufferException ex) {
         System.out.println("ROBE");
      }
      return null;
   } 
   
	/**
	 *	Accessor function.
    *
    * @param id The index of the inode.
    * @param debug Whether debug mode is active or not.
	 * @return An Inode.
	 */
   public Inode getInode(int id, boolean debug) {
      int offset;
      Block inodeTableBlock;
      // gets the correct block for the inode
      if(id <= INODES_PER_BLOCK) {
         offset = INODE_SIZE * --id;
         inodeTableBlock = inodeTable;
      } else {
         int blockOffset = (int)Math.floor(id/INODES_PER_BLOCK);
         id = id - (8 * blockOffset);
         offset = INODE_SIZE * --id;
         inodeTableBlock = new Block(this.getBlock(this.groupDesc.getInodeTablePointer() + blockOffset), false);
      }
      
      // gets the data blocks from the inode
      int[] blocks = new int[12];
      for (int i = 0; i < 12; i++) blocks[i] = inodeTableBlock.getInt(offset + (40 + (i * 4)));
                  
      return new Inode( inodeTableBlock.getShort(offset+0),
                        inodeTableBlock.getShort(offset+2),
                        inodeTableBlock.getInt(offset+4),
                        inodeTableBlock.getInt(offset+8),
                        inodeTableBlock.getInt(offset+12),
                        inodeTableBlock.getInt(offset+16),
                        inodeTableBlock.getInt(offset+20),
                        inodeTableBlock.getShort(offset+24),
                        inodeTableBlock.getShort(offset+26),
                        blocks,
                        inodeTableBlock.getInt(offset+88),
                        inodeTableBlock.getInt(offset+92),
                        inodeTableBlock.getInt(offset+96),
                        inodeTableBlock.getInt(offset+108), debug);
   }
   
   /**
    * Prints the directory listing.
    */
   public void listDir() {
      this.rootDir.listDir();
   }
   
	/**
	 *	Accessor function.
    *
    * @param fileName The filename.
	 * @return The file's inode pointer.
	 */
   public int getFileInodePointer(String fileName) {
      int id = this.rootDir.getFileNum(fileName);
      if(id != -1)
         return this.rootDir.getInodePointer(id);
      else return -1;
   }
   
	/**
	 *	Accessor function.
    *
    * @param fileName The filename.
	 * @return The file's length.
	 */
   public int getFileLength(String fileName) {
      return this.rootDir.getLength(this.rootDir.getFileNum(fileName));
   }
}
