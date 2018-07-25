/*
 * Copyright 2017 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.atlasdb.schema;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.palantir.atlasdb.schema.cleanup.ArbitraryCleanupMetadata;
import com.palantir.atlasdb.schema.cleanup.ImmutableStreamStoreCleanupMetadata;
import com.palantir.atlasdb.schema.cleanup.NullCleanupMetadata;
import com.palantir.atlasdb.table.description.ValueType;

public class SchemaDependentTableMetadataTest {
    @Test
    public void canSerializeAndDeserializeWithStreamStoreCleanupMetadata() {
        Arrays.stream(ValueType.values())
                .forEach(valueType -> {
                    SchemaDependentTableMetadata metadata = ImmutableSchemaDependentTableMetadata.builder()
                            .cleanupMetadata(ImmutableStreamStoreCleanupMetadata.builder()
                                    .numHashedRowComponents(1)
                                    .streamIdType(valueType)
                                    .build())
                            .build();
                    assertEqualAfterSerializationAndDeserialization(metadata);
                });
    }

    @Test
    public void canSerializeAndDeserializeWithNullCleanupMetadata() {
        SchemaDependentTableMetadata metadata = ImmutableSchemaDependentTableMetadata.builder()
                .cleanupMetadata(new NullCleanupMetadata())
                .build();
        assertEqualAfterSerializationAndDeserialization(metadata);
    }

    @Test
    public void canSerializeAndDeserializeWithArbitraryCleanupMetadata() {
        SchemaDependentTableMetadata metadata = ImmutableSchemaDependentTableMetadata.builder()
                .cleanupMetadata(new ArbitraryCleanupMetadata())
                .build();
        assertEqualAfterSerializationAndDeserialization(metadata);
    }

    private void assertEqualAfterSerializationAndDeserialization(SchemaDependentTableMetadata metadata) {
        assertThat(SchemaDependentTableMetadata.BYTES_HYDRATOR.hydrateFromBytes(metadata.persistToBytes()))
                .isEqualTo(metadata);
    }
}