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
 ** This class represents a Ext2 file.
 **/

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Ext2File {
   public final int BLOCK_SIZE = 1024;
   
   private Block[] content;
   
   /**
    **   Constructor method.
    **
    **   @param blocks The array of Blocks containing the file.
    **   @param debug Whether debug mode is active or not.
    **/
   public Ext2File(Block[] blocks, boolean debug) {
      this.content = new Block[blocks.length];
      for(int i = 0; i < blocks.length; i++) {
         this.content[i] = blocks[i];
      }
      
      if (debug) {
         System.out.println("-----Values read from file-----");
         for(int i = 0; i < this.content.length; i++)
            this.content[i].hexDump();
         System.out.println("-------------------------------------");
      }
   }
   
	/**
	 *	Accessor function.
    *
    * @param num The Index of the block.
	 * @return The Block.
	 */
   public Block getBlock(int num) {
      return content[--num];
   }
        
   /**
    **   Prints a formatted hex dump of the block's contents.
    **/
   public void hexDump() {
      for(int i = 0; i < this.content.length; i++)
         this.content[i].hexDump();
   }
}
