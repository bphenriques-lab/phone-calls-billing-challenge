/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager

import java.text.DecimalFormat

/**
  * Type aliases.
  */
object Types {

  /**
    * Represents a financial amount.
    *
    * @note Using [[BigDecimal]] b/c it is precise while [[Double]] is not. This is crucial to compute prices.
    */
  type Cost = BigDecimal

  /**
    * Represents how [[com.talkdesk.billing.manager.Types.Cost]] can be formatted.
    *
    * @see [[https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html DecimalFormat]]
    */
  type CostFormat = DecimalFormat
}
