/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hudi.common.util;

import org.apache.hudi.avro.model.HoodieCleanMetadata;
import org.apache.hudi.avro.model.HoodieCleanPartitionMetadata;
import org.apache.hudi.avro.model.HoodieCleanerPlan;
import org.apache.hudi.common.HoodieCleanStat;
import org.apache.hudi.common.table.HoodieTableMetaClient;
import org.apache.hudi.common.table.timeline.HoodieInstant;
import org.apache.hudi.common.table.timeline.TimelineMetadataUtils;
import org.apache.hudi.common.table.timeline.versioning.clean.CleanMetadataMigrator;
import org.apache.hudi.common.table.timeline.versioning.clean.CleanV1MigrationHandler;
import org.apache.hudi.common.table.timeline.versioning.clean.CleanV2MigrationHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CleanerUtils {
  public static final Integer CLEAN_METADATA_VERSION_1 = CleanV1MigrationHandler.VERSION;
  public static final Integer CLEAN_METADATA_VERSION_2 = CleanV2MigrationHandler.VERSION;
  public static final Integer LATEST_CLEAN_METADATA_VERSION = CLEAN_METADATA_VERSION_2;

  public static HoodieCleanMetadata convertCleanMetadata(String startCleanTime,
                                                         Option<Long> durationInMs,
                                                         List<HoodieCleanStat> cleanStats) {
    Map<String, HoodieCleanPartitionMetadata> partitionMetadataMap = new HashMap<>();
    int totalDeleted = 0;
    String earliestCommitToRetain = null;
    for (HoodieCleanStat stat : cleanStats) {
      HoodieCleanPartitionMetadata metadata =
          new HoodieCleanPartitionMetadata(stat.getPartitionPath(), stat.getPolicy().name(),
              stat.getDeletePathPatterns(), stat.getSuccessDeleteFiles(), stat.getFailedDeleteFiles());
      partitionMetadataMap.put(stat.getPartitionPath(), metadata);
      totalDeleted += stat.getSuccessDeleteFiles().size();
      if (earliestCommitToRetain == null) {
        // This will be the same for all partitions
        earliestCommitToRetain = stat.getEarliestCommitToRetain();
      }
    }

    return new HoodieCleanMetadata(startCleanTime,
        durationInMs.orElseGet(() -> -1L), totalDeleted, earliestCommitToRetain, partitionMetadataMap, CLEAN_METADATA_VERSION_2);
  }

  /**
   * Get Latest Version of Hoodie Cleaner Metadata - Output of cleaner operation.
   * @param metaClient Hoodie Table Meta Client
   * @param cleanInstant Instant referring to clean action
   * @return Latest version of Clean metadata corresponding to clean instant
   * @throws IOException
   */
  public static HoodieCleanMetadata getCleanerMetadata(HoodieTableMetaClient metaClient, HoodieInstant cleanInstant)
      throws IOException {
    CleanMetadataMigrator metadataMigrator = new CleanMetadataMigrator(metaClient);
    HoodieCleanMetadata cleanMetadata = TimelineMetadataUtils.deserializeHoodieCleanMetadata(
        metaClient.getActiveTimeline().readCleanerInfoAsBytes(cleanInstant).get());
    return metadataMigrator.upgradeToLatest(cleanMetadata, cleanMetadata.getVersion());
  }

  /**
   * Get Cleaner Plan corresponding to a clean instant.
   * @param metaClient  Hoodie Table Meta Client
   * @param cleanInstant Instant referring to clean action
   * @return Cleaner plan corresponding to clean instant
   * @throws IOException
   */
  public static HoodieCleanerPlan getCleanerPlan(HoodieTableMetaClient metaClient, HoodieInstant cleanInstant)
      throws IOException {
    return TimelineMetadataUtils.deserializeAvroMetadata(metaClient.getActiveTimeline().readCleanerInfoAsBytes(cleanInstant).get(),
        HoodieCleanerPlan.class);
  }
}
