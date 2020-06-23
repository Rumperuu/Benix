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
 ** This class represents a volume.
 **/

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.BufferOverflowException;
import java.nio.ReadOnlyBufferException;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Volume {
   public final int BLOCK_SIZE = 1024;
   public final int INODE_SIZE = 128;
   
   private RandomAccessFile file;
   private SuperBlock superBlock;
   private GroupDesc groupDesc;
   private Block inodeTable;
   private Inode rootInode;
   private int numofBlockGroups;
   BlockGroup[] blockGroups = new BlockGroup[numofBlockGroups];
       
   /**
    **   Constructor method.
    **
    **   @param fileName The name of the ext2 dump.
    **/    
   public Volume(String fileName) {
      try {
         file = new RandomAccessFile(fileName, "r");
         
         superBlock = new SuperBlock(getBlock(1), false);
         groupDesc = new GroupDesc(getBlock(2), false);
         
         numofBlockGroups = superBlock.getNumofBlocks()/superBlock.getBlocksPerGroup();
         blockGroups = new BlockGroup[numofBlockGroups];
         for (int i = 0; i < numofBlockGroups; i++) {
            byte blockGroup[] = new byte[BLOCK_SIZE * superBlock.getBlocksPerGroup()];
            try {
               file.seek(BLOCK_SIZE);
               file.read(blockGroup);
               
               ByteBuffer blockGroupBuf = ByteBuffer.allocate(BLOCK_SIZE * superBlock.getBlocksPerGroup()).order(ByteOrder.LITTLE_ENDIAN);
           
               blockGroupBuf.put(blockGroup);
               blockGroupBuf.flip();  
               
               blockGroups[i] = new BlockGroup(blockGroupBuf, false);
            } catch (IOException ex) {
               System.out.println("ACK");         
            } catch (BufferOverflowException ex) {
               System.out.println("BOFE");
            } catch (ReadOnlyBufferException ex) {
               System.out.println("ROBE");
            }
         }
      } catch (FileNotFoundException ex) {
         System.out.println("AWSHIT");
      }
   }
   
	/**
	 *	Accessor function.
    *
    * @param id The index of the block.
	 * @return The block's byte dump.
	 */
   private ByteBuffer getBlock(int id) {
      byte block[] = new byte[BLOCK_SIZE];
      try {
         file.seek(BLOCK_SIZE * id);
         file.read(block);
         
         ByteBuffer blockBuf = ByteBuffer.allocate(BLOCK_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        
         blockBuf.put(block);
         blockBuf.flip();  
         
         return blockBuf;
      } catch (IOException ex) {
         System.out.println("ACK");         
      } catch (BufferOverflowException ex) {
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
	 * @return The Inode.
	 */
   private Inode getInode(int id, boolean debug) {
      int blockgroup = (int)Math.floor((id - 1)/superBlock.getInodesPerGroup());
      // offset by 1 inode as we're after inode #2
      int offset = INODE_SIZE * --id;
      int[] blocks = new int[12];
      for (int i = 0; i < 12; i++) blocks[i] = inodeTable.getInt(offset + (40 + (i * 4)));
                  
      return new Inode( inodeTable.getShort(offset+0),
                        inodeTable.getShort(offset+2),
                        inodeTable.getInt(offset+4),
                        inodeTable.getInt(offset+8),
                        inodeTable.getInt(offset+12),
                        inodeTable.getInt(offset+16),
                        inodeTable.getInt(offset+20),
                        inodeTable.getShort(offset+24),
                        inodeTable.getShort(offset+26),
                        blocks,
                        inodeTable.getInt(offset+88),
                        inodeTable.getInt(offset+92),
                        inodeTable.getInt(offset+96),
                        inodeTable.getInt(offset+108), debug);
   }

	/**
	 *	Accessor function.
    *
    * @param fileName The name of the file.
	 * @return The array of Blocks the file covers.
	 */
   public Block[] getFile(String fileName) {
      int iP = this.blockGroups[0].getFileInodePointer(fileName);
      Inode inode = this.blockGroups[this.getInodeBG(iP)].getInode(iP, false);
      int blocks = 0;
      for(int i = 0; i < 12; i++)
         if (inode.getBlockPointer(i) != 0)
            blocks++;
      
      Block[] contentBlocks = new Block[blocks];
      for(int i = 0; i < blocks; i++)
         contentBlocks[i] = new Block(this.getBlock(inode.getBlockPointer(i)), false);
      
      return contentBlocks;
   }
   
	/**
	 *	Accessor function.
    *
    * @param iP The inode pointer.
	 * @return The block group containing the inode.
	 */
   private int getInodeBG(int iP) {
      return (int)Math.ceil(iP / this.superBlock.getInodesPerGroup());
   }
   
   /**
    * Prints the directory listing.
    * 
    * @param dirName The directory name.
    */ 
   public void listDir(String dirName) {
      this.blockGroups[0].listDir();
   }
   
   /**
    * Prints the directory listing.
    */
   public void listRoot() {
      this.blockGroups[0].listDir();
   }
}
