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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.datasketches.hive.theta;

import org.apache.hadoop.io.BytesWritable;
import org.testng.annotations.Test;

import org.apache.datasketches.hive.theta.EstimateSketchUDF;

import org.apache.datasketches.theta.CompactSketch;
import org.apache.datasketches.theta.UpdateSketch;
import org.apache.datasketches.theta.Sketches;
import static org.testng.AssertJUnit.assertEquals;

@SuppressWarnings("javadoc")
public class EstimateSketchUDFTest {

  @Test
  public void evaluateNull() {
    EstimateSketchUDF testObject = new EstimateSketchUDF();

    Double testResult = testObject.evaluate(null);

    assertEquals(testResult, 0.0);
  }

  @Test
  public void evaluateEmptyInput() {
    EstimateSketchUDF testObject = new EstimateSketchUDF();

    BytesWritable testInput = new BytesWritable();

    Double testResult = testObject.evaluate(testInput);

    assertEquals(testResult, 0.0);
  }

  @Test
  public void evaluateValid() {
    EstimateSketchUDF testObject = new EstimateSketchUDF();

    UpdateSketch sketch = Sketches.updateSketchBuilder().setNominalEntries(1024).build();
    for (int i = 0; i<128; i++) {
      sketch.update(i);
    }

    BytesWritable input = new BytesWritable(sketch.toByteArray());

    Double testResult = testObject.evaluate(input);

    assertEquals(128.0, testResult);

    CompactSketch compactSketch = sketch.compact(false, null);
    input = new BytesWritable(compactSketch.toByteArray());

    testResult = testObject.evaluate(input);

    assertEquals(128.0, testResult);
  }

  @Test
  public void evaluateValidExplicitSeed() {
    EstimateSketchUDF testObject = new EstimateSketchUDF();

    final long seed = 1;
    UpdateSketch sketch = Sketches.updateSketchBuilder().setSeed(seed).setNominalEntries(1024).build();
    for (int i = 0; i<128; i++) {
      sketch.update(i);
    }

    BytesWritable input = new BytesWritable(sketch.toByteArray());

    Double testResult = testObject.evaluate(input, seed);

    assertEquals(128.0, testResult);

    CompactSketch compactSketch = sketch.compact(false, null);
    input = new BytesWritable(compactSketch.toByteArray());

    testResult = testObject.evaluate(input, seed);

    assertEquals(128.0, testResult);
  }

}
