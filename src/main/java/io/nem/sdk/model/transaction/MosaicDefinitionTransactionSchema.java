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

import java.util.Arrays;

class MosaicDefinitionTransactionSchema extends Schema {
    public MosaicDefinitionTransactionSchema() {
        super(Arrays.asList(
                new ScalarAttribute("size", Constants.SIZEOF_INT),
                new ArrayAttribute("signature", Constants.SIZEOF_BYTE),
                new ArrayAttribute("signer", Constants.SIZEOF_BYTE),
                new ScalarAttribute("version", Constants.SIZEOF_SHORT),
                new ScalarAttribute("type", Constants.SIZEOF_SHORT),
                new ArrayAttribute("fee", Constants.SIZEOF_INT),
                new ArrayAttribute("deadline", Constants.SIZEOF_INT),
                new ArrayAttribute("parentId", Constants.SIZEOF_INT),
                new ArrayAttribute("mosaicId", Constants.SIZEOF_INT),
                new ScalarAttribute("mosaicNameLength", Constants.SIZEOF_BYTE),
                new ScalarAttribute("numOptionalProperties", Constants.SIZEOF_BYTE),
                new ScalarAttribute("flags", Constants.SIZEOF_BYTE),
                new ScalarAttribute("divisibility", Constants.SIZEOF_BYTE),
                new ArrayAttribute("mosaicName", Constants.SIZEOF_BYTE),
                new ScalarAttribute("indicateDuration", Constants.SIZEOF_BYTE),
                new ArrayAttribute("duration", Constants.SIZEOF_INT)
        ));
    }
}
