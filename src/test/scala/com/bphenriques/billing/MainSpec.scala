/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing

import com.bphenriques.helpers.BaseSpec

/**
  * Tests [[Application]].
  */
class MainSpec extends BaseSpec with Application {

  it must "should fail with invalid fails" in {
    process (getInvalidResource("bad-time-format.csv").getAbsolutePath).isFailure shouldBe true
  }

  it must "should succeed with valid files" in {
    process(getValidResource("sample.csv").getAbsolutePath).isSuccess shouldEqual true
  }
}
