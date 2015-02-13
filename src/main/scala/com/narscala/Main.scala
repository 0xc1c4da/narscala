package com.narscala

import grammar.Narsese

object Main extends Narsese {

    def main(args: Array[String]) {
        // println(System.getProperty("file.encoding"))
        println(
            parseAll(task, args(0))
        )
    }

}