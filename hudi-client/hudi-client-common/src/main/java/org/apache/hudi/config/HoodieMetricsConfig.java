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

package org.apache.hudi.config;

import org.apache.hudi.common.config.ConfigClassProperty;
import org.apache.hudi.common.config.ConfigGroups;
import org.apache.hudi.common.config.ConfigProperty;
import org.apache.hudi.common.config.HoodieConfig;
import org.apache.hudi.metrics.MetricsReporterType;

import javax.annotation.concurrent.Immutable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Fetch the configurations used by the Metrics system.
 */
@Immutable
@ConfigClassProperty(name = "Metrics Configurations",
    groupName = ConfigGroups.Names.METRICS,
    description = "Enables reporting on Hudi metrics. Hudi publishes metrics on "
        + "every commit, clean, rollback etc. The following sections list the supported reporters.")
public class HoodieMetricsConfig extends HoodieConfig {

  public static final String METRIC_PREFIX = "hoodie.metrics";

  public static final ConfigProperty<Boolean> TURN_METRICS_ON = ConfigProperty
      .key(METRIC_PREFIX + ".on")
      .defaultValue(false)
      .sinceVersion("0.5.0")
      .withDocumentation("Turn on/off metrics reporting. off by default.");

  public static final ConfigProperty<MetricsReporterType> METRICS_REPORTER_TYPE_VALUE = ConfigProperty
      .key(METRIC_PREFIX + ".reporter.type")
      .defaultValue(MetricsReporterType.GRAPHITE)
      .sinceVersion("0.5.0")
      .withDocumentation("Type of metrics reporter.");

  // Graphite
  public static final String GRAPHITE_PREFIX = METRIC_PREFIX + ".graphite";

  public static final ConfigProperty<String> GRAPHITE_SERVER_HOST_NAME = ConfigProperty
      .key(GRAPHITE_PREFIX + ".host")
      .defaultValue("localhost")
      .sinceVersion("0.5.0")
      .withDocumentation("Graphite host to connect to");

  public static final ConfigProperty<Integer> GRAPHITE_SERVER_PORT_NUM = ConfigProperty
      .key(GRAPHITE_PREFIX + ".port")
      .defaultValue(4756)
      .sinceVersion("0.5.0")
      .withDocumentation("Graphite port to connect to");

  // Jmx
  public static final String JMX_PREFIX = METRIC_PREFIX + ".jmx";

  public static final ConfigProperty<String> JMX_HOST_NAME = ConfigProperty
      .key(JMX_PREFIX + ".host")
      .defaultValue("localhost")
      .sinceVersion("0.5.1")
      .withDocumentation("Jmx host to connect to");

  public static final ConfigProperty<Integer> JMX_PORT_NUM = ConfigProperty
      .key(JMX_PREFIX + ".port")
      .defaultValue(9889)
      .sinceVersion("0.5.1")
      .withDocumentation("Jmx port to connect to");

  public static final ConfigProperty<String> GRAPHITE_METRIC_PREFIX_VALUE = ConfigProperty
      .key(GRAPHITE_PREFIX + ".metric.prefix")
      .noDefaultValue()
      .sinceVersion("0.5.1")
      .withDocumentation("Standard prefix applied to all metrics. This helps to add datacenter, environment information for e.g");

  // User defined
  public static final ConfigProperty<String> METRICS_REPORTER_CLASS_NAME = ConfigProperty
      .key(METRIC_PREFIX + ".reporter.class")
      .defaultValue("")
      .sinceVersion("0.6.0")
      .withDocumentation("");

  // Enable metrics collection from executors
  public static final ConfigProperty<String> EXECUTOR_METRICS_ENABLE = ConfigProperty
      .key(METRIC_PREFIX + ".executor.enable")
      .noDefaultValue()
      .sinceVersion("0.7.0")
      .withDocumentation("");

