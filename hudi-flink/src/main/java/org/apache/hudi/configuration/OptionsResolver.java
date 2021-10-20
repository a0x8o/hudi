/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hudi.configuration;

import org.apache.hudi.common.model.WriteOperationType;

import org.apache.flink.configuration.Configuration;

import java.util.Locale;

/**
 * Tool helping to resolve the flink options {@link FlinkOptions}.
 */
public class OptionsResolver {
  /**
   * Returns whether insert clustering is allowed with given configuration {@code conf}.
   */
  public static boolean insertClustering(Configuration conf) {
    return isCowTable(conf) && isInsertOperation(conf) && conf.getBoolean(FlinkOptions.INSERT_CLUSTER);
  }

  /**
   * Returns whether the insert is clustering disabled with given configuration {@code conf}.
   */
  public static boolean isAppendMode(Configuration conf) {
    return isCowTable(conf) && isInsertOperation(conf) && !conf.getBoolean(FlinkOptions.INSERT_CLUSTER);
  }

  /**
   * Returns whether the table operation is 'insert'.
   */
  public static boolean isInsertOperation(Configuration conf) {
    WriteOperationType operationType = WriteOperationType.fromValue(conf.getString(FlinkOptions.OPERATION));
    return operationType == WriteOperationType.INSERT;
  }

  /**
   * Returns whether it is a MERGE_ON_READ table.
   */
  public static boolean isMorTable(Configuration conf) {
    return conf.getString(FlinkOptions.TABLE_TYPE)
        .toUpperCase(Locale.ROOT)
        .equals(FlinkOptions.TABLE_TYPE_MERGE_ON_READ);
  }

  /**
   * Returns whether it is a COPY_ON_WRITE table.
   */
  public static boolean isCowTable(Configuration conf) {
    return conf.getString(FlinkOptions.TABLE_TYPE)
        .toUpperCase(Locale.ROOT)
        .equals(FlinkOptions.TABLE_TYPE_COPY_ON_WRITE);
  }
}
