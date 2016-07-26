/*
 * Copyright 2015 Lutz Fischer <lfischer at staffmail.ed.ac.uk>.
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
package org.rappsilber.utils;

/**
 *
 * @author lfischer
 */
public class UpdatableChar  implements java.io.Serializable, Comparable<UpdatableChar>  {
    public char value;

    public int compareTo(UpdatableChar o) {
        return this.value - o.value;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof UpdatableChar) {
            return value == ((UpdatableChar)obj).value;
        }
        return false;
    }    

    public String toString() {
        char buf[] = {value};
        return String.valueOf(buf);
    }    
    
    /**
     * Returns a hash code for this <code>Character</code>.
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        return (int)value;
    }

    
}
