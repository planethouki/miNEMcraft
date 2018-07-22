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


import io.nem.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

import java.net.MalformedURLException;

/**
 * Network http repository.
 *
 * @since 1.0
 */
public class NetworkHttp extends Http implements NetworkRepository {
    public NetworkHttp(String host) throws MalformedURLException {
        super(host + "/network");
    }

    public Observable<NetworkType> getNetworkType() {
        return this.client
                .getAbs(this.url.toString())
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(Http::mapJsonObjectOrError)
                .map(json -> json.getString("name"))
                .map(name -> {
                    if (name.equalsIgnoreCase("mijinTest"))
                        return NetworkType.MIJIN_TEST;
                    else {
                        throw new IllegalArgumentException("network " + name + " is not supported yet by the sdk");
                    }
                });
    }
}
