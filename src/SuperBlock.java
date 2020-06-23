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
 ** This class represents a superblock.
 **/

import java.nio.ByteBuffer;
import java.lang.StringBuffer;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
final public class SuperBlock extends Block {       
   /**
    **   Constructor method.
    **
    **   @param superBlock The block byte dump.
    **   @param debug Whether debug mode is active or not.
    **/        
   public SuperBlock(ByteBuffer superBlock, boolean debug) {
      super(superBlock, false);
      
      if (debug) {
         System.out.println("-----Values read from superblock-----");
         System.out.println("Number of inodes: " + this.getNumofInodes());
         System.out.println("Number of blocks: " + this.getNumofBlocks());
         System.out.println("Number of blocks per group: " + this.getBlocksPerGroup());
         System.out.println("Number of inodes per group: " + this.getInodesPerGroup());
         System.out.println("Inode size: " + this.getInodeSize() + " bytes");
         System.out.println("Volume label: " + this.getVolumeLabel());
         System.out.println("Size: " + this.getNumofBlocks() * BLOCK_SIZE + " bytes");
         System.out.println("Number of block groups: " + this.getNumofBlockGroups());
         System.out.println("-------------------------------------");
      }
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of inodes.
	 */
   public int getNumofInodes() {
      return block.getInt(0);
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of blocks.
	 */
   public int getNumofBlocks() {
      return block.getInt(4);
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of blocks per block group.
	 */
   public int getBlocksPerGroup() {
      return block.getInt(32);
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of inodes per block group.
	 */
   public int getInodesPerGroup() {
      return block.getInt(40);
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The size of inodes in bytes.
	 */
   public int getInodeSize() {
      return block.getInt(88);
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The label of the volume.
	 */
   public String getVolumeLabel() {
      StringBuffer volumeLabel = new StringBuffer();
      for (int i = 120; i < (120 + 16); i++)
         volumeLabel.append((char)block.get(i));
      return volumeLabel.toString();
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of of block groups.
	 */
   public int getNumofBlockGroups() {
      return (int)Math.ceil(getNumofBlocks()/getBlocksPerGroup());
   }
}
