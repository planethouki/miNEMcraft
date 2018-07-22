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

package io.nem.sdk.model.account;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The multisig account graph info structure describes the information of all the mutlisig levels an account is involved in.
 *
 * @since 1.0
 */
public class MultisigAccountGraphInfo {
    private final Map<Integer, List<MultisigAccountInfo>> multisigAccounts;

    public MultisigAccountGraphInfo(Map<Integer, List<MultisigAccountInfo>> multisigAccounts) {
        this.multisigAccounts = multisigAccounts;
    }

    /**
     * Returns multisig accounts levels number.
     *
     * @return Set<Integer>
     */
    public Set<Integer> getLevelsNumber() {
        return this.multisigAccounts.keySet();
    }

    /**
     * Returns multisig accounts.
     *
     * @return Map<Integer ,   List < { @ link   MultisigAccountInfo }>>
     */
    public Map<Integer, List<MultisigAccountInfo>> getMultisigAccounts() {
        return multisigAccounts;
    }
}
