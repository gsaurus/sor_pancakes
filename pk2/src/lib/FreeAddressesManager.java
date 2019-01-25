/*
 * Copyright 2018 gil.costa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gil.costa
 */
public class FreeAddressesManager {
    
    public static final class FreeChunk {
        public long address;
        public long length;

        public FreeChunk(long address, long length) {
            this.address = address;
            this.length = length;
        }
    }
    
    private static List<FreeChunk> freeChunks = new ArrayList<FreeChunk>(10);
    
    public static void addChunk(long address, long length){
        long addedChunkFinalAddress = address + length;
        // Merge with existing chunks if possible
        for (FreeChunk chunk : freeChunks){
            long chunckFinalAddress = chunk.address + chunk.length;
            if (address >= chunk.address && address <= chunckFinalAddress){
                // Add tail
                long finalAddress = Math.max(chunckFinalAddress, addedChunkFinalAddress);
                chunk.length = finalAddress - chunk.address;
                return;
            }else if (address <= chunk.address && addedChunkFinalAddress >= chunk.address){
                // Add head
                long finalAddress = Math.max(chunckFinalAddress, addedChunkFinalAddress);
                chunk.address = address;                
                chunk.length = finalAddress - chunk.address;
                return;
            }
        }
        // Merge not possible, add new chunk
        freeChunks.add(new FreeChunk(address, length));
    }
    
    public static void consumeChunk(long address, long length){
        long consummedChunkFinalAddress = address + length;
        // Consume existing chunks crossing the given chunk
        List<FreeChunk> addedChunks = new ArrayList<FreeChunk>();
        List<FreeChunk> removedChunks = new ArrayList<FreeChunk>();
        for (FreeChunk chunk : freeChunks){
            long chunckFinalAddress = chunk.address + chunk.length;
            if (address <= chunk.address && consummedChunkFinalAddress > chunk.address && consummedChunkFinalAddress < chunckFinalAddress){
                // Shrink left
                chunk.address = consummedChunkFinalAddress;
            }else if (address > chunk.address && address < chunckFinalAddress && consummedChunkFinalAddress >= chunckFinalAddress){
                // Shrink right
                chunk.length = address - chunk.address;
            }else if (address <= chunk.address && consummedChunkFinalAddress >= chunckFinalAddress){
                // Consume entire chunk
                removedChunks.add(chunk);
            }else if (address > chunk.address && consummedChunkFinalAddress < chunckFinalAddress){
                // Split in two chuncks                
                // Chunk one:
                FreeChunk newChunk = new FreeChunk(chunk.address, address - chunk.address);
                addedChunks.add(newChunk);
                // Chunk two:
                chunk.address = consummedChunkFinalAddress;
                chunk.length = chunckFinalAddress - consummedChunkFinalAddress;
            }            
        }
        // Remove all removed chunks
        freeChunks.removeAll(removedChunks);
        // Add all added chunks (splits)
        freeChunks.addAll(addedChunks);
    }
    
}
