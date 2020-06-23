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
 ** This class represents a group descriptor block.
 **/

import java.nio.ByteBuffer;
import java.lang.StringBuffer;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
final public class GroupDesc extends Block { 
   /**
    **   Constructor method.
    **
    **   @param groupDesc The block byte dump.
    **   @param debug Whether debug mode is active or not.
    **/
   public GroupDesc(ByteBuffer groupDesc, boolean debug) {
      super(groupDesc, false);
      
      if (debug) {
         System.out.println("-----Values read from group desc-----");
         System.out.println("Inode Table Pointer: " + getInodeTablePointer());
         System.out.println("-------------------------------------");
      }
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The inode table pointer of the block group.
	 */
   public int getInodeTablePointer() {
      return block.getInt(8);
   }
}
