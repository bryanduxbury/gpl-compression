/*
 * This file is part of Hadoop-Gpl-Compression.
 *
 * Hadoop-Gpl-Compression is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Hadoop-Gpl-Compression is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hadoop-Gpl-Compression.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
 
package com.gpl_compression.lzo;

import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Checksums on compressed block data with header bitmask, Checksum class.
 */
public enum CChecksum {
  F_ADLER32C(0x02, Adler32.class), F_CRC32C(0x200, CRC32.class);
  private final int mask;
  private final Class<? extends Checksum> clazz;
  CChecksum(int mask, Class<? extends Checksum> clazz) {
    this.mask = mask;
    this.clazz = clazz;
  }
  public int getHeaderMask() {
    return mask;
  }
  public Class<? extends Checksum> getChecksumClass() {
    return clazz;
  }
}