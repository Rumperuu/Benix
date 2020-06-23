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
 ** This class runs the OS.
 **/

/**
 **   @author  Ben Goldsworthy (rumps) <me+benix@bengoldsworthy.net>
 **   @version 0.8
 **/
public class Benix {	    
   /**
	 **   Interacts with the Benix API
	 **
	 **   @param args arguments passed in from the command line.
	 **/
   public static void main(String[] args) {
      // mounts the volume
      Volume vol = new Volume("../ext2fs");
      vol.listRoot();
      
      Ext2File file = new Ext2File(vol.getFile("two-cities"), false);
      
      Directory dir = new Directory(vol.getFile("lost+found"), false);
      dir.listDir();
   }
}
