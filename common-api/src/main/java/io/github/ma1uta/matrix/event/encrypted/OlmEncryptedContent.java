/*
 * Copyright sablintolya@gmail.com
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

package io.github.ma1uta.matrix.event.encrypted;

import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.RoomEncryptedContent;
import io.github.ma1uta.matrix.event.nested.CiphertextInfo;
import io.github.ma1uta.matrix.support.DeserializerUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * Olm encrypted message.
 */
@Schema(
    description = "Olm encrypted message."
)
public class OlmEncryptedContent extends RoomEncryptedContent {

    /**
     * Required. The encrypted content of the event. A map from the recipient Curve25519 identity key to ciphertext information.
     * For more details, see Messaging Algorithms.
     */
    @Schema(
        description = "The encrypted content of the event. A map from the recipient Curve25519 identity key to ciphertext information."
            + " For more details, see Messaging Algorithms.",
        required = true
    )
    private Map<String, CiphertextInfo> ciphertext;

    public OlmEncryptedContent() {
    }

    public OlmEncryptedContent(Map props) {
        super(props);
        this.ciphertext = DeserializerUtil.toMap(props, "ciphertext",
            entry -> (String) entry.getKey(),
            entry -> new CiphertextInfo((Map) entry.getValue())
        );
    }

    public Map<String, CiphertextInfo> getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(Map<String, CiphertextInfo> ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getAlgorithm() {
        return Event.Encryption.OLM;
    }
}
