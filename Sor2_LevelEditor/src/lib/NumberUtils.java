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
    
    static String hexString(long number){
        return Long.toHexString(number);
    }
    static String binaryString(long number){
        return Long.toBinaryString(number);
    }
    static String decimalString(long number){
        return Long.toString(number);
    }
    static long hexNumber(String text){
        return Long.parseLong(text, 16);
    }
    static long binaryNumber(String text){
        return Long.parseLong(text, 2);
    }
    static long decimalNumber(String text){
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
    
    
    public interface Formatter {
        long toNumber(String text);
        String toString(long number);
        String toString(int number);
    }
    
    public static class HexadecimalFormatter implements Formatter{
        public HexadecimalFormatter(){}
        @Override
        public long toNumber(String text){
            return hexNumber(text);
        }
        @Override
        public String toString(long number){
            return hexString(number);
        }
        @Override
        public String toString(int number){
            return hexString(number);
        }
    }
    
    public static class BinaryFormatter implements Formatter{
        public BinaryFormatter(){}
        @Override
        public long toNumber(String text){
            return binaryNumber(text);
        }
        @Override
        public String toString(long number){
            return binaryString(number);
        }
        @Override
        public String toString(int number){
            return binaryString(number);
        }
    }
    
    public static class DecimalFormatter implements Formatter{
        public DecimalFormatter(){}
        @Override
        public long toNumber(String text){
            return decimalNumber(text);
        }
        @Override
        public String toString(long number){
            return decimalString(number);
        }
        @Override
        public String toString(int number){
            return decimalString(number);
        }
    }
}

