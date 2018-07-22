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

import io.nem.core.crypto.Signer;
import io.nem.sdk.model.account.Account;
import org.bouncycastle.util.encoders.Hex;

/**
 * The cosignature transaction is used to sign an aggregate transactions with missing cosignatures.
 *
 * @since 1.0
 */
public class CosignatureTransaction {
    private final AggregateTransaction transactionToCosign;

    /**
     * Constructor
     *
     * @param transactionToCosign Aggregate transaction that will be cosigned.
     */
    public CosignatureTransaction(AggregateTransaction transactionToCosign) {
        if (!transactionToCosign.getTransactionInfo().isPresent() || !transactionToCosign.getTransactionInfo().get().getHash().isPresent()) {
            throw new IllegalArgumentException("Transaction to cosign should be announced before being able to cosign it");
        }
        this.transactionToCosign = transactionToCosign;
    }

    /**
     * Create a cosignature transaction.
     *
     * @param transactionToCosign Aggregate transaction that will be cosigned.
     * @return {@link CosignatureTransaction}
     */
    public static CosignatureTransaction create(AggregateTransaction transactionToCosign) {
        return new CosignatureTransaction(transactionToCosign);
    }

    /**
     * Returns transaction to cosign.
     *
     * @return {@link AggregateTransaction}
     */
    public AggregateTransaction getTransactionToCosign() {
        return transactionToCosign;
    }

    /**
     * Serialize and sign transaction creating a new SignedTransaction.
     *
     * @param account Account
     * @return {@link CosignatureSignedTransaction}
     */
    public CosignatureSignedTransaction signWith(Account account) {
        Signer signer = new Signer(account.getKeyPair());
        byte[] bytes = Hex.decode(this.transactionToCosign.getTransactionInfo().get().getHash().get());
        byte[] signatureBytes = signer.sign(bytes).getBytes();
        return new CosignatureSignedTransaction(this.transactionToCosign.getTransactionInfo().get().getHash().get(), Hex.toHexString(signatureBytes), account.getPublicKey());
    }
}
