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

package io.nem.sdk.model.transaction;

import com.google.flatbuffers.FlatBufferBuilder;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicSupplyType;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.Optional;

/**
 * In case a mosaic has the flag 'supplyMutable' set to true, the creator of the mosaic can change the supply,
 * i.e. increase or decrease the supply.
 *
 * @since 1.0
 */
public class MosaicSupplyChangeTransaction extends Transaction {
    private final MosaicId mosaicId;
    private final MosaicSupplyType mosaicSupplyType;
    private final BigInteger delta;
    private final Schema schema = new MosaicSupplyChangeTransactionSchema();


    public MosaicSupplyChangeTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, MosaicSupplyType mosaicSupplyType, BigInteger delta, String signature, PublicAccount signer, TransactionInfo transactionInfo) {
        this(networkType, version, deadline, fee, mosaicId, mosaicSupplyType, delta, Optional.of(signature), Optional.of(signer), Optional.of(transactionInfo));
    }

    public MosaicSupplyChangeTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, MosaicSupplyType mosaicSupplyType, BigInteger delta) {
        this(networkType, version, deadline, fee, mosaicId, mosaicSupplyType, delta, Optional.empty(), Optional.empty(), Optional.empty());
    }

    private MosaicSupplyChangeTransaction(NetworkType networkType, Integer version, Deadline deadline, BigInteger fee, MosaicId mosaicId, MosaicSupplyType mosaicSupplyType, BigInteger delta, Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
        super(TransactionType.MOSAIC_SUPPLY_CHANGE, networkType, version, deadline, fee, signature, signer, transactionInfo);
        Validate.notNull(mosaicId, "MosaicId must not be null");
        Validate.notNull(mosaicSupplyType, "MosaicSupplyType must not be null");
        Validate.notNull(delta, "Delta must not be null");
        this.mosaicId = mosaicId;
        this.mosaicSupplyType = mosaicSupplyType;
        this.delta = delta;
    }

    /**
     * Create a mosaic supply change transaction object.
     *
     * @param deadline         The deadline to include the transaction.
     * @param mosaicId         The mosaic id.
     * @param mosaicSupplyType The supply type.
     * @param delta            The supply change in units for the mosaic.
     * @param networkType      The network type.
     * @return {@link MosaicSupplyChangeTransaction}
     */
    public static MosaicSupplyChangeTransaction create(Deadline deadline, MosaicId mosaicId, MosaicSupplyType mosaicSupplyType, BigInteger delta, NetworkType networkType) {
        return new MosaicSupplyChangeTransaction(networkType, 2, deadline, BigInteger.valueOf(0), mosaicId, mosaicSupplyType, delta);
    }

    /**
     * Returns mosaic id.
     *
     * @return BigInteger
     */
    public MosaicId getMosaicId() {
        return mosaicId;
    }

    /**
     * Returns mosaic supply type.
     *
     * @return {@link MosaicSupplyType}
     */
    public MosaicSupplyType getMosaicSupplyType() {
        return mosaicSupplyType;
    }

    /**
     * Returns amount of mosaics added or removed.
     *
     * @return BigInteger
     */
    public BigInteger getDelta() {
        return delta;
    }

    byte[] generateBytes() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        BigInteger deadlineBigInt = BigInteger.valueOf(getDeadline().getInstant());
        int[] fee = new int[]{0, 0};
        int version = (int) Long.parseLong(Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);

        // Create Vectors
        int signatureVector = MosaicSupplyChangeTransactionBuffer.createSignatureVector(builder, new byte[64]);
        int signerVector = MosaicSupplyChangeTransactionBuffer.createSignerVector(builder, new byte[32]);
        int deadlineVector = MosaicSupplyChangeTransactionBuffer.createDeadlineVector(builder, UInt64.fromBigInteger(deadlineBigInt));
        int feeVector = MosaicSupplyChangeTransactionBuffer.createFeeVector(builder, fee);
        int mosaicIdVector = MosaicSupplyChangeTransactionBuffer.createMosaicIdVector(builder, UInt64.fromBigInteger(mosaicId.getId()));
        int deltaVector = MosaicSupplyChangeTransactionBuffer.createDeltaVector(builder, UInt64.fromBigInteger(delta));

        int fixSize = 137; // replace by the all numbers sum or add a comment explaining this

        MosaicSupplyChangeTransactionBuffer.startMosaicSupplyChangeTransactionBuffer(builder);
        MosaicSupplyChangeTransactionBuffer.addSize(builder, fixSize);
        MosaicSupplyChangeTransactionBuffer.addSignature(builder, signatureVector);
        MosaicSupplyChangeTransactionBuffer.addSigner(builder, signerVector);
        MosaicSupplyChangeTransactionBuffer.addVersion(builder, version);
        MosaicSupplyChangeTransactionBuffer.addType(builder, getType().getValue());
        MosaicSupplyChangeTransactionBuffer.addFee(builder, feeVector);
        MosaicSupplyChangeTransactionBuffer.addDeadline(builder, deadlineVector);
        MosaicSupplyChangeTransactionBuffer.addMosaicId(builder, mosaicIdVector);
        MosaicSupplyChangeTransactionBuffer.addDirection(builder, mosaicSupplyType.getValue());
        MosaicSupplyChangeTransactionBuffer.addDelta(builder, deltaVector);

        int codedTransaction = MosaicSupplyChangeTransactionBuffer.endMosaicSupplyChangeTransactionBuffer(builder);
        builder.finish(codedTransaction);

        return schema.serialize(builder.sizedByteArray());
    }
}
