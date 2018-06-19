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

package com.palantir.atlasdb.transaction.impl.lock;

import com.palantir.atlasdb.transaction.api.TransactionLockManager;
import com.palantir.lock.v2.LockRequest;
import com.palantir.lock.v2.LockToken;
import java.util.Optional;

public class NoOpTransactionLockManager implements TransactionLockManager {
    @Override
    public Optional<LockToken> acquireTransactionLock(LockRequest lockRequest) {
        return null;
    }

    @Override
    public void checkAndMaybeReleaseLocksBeforeCommit() {
    }

    @Override
    public void releaseLocks() {
    }
}