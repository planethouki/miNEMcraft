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

import io.nem.sdk.model.transaction.IdGenerator;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

/**
 * The namespace id structure describes namespace id
 *
 * @since 1.0
 */
public class NamespaceId {
    private final BigInteger id;
    private final Optional<String> fullName;

    /**
     * Create NamespaceId from namespace string name (ex: nem or domain.subdom.subdome)
     *
     * @param id
     */
    public NamespaceId(String id) {
        this.id = IdGenerator.generateNamespaceId(id);
        this.fullName = Optional.of(id);
    }

    /**
     * Create NamespaceId from biginteger id
     *
     * @param id
     */
    public NamespaceId(BigInteger id) {
        this.id = id;
        this.fullName = Optional.empty();
    }

    /**
     * Returns namespace biginteger id
     *
     * @return namespace biginteger id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Returns optional namespace full name, with subnamespaces if it's the case.
     *
     * @return namespace full name
     */
    public Optional<String> getFullName() {
        return fullName;
    }

    /**
     * Compares namespaceIds for equality.
     *
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamespaceId)) return false;
        NamespaceId namespaceId1 = (NamespaceId) o;
        return Objects.equals(id, namespaceId1.id);
    }
}
