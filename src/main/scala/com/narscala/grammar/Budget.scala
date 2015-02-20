package com.narscala.grammar

import com.typesafe.config.ConfigFactory
import org.parboiled2._

// TODO: BudgetFactory, minimize memory usage of Budget values, OpenNARS memory is 10% Budget Values

object BudgetDefaults {
    lazy val priority:   Double = ConfigFactory.load().getDouble("narscala.defaults.priority")
    lazy val durability: Double = ConfigFactory.load().getDouble("narscala.defaults.durability")
}

case class Budget(  priority:   Double = BudgetDefaults.priority,
                    durability: Option[Double] = Some(BudgetDefaults.durability)
    ) {
    require(priority <= 1.0, "Priority > 1.0 at " + priority)
    require(priority >= 0.0, "Priority < 0.0 at " + priority)
    require(durability.getOrElse(BudgetDefaults.durability) <= 1.0, "Durability > 1.0 at " + durability.getOrElse(BudgetDefaults.durability))
    require(durability.getOrElse(BudgetDefaults.durability) >= 0.0, "Durability < 0.0 at " + durability.getOrElse(BudgetDefaults.durability))
}