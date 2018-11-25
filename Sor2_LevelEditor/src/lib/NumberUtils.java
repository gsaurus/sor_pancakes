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

/**
 *
 * @author gil.costa
 */
public final class NumberUtils {
    
    public static final Formatter decimalFormatter = new DecimalFormatter();
    public static final Formatter hexadecimalFormatter = new HexadecimalFormatter();
    public static final Formatter binaryFormatter = new BinaryFormatter();
    
    static String asHexString(long number){
        return Long.toHexString(number);
    }
    static String asBinaryString(long number){
        return Long.toBinaryString(number);
    }
    static String asDecimalString(long number){
        return Long.toString(number);
    }
    static long asHexNumber(String text){
        return Long.parseLong(text, 16);
    }
    static long asBinaryNumber(String text){
        return Long.parseLong(text, 2);
    }
    static long asDecimalNumber(String text){
        return Long.parseLong(text);
    }
    
    
    
    static String asHexString(int number){
        return Integer.toHexString(number);
    }
    static String asBinaryString(int number){
        return Integer.toBinaryString(number);
    }
    static String asDecimalString(int number){
        return Integer.toString(number);
    }
    
    static String asHexString(short number){
        return Integer.toHexString(number & 0xffff);
    }
    static String asBinaryString(short number){
        return Integer.toBinaryString(number & 0xffff);
    }
    static String asDecimalString(short number){
        return Short.toString(number);
    }
    
    static String asHexString(byte number){
        return Integer.toHexString(number & 0xff);
    }
    static String asBinaryString(byte number){
        return Integer.toBinaryString(number & 0xff);
    }
    static String asDecimalString(byte number){
        return Byte.toString(number);
    }
    
    
    public interface Formatter {
        long toNumber(String text);
        String toString(long number);
        String toString(int number);
        String toString(short number);
        String toString(byte number);
    }
    
    public static class HexadecimalFormatter implements Formatter{
        public HexadecimalFormatter(){}
        @Override
        public long toNumber(String text){
            return asHexNumber(text);
        }
        @Override
        public String toString(long number){
            return asHexString(number);
        }
        @Override
        public String toString(int number){
            return asHexString(number);
        }
        @Override
        public String toString(short number){
            return asHexString(number);
        }
        @Override
        public String toString(byte number){
            return asHexString(number);
        }
    }
    
    public static class BinaryFormatter implements Formatter{
        public BinaryFormatter(){}
        @Override
        public long toNumber(String text){
            return asBinaryNumber(text);
        }
        @Override
        public String toString(long number){
            return asBinaryString(number);
        }
        @Override
        public String toString(int number){
            return asBinaryString(number);
        }
        @Override
        public String toString(short number){
            return asBinaryString(number);
        }
        @Override
        public String toString(byte number){
            return asBinaryString(number);
        }
    }
    
    public static class DecimalFormatter implements Formatter{
        public DecimalFormatter(){}
        @Override
        public long toNumber(String text){
            return asDecimalNumber(text);
        }
        @Override
        public String toString(long number){
            return asDecimalString(number);
        }
        @Override
        public String toString(int number){
            return asDecimalString(number);
        }
        @Override
        public String toString(short number){
            return asDecimalString(number);
        }
        @Override
        public String toString(byte number){
            return asDecimalString(number);
        }
    }
}

