/**
 * Copyright 2016 Palantir Technologies
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
package com.palantir.atlasdb.keyvalue.dbkvs.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.palantir.atlasdb.keyvalue.dbkvs.impl.oracle.OverflowSequenceSupplier;
import com.palantir.nexus.db.sql.SqlConnection;

public class OverflowSequenceSupplierTest {

    @Test
    public void shouldGetConsecutiveOverflowIdsFromSameSupplier() throws Exception {
        final ConnectionSupplier conns = mock(ConnectionSupplier.class);
        final SqlConnection sqlConnection = mock(SqlConnection.class);

        when(conns.get()).thenReturn(sqlConnection);
        when(sqlConnection.selectIntegerUnregisteredQuery(anyString())).thenReturn(1);

        OverflowSequenceSupplier sequenceSupplier = OverflowSequenceSupplier.create(conns, "a_");
        long firstSequenceId = sequenceSupplier.get();
        long nextSequenceId = sequenceSupplier.get();

        assertThat(nextSequenceId - firstSequenceId, is(1L));
    }

    @Test
    public void shouldGetConsecutiveOverflowIdsFromDifferentSuppliers() throws Exception {
        final ConnectionSupplier conns = mock(ConnectionSupplier.class);
        final SqlConnection sqlConnection = mock(SqlConnection.class);

        when(conns.get()).thenReturn(sqlConnection);
        when(sqlConnection.selectIntegerUnregisteredQuery(anyString())).thenReturn(1, 1001);

        long firstSequenceId = OverflowSequenceSupplier.create(conns, "a_").get();
        long nextSequenceId = OverflowSequenceSupplier.create(conns, "a_").get();

        assertThat(nextSequenceId - firstSequenceId, is(1L));
    }
}
