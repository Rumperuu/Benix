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
 ** This class represents an inode.
 **/

import java.util.Date;
/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Inode {
   private short fileMode, uID, gID, hardLinks;
   private int fileSizeLower, indirectPointer, dIndirectPointer,
               tIndirectPointer, fileSizeUpper;
   private Date lastAccessed, creationTime, lastModified, deletionTime;
   private int[] blockPointers = new int[12];
    
   /**
    **   Constructor method.
    **
    **   @param fileMode The file mode.
    **   @param uID The owner user ID.
    **   @param fileSizeLower The lower 32 bits of the file size.
    **   @param lastAccessed The time of last access.
    **   @param creationTime The time of creation.
    **   @param lastModified The time of last modification.
    **   @param deletionTime The time of deletion.
    **   @param gID The owner group ID.
    **   @param hardLinks The number of hard links to the file.
    **   @param blockPointers The twelve block pointers.
    **   @param indirectPointer The indirect block pointer.
    **   @param dIndirectPointer The double indirect block pointer.
    **   @param tIndirectPointer The triple indirect block pointer.
    **   @param fileSizeUpper The upper 32 bits of the file size.
    **   @param debug Whether debug mode is active or not.
    **/
   public Inode(short fileMode, short uID, int fileSizeLower, int lastAccessed, 
                int creationTime, int lastModified, int deletionTime, short gID, 
                short hardLinks, int blockPointers[], int indirectPointer, 
                int dIndirectPointer, int tIndirectPointer, int fileSizeUpper,
                boolean debug) {
      this.fileMode = fileMode;
      this.uID = uID;
      this.fileSizeLower = fileSizeLower;
      this.lastAccessed = new Date((long)lastAccessed * 1000);
      this.creationTime = new Date((long)creationTime * 1000);
      this.lastModified = new Date((long)lastModified * 1000);
      this.deletionTime = (deletionTime != 0) ? new Date((long)deletionTime * 1000) : null;
      this.gID = gID;
      this.hardLinks = hardLinks;
      this.blockPointers = blockPointers;
      this.indirectPointer = indirectPointer;
      this.dIndirectPointer = dIndirectPointer;
      this.tIndirectPointer = tIndirectPointer;
      this.fileSizeUpper = fileSizeUpper;
             
      if (debug) {
         System.out.println("-----Values read from inode-----");
         System.out.println("File Mode: " + this.fileMode);
         System.out.println("Owner User ID: " + this.uID);
         System.out.println("File Size (lower 32-bits): " + this.fileSizeLower);
         System.out.println("File Size (upper 32-bits): " + this.fileSizeUpper);
         System.out.println("Last Accessed Time: " + this.lastAccessed);
         System.out.println("Creation Time: " + this.creationTime);
         System.out.println("Last Modify Time: " + this.lastModified);
         System.out.println("Deletion Time: " + this.deletionTime);
         System.out.println("Owner Group ID: " + this.gID);
         for (int i = 0; i < 12; i++) System.out.println("Block Pointer " + i + ": " + this.blockPointers[i]);
         System.out.println("Indirect Pointer: " + this.indirectPointer);
         System.out.println("Double Indirect Pointer: " + this.dIndirectPointer);
         System.out.println("Triple Indirect Pointer: " + this.tIndirectPointer);
         System.out.println("-------------------------------------");
      }
   }
   
	/**
	 *	Accessor function.
    *
    * @param blockID The index of the block.
	 * @return The block pointer.
	 */
   public int getBlockPointer(int blockID) {
      return this.blockPointers[blockID];
   }
   
	/**
	 *	Accessor function.
    *
	 * @return The lower 32 bits of the filesize.
	 */
   public int getFileSizeLower() {
      return this.fileSizeLower;
   }
}
