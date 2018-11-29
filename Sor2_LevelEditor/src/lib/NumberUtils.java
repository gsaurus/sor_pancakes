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
    
    public static final Formatter decimalFormatter = new Formatter(10);
    public static final Formatter hexadecimalFormatter = new Formatter(16);
    public static final Formatter binaryFormatter = new Formatter(2);
    
    
    public static class Formatter {
        protected int radix;
        Formatter(int radix){
            this.radix = radix;
        }
        public long toUnsignedNumber(String text){
            return Long.parseUnsignedLong(text, radix);
        }
        public long toNumber(String text){
            return Long.parseLong(text, radix);
        }
        public String toString(long number){
            return Long.toString(number, radix);
        }
        public String toString(int number){
            return Integer.toString(number, radix);
        }
        public String toString(short number){
            return toString((int)number);
        }
        public String toString(byte number){
            return toString((int)number);
        }
        public String toUnsignedString(long number){
            return Long.toUnsignedString(number, radix);
        }
    }
    
}