  /**
   * @deprecated Use {@link #TURN_METRICS_ON} and its methods instead
   */
  @Deprecated
  public static final String METRICS_ON = TURN_METRICS_ON.key();
  /**
   * @deprecated Use {@link #TURN_METRICS_ON} and its methods instead
   */
  @Deprecated
  public static final boolean DEFAULT_METRICS_ON = TURN_METRICS_ON.defaultValue();
  /**
   * @deprecated Use {@link #METRICS_REPORTER_TYPE_VALUE} and its methods instead
   */
  @Deprecated
  public static final String METRICS_REPORTER_TYPE = METRICS_REPORTER_TYPE_VALUE.key();
  /**
   * @deprecated Use {@link #METRICS_REPORTER_TYPE_VALUE} and its methods instead
   */
  @Deprecated
  public static final MetricsReporterType DEFAULT_METRICS_REPORTER_TYPE = METRICS_REPORTER_TYPE_VALUE.defaultValue();
  /**
   * @deprecated Use {@link #GRAPHITE_SERVER_HOST_NAME} and its methods instead
   */
  @Deprecated
  public static final String GRAPHITE_SERVER_HOST = GRAPHITE_SERVER_HOST_NAME.key();
  /**
   * @deprecated Use {@link #GRAPHITE_SERVER_HOST_NAME} and its methods instead
   */
  @Deprecated
  public static final String DEFAULT_GRAPHITE_SERVER_HOST = GRAPHITE_SERVER_HOST_NAME.defaultValue();
  /**
   * @deprecated Use {@link #GRAPHITE_SERVER_PORT_NUM} and its methods instead
   */
  @Deprecated
  public static final String GRAPHITE_SERVER_PORT = GRAPHITE_SERVER_PORT_NUM.key();
  /**
   * @deprecated Use {@link #GRAPHITE_SERVER_PORT_NUM} and its methods instead
   */
  @Deprecated
  public static final int DEFAULT_GRAPHITE_SERVER_PORT = GRAPHITE_SERVER_PORT_NUM.defaultValue();
  /**
   * @deprecated Use {@link #JMX_HOST_NAME} and its methods instead
   */
  @Deprecated
  public static final String JMX_HOST = JMX_HOST_NAME.key();
  /**
   * @deprecated Use {@link #JMX_HOST_NAME} and its methods instead
   */
  @Deprecated
  public static final String DEFAULT_JMX_HOST = JMX_HOST_NAME.defaultValue();
  /**
   * @deprecated Use {@link #JMX_PORT_NUM} and its methods instead
   */
  @Deprecated
  public static final String JMX_PORT = JMX_PORT_NUM.key();
  /**
   * @deprecated Use {@link #JMX_PORT_NUM} and its methods instead
   */
  @Deprecated
  public static final int DEFAULT_JMX_PORT = JMX_PORT_NUM.defaultValue();
  /**
   * @deprecated Use {@link #GRAPHITE_METRIC_PREFIX_VALUE} and its methods instead
   */
  @Deprecated
  public static final String GRAPHITE_METRIC_PREFIX = GRAPHITE_METRIC_PREFIX_VALUE.key();
  /**
   * @deprecated Use {@link #METRICS_REPORTER_CLASS_NAME} and its methods instead
   */
  @Deprecated
  public static final String METRICS_REPORTER_CLASS = METRICS_REPORTER_CLASS_NAME.key();
  /**
   * @deprecated Use {@link #METRICS_REPORTER_CLASS_NAME} and its methods instead
   */
  @Deprecated
  public static final String DEFAULT_METRICS_REPORTER_CLASS = METRICS_REPORTER_CLASS_NAME.defaultValue();
  /**
   * @deprecated Use {@link #EXECUTOR_METRICS_ENABLE} and its methods instead
   */
  @Deprecated
  public static final String ENABLE_EXECUTOR_METRICS = EXECUTOR_METRICS_ENABLE.key();

  private HoodieMetricsConfig() {
    super();
  }

  public static HoodieMetricsConfig.Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private final HoodieMetricsConfig hoodieMetricsConfig = new HoodieMetricsConfig();

    public Builder fromFile(File propertiesFile) throws IOException {
      try (FileReader reader = new FileReader(propertiesFile)) {
        this.hoodieMetricsConfig.getProps().load(reader);
        return this;
      }
    }

    public Builder fromProperties(Properties props) {
      this.hoodieMetricsConfig.getProps().putAll(props);
      return this;
    }

    public Builder on(boolean metricsOn) {
      hoodieMetricsConfig.setValue(TURN_METRICS_ON, String.valueOf(metricsOn));
      return this;
    }

    public Builder withReporterType(String reporterType) {
      hoodieMetricsConfig.setValue(METRICS_REPORTER_TYPE_VALUE, reporterType);
      return this;
    }

    public Builder toGraphiteHost(String host) {
      hoodieMetricsConfig.setValue(GRAPHITE_SERVER_HOST_NAME, host);
      return this;
    }

    public Builder onGraphitePort(int port) {
      hoodieMetricsConfig.setValue(GRAPHITE_SERVER_PORT_NUM, String.valueOf(port));
      return this;
    }

    public Builder toJmxHost(String host) {
      hoodieMetricsConfig.setValue(JMX_HOST_NAME, host);
      return this;
    }

    public Builder onJmxPort(String port) {
      hoodieMetricsConfig.setValue(JMX_PORT_NUM, port);
      return this;
    }

    public Builder usePrefix(String prefix) {
      hoodieMetricsConfig.setValue(GRAPHITE_METRIC_PREFIX_VALUE, prefix);
      return this;
    }

    public Builder withReporterClass(String className) {
      hoodieMetricsConfig.setValue(METRICS_REPORTER_CLASS_NAME, className);
      return this;
    }

    public Builder withExecutorMetrics(boolean enable) {
      hoodieMetricsConfig.setValue(EXECUTOR_METRICS_ENABLE, String.valueOf(enable));
      return this;
    }

    public HoodieMetricsConfig build() {

      hoodieMetricsConfig.setDefaults(HoodieMetricsConfig.class.getName());

      MetricsReporterType reporterType = MetricsReporterType.valueOf(hoodieMetricsConfig.getString(METRICS_REPORTER_TYPE_VALUE));

      hoodieMetricsConfig.setDefaultOnCondition(reporterType == MetricsReporterType.DATADOG,
          HoodieMetricsDatadogConfig.newBuilder().fromProperties(hoodieMetricsConfig.getProps()).build());
      hoodieMetricsConfig.setDefaultOnCondition(reporterType == MetricsReporterType.PROMETHEUS_PUSHGATEWAY,
              HoodieMetricsPrometheusConfig.newBuilder().fromProperties(hoodieMetricsConfig.getProps()).build());
      hoodieMetricsConfig.setDefaultOnCondition(reporterType == MetricsReporterType.PROMETHEUS,
              HoodieMetricsPrometheusConfig.newBuilder().fromProperties(hoodieMetricsConfig.getProps()).build());
      return hoodieMetricsConfig;
    }
  }

}
