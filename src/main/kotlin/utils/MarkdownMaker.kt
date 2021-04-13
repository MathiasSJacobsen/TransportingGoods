package utils

import java.io.File
import Result

class MarkdownMaker {
    fun makeMarkdownFile(tables: String) {
        val file = File("src/main/kotlin/test.md")
        file.writeText("")
        file.appendText("<h1>Transporting Goods\n")
        file.appendText("<h4>By Mathias Skallerud Jacobsen\n")
        file.appendText("\n\nINF273 - Meta-Heuristics  \n\n")
        file.appendText(tables)
        file.appendText("  \n\n\n")

    }

    fun makeMarkdownTableForAGivenInstance(results: ArrayList<Result>): String {
        var table = "| | | ${results[0].instanceName} | | |  \n" +
                "| --- | --- | --- | --- | --- |  \n" +
                "| | Ave. objective | Best objective | Imp (%) | Runtime |  \n"
        var bestSolutions = ""
        for (result in results) {
            val search = result.search
            val averageCost = result.averageCost
            val bestObjective = result.bestObjective
            val improvement = result.improvement
            val runningTime = result.runningTime
            table += "| $search | $averageCost | $bestObjective | $improvement % | $runningTime s |  \n"
            bestSolutions += "Best solution $search: `${result.solution}`  \n"
        }
        table += "  \n  \n"
        table += bestSolutions
        table += "  \n  \n"

        return table
    }

}