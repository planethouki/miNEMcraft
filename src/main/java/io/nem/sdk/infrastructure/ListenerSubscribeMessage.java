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

package io.nem.sdk.infrastructure;

public class ListenerSubscribeMessage {
    final private String uid;
    final private String subscribe;

    public ListenerSubscribeMessage(String uid, String subscribe) {
        this.uid = uid;
        this.subscribe = subscribe;
    }

    public String getUid() {
        return uid;
    }

    public String getSubscribe() {
        return subscribe;
    }
}
