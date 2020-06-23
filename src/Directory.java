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
 ** This class represents a directory.
 **/
 
import java.lang.StringBuffer;

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Directory extends Ext2File {
   public final int INODE_FIELD_SIZE = 4, LENGTH_FIELD_SIZE = 2,
                    NAME_LENGTH_FIELD_SIZE = 1, TYPE_INDICATOR_FIELD_SIZE = 1,
                    LENGTH_FIELD_OFFSET = 4, NAME_LENGTH_FIELD_OFFSET = 6, TYPE_INDICATOR_FIELD_OFFSET = 7, NAME_FIELD_OFFSET = 8;
   
   /**
    **   Constructor method.
    **
    **   @param content The array of Blocks containing the file.
    **   @param debug Whether debug mode is active or not.
    **/
   public Directory(Block[] content, boolean debug) {
      super(content, debug);
   }
   
	/**
	 *	Accessor function.
    *
    * @param fileName The name of the file.
	 * @return The index of the file.
	 */
   public int getFileNum(String fileName) {
      for(int i = 0; i < this.getNumofFiles(); i++) {
         if (this.getName(i).equals(fileName)) return i;
      }
      return -1;
   } 
   
	/*
    * I don't remember what this does but it breaks when I try to get rid of it.
    */
   private int getFileOffset(int fileNum) {
      if(fileNum <= 0) return -1;
      short val = 0;
      for(int i = 0; i < fileNum-1; i++) val += this.getBlock(1).getShort(val+4);
      return (val != 1024) ? val : -1;
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The number of files in the directory.
	 */
   public int getNumofFiles() {
      int num;
      boolean end = false;
      for(num = 1; !end && num < BLOCK_SIZE; num++)
         end = (this.getFileOffset(num) != -1) ? false : true;
      return num - 2;
   }
   
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The inode pointer of the file.
	 */
   public int getInodePointer(int fileNum) { 
      return this.getBlock(1).getInt(getFileOffset(fileNum));
   }
    
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The length of the file.
	 */
   public short getLength(int fileNum) { 
      return this.getBlock(1).getShort(getFileOffset(fileNum) + LENGTH_FIELD_OFFSET);
   }
    
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The length of the name of the file.
	 */
   public byte getNameLength(int fileNum) { 
      return this.getBlock(1).get(getFileOffset(fileNum) + NAME_LENGTH_FIELD_OFFSET);
   }
    
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The type character of the file.
	 */
   public char getType(int fileNum) {
      switch(this.getTypeIndicator(fileNum)) {
         case 1: return '-';
         case 2: return 'd';
         default: return ' ';
      }
   }
    
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The type indicator of the file.
	 */
   public byte getTypeIndicator(int fileNum) { 
      return this.getBlock(1).get(getFileOffset(fileNum) + TYPE_INDICATOR_FIELD_OFFSET);
   }
    
	/**
	 *	Accessor function.
    *
    * @param fileNum The index of the file.
	 * @return The name of the file.
	 */
   public String getName(int fileNum) { 
      StringBuffer fileName = new StringBuffer();
      int offset = this.getFileOffset(fileNum) + NAME_FIELD_OFFSET,
          end = offset + this.getNameLength(fileNum);
      for(int i = offset; i < end; i++)
         fileName.append((char)this.getBlock(1).get(i));
      return fileName.toString();
   }
   
   /**
    * Prints the directory listing.
    */
   public void listDir() {
      for(int i = 1; i <= this.getNumofFiles(); i++) {
        System.out.println(this.getType(i) + "\t" + this.getLength(i) + "\t" + this.getNameLength(i) + "\t" + this.getTypeIndicator(i) + "\t" + this.getName(i));
         
      }
   }
}
