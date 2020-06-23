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
 ** This class represents a block.
 **/

import java.nio.ByteBuffer;
import java.lang.StringBuffer;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Block {
   public final int BLOCK_SIZE = 1024;
   
   ByteBuffer block;
    
   /**
    **   Constructor method.
    **
    **   @param block The block byte dump.
    **   @param debug Whether debug mode is active or not.
    **/
   public Block(ByteBuffer block, boolean debug) {
      this.block = block;
   }
      
   /**
    **   Gets one byte, increments the pointer.
    **
    **   @return A byte.
    **/
   public byte get() {
      return block.get();
   }
   /**
    **   Gets one byte from a given index.
    **
    **   @param index The index.
    **   @return A byte.
    **/
   public byte get(int index) {
      return block.get(index);
   }
     
   /**
    **   Gets two bytes from a given index.
    **
    **   @param index The index.
    **   @return A short.
    **/
   public short getShort(int index) {
      return block.getShort(index);
   }
   
   /**
    **   Gets four bytes from a given index.
    **
    **   @param index The index.
    **   @return An int.
    **/
   public int getInt(int index) {
      return block.getInt(index);
   }
       
   /**
    **   Prints a formatted hex dump of the block's contents.
    **/
   public void hexDump() {
      StringBuffer hexDump;
      StringBuffer ASCIIVals;
      byte val;
      while (block.hasRemaining()) {
         hexDump = new StringBuffer();
         ASCIIVals = new StringBuffer();
         for (int i = 0; i < 8; i++) {
            val = block.get();
            hexDump.append(String.format("%02X ", val));
            ASCIIVals.append(((val > 31 && val < 126) ? (char)val : ".") + " ");
         }
         hexDump.append("| ");ASCIIVals.append("| ");
         for (int i = 0; i < 8; i++) {
            val = block.get();
            hexDump.append(String.format("%02X ", val));
            ASCIIVals.append(((val > 31 && val < 126) ? (char)val : ".") + " ");
         }
         hexDump.append("| ");ASCIIVals.append("| ");
         System.out.println(hexDump.toString() + " | " + ASCIIVals.toString());
      }
   }
}
