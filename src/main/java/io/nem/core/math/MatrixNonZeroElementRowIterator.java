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

package io.nem.core.math;

/**
 * Interface for iterating through the nonzero elements of a matrix row.
 */
public interface MatrixNonZeroElementRowIterator {

    /**
     * Gets a value indicating whether or not the matrix row has more non-zero elements.
     *
     * @return true if the matrix row has more non-zero elements, false otherwise.
     */
    boolean hasNext();

    /**
     * Gets the next non-zero matrix row element.
     *
     * @return The next non-zero matrix element of the row.
     */
    MatrixElement next();
}
