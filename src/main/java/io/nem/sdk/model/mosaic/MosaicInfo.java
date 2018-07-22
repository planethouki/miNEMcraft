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

package io.nem.sdk.model.mosaic;

import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.namespace.NamespaceId;

import java.math.BigInteger;

/**
 * The mosaic info structure contains its properties, the owner and the namespace to which it belongs to.
 *
 * @since 1.0
 */
public class MosaicInfo {
    private final boolean active;
    private final Integer index;
    private final String metaId;
    private final NamespaceId namespaceId;
    private final MosaicId mosaicId;
    private final BigInteger supply;
    private final BigInteger height;
    private final PublicAccount owner;
    private final MosaicProperties properties;
    //private final any levy;

    public MosaicInfo(boolean active, Integer index, String metaId, NamespaceId namespaceId, MosaicId mosaicId, BigInteger supply, BigInteger height, PublicAccount owner, MosaicProperties properties) {
        this.active = active;
        this.index = index;
        this.metaId = metaId;
        this.namespaceId = namespaceId;
        this.mosaicId = mosaicId;
        this.supply = supply;
        this.height = height;
        this.owner = owner;
        this.properties = properties;
    }

    /**
     * Returns true if the mosaic is active
     *
     * @return if the mosaic is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns true if the mosaic is expired
     *
     * @return if the mosaic is expired
     */
    public boolean isExpired() {
        return !active;
    }

    /**
     * @return index
     */
    public Integer getIndex() {
        return index;
    }

    public String getMetaId() {
        return metaId;
    }

    /**
     * Returns the namespace id it belongs to
     *
     * @return namespace it belongs to
     */
    public NamespaceId getNamespaceId() {
        return namespaceId;
    }

    /**
     * Returns the mosaic id
     *
     * @return mosaic id
     */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    /**
     * Returns the total mosaic supply
     *
     * @return total mosaic supply
     */
    public BigInteger getSupply() {
        return supply;
    }

    /**
     * Returns the block height it was created
     *
     * @return height it was created
     */
    public BigInteger getHeight() {
        return height;
    }

    /**
     * Returns the mosaic account owner
     *
     * @return mosaic account owner
     */
    public PublicAccount getOwner() {
        return owner;
    }

    /**
     * Returns true if the supply is mutable
     *
     * @return if supply is mutable
     */
    public boolean isSupplyMutable() {
        return properties.isSupplyMutable();
    }

    /**
     * Returns tue if the mosaic is transferable between non-owner accounts
     *
     * @return if the mosaic is transferable between non-owner accounts
     */
    public boolean isTransferable() {
        return properties.isTransferable();
    }

    /**
     * Returns if the mosaic levy is mutable
     *
     * @return if the mosaic levy is mutable
     */
    public boolean isLevyMutable() {
        return properties.isLevyMutable();
    }

    /**
     * Return the number of blocks from height it will be active
     *
     * @return the number of blocks from height it will be active
     */
    public BigInteger getDuration() {
        return properties.getDuration();
    }

    /**
     * Returns the mosaic divisibility
     *
     * @return mosaic divisibility
     */
    public int getDivisibility() {
        return properties.getDivisibility();
    }
}

