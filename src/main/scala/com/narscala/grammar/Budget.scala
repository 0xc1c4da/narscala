package com.narscala.grammar

import org.parboiled2._

// TODO: BudgetFactory, minimize memory usage of Budget values, OpenNARS memory is 10%
// TODO: Handle Durability default value

case class Budget(priority: Double, durability: Option[Double]) {
    require(priority <= 1.0, "Priority > 1.0")
    require(priority >= 0.0, "Priority < 0.0")
}