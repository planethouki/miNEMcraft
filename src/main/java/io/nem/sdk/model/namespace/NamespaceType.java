/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.model.namespace;

/**
 * Enum containing namespace supply type.
 *
 * @since 1.0
 */
public enum NamespaceType {
    /**
     * Root namespace
     */
    RootNamespace(0),
    /**
     * Sub namespace
     */
    SubNamespace(1);

    private int value;

    NamespaceType(int value) {
        this.value = value;
    }

    public static NamespaceType rawValueOf(int value) {
        switch (value) {
            case 0:
                return NamespaceType.RootNamespace;
            case 1:
                return NamespaceType.SubNamespace;
            default:
                throw new IllegalArgumentException(value + " is not a valid value");
        }
    }

    public int getValue() {
        return value;
    }

}

