import algorithms.BlindRandomSearch
import algorithms.GeneralAdaptiveMetaHeuristicFramework
import algorithms.IAlgorithm
import algorithms.LocalSearch
import algorithms.SimulatedAnnealing
import algorithms.SimulatedAnnealingBetter
import algorithms.operations.escape.EscapeThisHell
import java.time.LocalDateTime
import utils.Instance
import utils.costFunction
import utils.parseInstance
import kotlin.system.measureTimeMillis
import utils.MarkdownMaker

val markdownMaker = MarkdownMaker()
const val iterations = 1
fun main(args: Array<String>) {

    val CALL_007_VEHICLE_03: Instance = parseInstance("src/main/kotlin/data/Exam_instances/New_Call_7.txt")
    val CALL_018_VEHICLE_05: Instance = parseInstance("src/main/kotlin/data/Exam_instances/New_Call_18.txt")
    val CALL_035_VEHICLE_07: Instance = parseInstance("src/main/kotlin/data/Exam_instances/New_Call_35.txt")
    val CALL_080_VEHICLE_20: Instance = parseInstance("src/main/kotlin/data/Exam_instances/New_Call_80.txt")
    val CALL_130_VEHICLE_40: Instance = parseInstance("src/main/kotlin/data/Exam_instances/New_Call_130.txt")
    val ALGORITHMS =
        listOf<IAlgorithm>(
            BlindRandomSearch(),
            LocalSearch(),
            SimulatedAnnealing(),
            SimulatedAnnealingBetter(),
            GeneralAdaptiveMetaHeuristicFramework()
        )
    val INSTANCES = listOf<Instance>(
        CALL_007_VEHICLE_03,
        CALL_018_VEHICLE_05,
        CALL_035_VEHICLE_07,
        CALL_080_VEHICLE_20,
        CALL_130_VEHICLE_40,
    )

    val start = LocalDateTime.now()
    println(start)
    val final = start.plusSeconds((600*iterations).toLong())
    println(final)
    runAllInstancesWithAllAlgorithms(INSTANCES, listOf(GeneralAdaptiveMetaHeuristicFramework()))
    println(LocalDateTime.now())

}

fun genInitialSolution(instance: Instance): Solution {
    val sol = arrayListOf<Int>()

    for (i in 0 until instance.numberOfVehicles) {
        sol.add(-1)
    }
    for (i in 0 until instance.numberOfCalls) {
        sol.add(i)
        sol.add(i)
    }
    return Solution(instance, sol)
}

fun runAlgo(instance: Instance, heuristic: IAlgorithm): Result {

    val initSol = genInitialSolution(instance)
    var bestSol: Solution = initSol
    var resultFromSearch: Solution
    var averageCost = 0
    var sumtime = 0.0
    val timeConstraint = when (instance.numberOfVehicles) {
        3 -> 4.0/4
        5 -> 19.0/4
        7 -> 99.0/4
        20 -> 174.0/4
        40 -> 300.0/4
        else -> kotlin.error("Cant find time constraint")
    }



    for (i in 0 until iterations*4) {
        val time = measureTimeMillis {
            resultFromSearch = heuristic.search(initSol, timeConstraint)
        }
        val costNewInstance = costFunction(resultFromSearch.instance, resultFromSearch.arr)
        if (costNewInstance < costFunction(bestSol.instance, bestSol.arr)) {
            bestSol = resultFromSearch
        }
        averageCost += costNewInstance
        sumtime += time
    }
    val name = "Call_${instance.calls.size}_Vehicle_${instance.vehicles.size}"
    val improvement = (costFunction(initSol.instance, initSol.arr).toDouble() - bestSol.cost) / costFunction(
        initSol.instance,
        initSol.arr
    )
    val averageRuntime = (sumtime / (iterations*4)) / 1000
    println(improvement)
    println(bestSol.solution)
    return Result(
        name,
        heuristic.name,
        (averageCost / (iterations*4)).toLong(),
        bestSol.cost,
        String.format("%.3f", improvement * 100).toDouble(),
        "%.4f".format(averageRuntime),
        bestSol.instance,
        bestSol.solution
    )
}

fun runAllInstancesWithAllAlgorithms(instances: List<Instance>, algorithms: List<IAlgorithm>) {
    var tables = ""
    for (instance in instances) {
        val results = arrayListOf<Result>()
        for (algo in algorithms) {
            results.add(runAlgo(instance, algo))
        }
        tables += markdownMaker.makeMarkdownTableForAGivenInstance(results)
    }
    markdownMaker.makeMarkdownFile(tables)
}