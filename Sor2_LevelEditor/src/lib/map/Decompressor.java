/* 
 * Copyright 2017 Gil Costa.
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
package lib.map;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import lib.RandomDataStream;



public class Decompressor {

    public static RandomDataStream run(RandomAccessFile input, long artAddress) throws IOException{
    
        int combyte = 0; // unsigned int
        int d0 = 0;
        int d1 = 0;
        int pos = 0;
        int prev = 0;
        int k = 0;
    
//	RandomAccessFile input  = new RandomAccessFile(new File("art.bin"), "r");
        input.seek(artAddress);
	RandomDataStream output = new RandomDataStream();
	int size = 0;
	size = fgetc(input);
	size = (size^(fgetc(input)<<8));
        
	for(int i = 0 ; i < size ; i++){
            pos = (int)(ftell(input) - artAddress);
            if (pos > size)
                break;
            combyte = fgetc(input);
            if ((combyte&0x80)== 0x80){   // bit 7
                d0=(((combyte&0x60)/0x20)+4);
                d1=((combyte&0x1F)<<8);
                d1=(d1^(fgetc(input)));
                for (int j = 0 ; j < d0 ; j++){
                    fseek (output,-d1,RandomDataStream.Pos.cur);
                    prev = ftell (output);
                    k = fgetc (output);
                    fseek (output,0,RandomDataStream.Pos.end);
                    fputc (k,output);
                }
                combyte = 0x80;
            }
            if ((combyte&0x60)== 0x60){ // bit 7
                d0=combyte&0x1F;
                for (int j = 0; j<d0;j++){
                    prev++;
                    fseek (output,prev,RandomDataStream.Pos.begin);

                    k = fgetc (output);
                    fseek (output,0,RandomDataStream.Pos.end);
                    fputc (k,output);
                }
                combyte = 0x80;
            }

            if ((combyte&0x40)== 0x40){ // bit six
                if ((combyte&0x10)== 0x10){
                    d0=((combyte^0x50)<<8);
                    d0=((d0^(fgetc(input)))+4);
                    k = fgetc(input);
                    for (int j = 0; j<d0;j++){
                        fputc (k,output);
                    }
                    combyte = 0x40;
                }

                else if((combyte&0x10)== 0x00){
                    d0 = combyte^0x40;
                    d0 = d0+4;
                    k = fgetc(input);
                    for (int j = 0; j<d0;j++){
                        fputc (k,output);
                    }
                    combyte = 0x40;
                }
            }
            if ((combyte&0x20)== 0x20){ // bit five
                d0=((combyte^0x20)<<8);
                d0=(d0^(fgetc(input)));
                for (int j = 0; j<d0;j++){
                    k = fgetc(input);
                    fputc (k,output);
                }
                combyte = 0x20;
            }


            if ((combyte&0xE0)== 0x00){
                d0 = combyte;
                //d0=d0+1;
                for (int j = 0; j<d0;j++){
                    k = fgetc(input);
                    fputc (k,output);
                }
            }
	}
	return output;
    }
    
    
      private static int fgetc(RandomAccessFile input) throws IOException {
        return input.read();
    }    

    private static int fgetc(RandomDataStream output) {
        byte res = output.read();
        output.seekWrite(output.getWritePos(), RandomDataStream.Pos.begin);
        return (int)(res);
    }

    private static void fputc(int k, RandomDataStream output) {
        output.write((byte)k);
        output.seekRead(output.getWritePos(), RandomDataStream.Pos.begin);
    }
    
    private static void fseek(RandomDataStream output, int ammount, RandomDataStream.Pos pos) {
        output.seekRead(ammount, pos);
        output.seekWrite(ammount, pos);
    } 
    private static long ftell(RandomAccessFile input) throws IOException {
        return input.getFilePointer();
    }
    private static int ftell(RandomDataStream output) {
        return output.getWritePos();
    }
        
}
