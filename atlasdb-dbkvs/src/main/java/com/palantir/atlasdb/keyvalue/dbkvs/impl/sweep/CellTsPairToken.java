/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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
package com.palantir.atlasdb.keyvalue.dbkvs.impl.sweep;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.palantir.atlasdb.encoding.PtBytes;
import com.palantir.logsafe.Preconditions;

@Value.Immutable
public abstract class CellTsPairToken {
    public abstract byte[] startRowInclusive();

    @Value.Default
    public byte[] startColInclusive() {
        return PtBytes.EMPTY_BYTE_ARRAY;
    }

    @Nullable
    @Value.Default
    public Long startTsInclusive() {
        return null;
    }

    @Value.Default
    public boolean reachedEnd() {
        return false;
    }

    public static CellTsPairToken startRow(byte[] startRowInclusive) {
        return ImmutableCellTsPairToken.builder()
                .startRowInclusive(startRowInclusive)
                .build();
    }

    public static CellTsPairToken continueRow(CellTsPairInfo lastResult) {
        Preconditions.checkState(lastResult.ts != Long.MAX_VALUE, "Illegal timestamp MAX_VALUE");

        return ImmutableCellTsPairToken.builder()
                .startRowInclusive(lastResult.rowName)
                .startColInclusive(lastResult.colName)
                .startTsInclusive(lastResult.ts + 1)
                .build();
    }

    public static CellTsPairToken end() {
        return ImmutableCellTsPairToken.builder()
                .startRowInclusive(PtBytes.EMPTY_BYTE_ARRAY)
                .reachedEnd(true)
                .build();
    }
}
