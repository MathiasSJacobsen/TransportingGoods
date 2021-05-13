import algorithms.BlindRandomSearch
import algorithms.GeneralAdaptiveMetaHeuristicFramework
import algorithms.IAlgorithm
import algorithms.LocalSearch
import algorithms.SimulatedAnnealing
import algorithms.SimulatedAnnealingBetter
import java.time.LocalTime
import utils.Instance
import utils.costFunction
import utils.parseInstance
import kotlin.system.measureTimeMillis
import utils.MarkdownMaker

val markdownMaker = MarkdownMaker()

fun main(args: Array<String>) {

    val CALL_007_VEHICLE_03: Instance = parseInstance("src/main/kotlin/data/Call_7_Vehicle_3.txt")
    val CALL_018_VEHICLE_05: Instance = parseInstance("src/main/kotlin/data/Call_18_Vehicle_5.txt")
    val CALL_035_VEHICLE_07: Instance = parseInstance("src/main/kotlin/data/Call_035_Vehicle_07.txt")
    val CALL_080_VEHICLE_20: Instance = parseInstance("src/main/kotlin/data/Call_080_Vehicle_20.txt")
    val CALL_130_VEHICLE_40: Instance = parseInstance("src/main/kotlin/data/Call_130_Vehicle_40.txt")
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
    runAllInstancesWithAllAlgorithms(INSTANCES, listOf(GeneralAdaptiveMetaHeuristicFramework()))

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
    val iterations = 1
    val initSol = genInitialSolution(instance)
    var bestSol: Solution = initSol
    var t: Solution
    var averageCost = 0
    var sumtime = 0.0
    val timeConstraint = when (instance.numberOfVehicles) {
        3 -> 4.0
        5 -> 19.0
        7 -> 99.0
        20 -> 174.0
        40 -> 300.0
        else -> kotlin.error("Cant find time constraint")
    }


    for (i in 0 until iterations) {
        val time = measureTimeMillis {
            t = heuristic.search(initSol, timeConstraint)
        }
        val costNewInstance = costFunction(t.instance, t.arr)
        if (costNewInstance < costFunction(bestSol.instance, bestSol.arr)) {
            bestSol = t
        }
        averageCost += costNewInstance
        sumtime += time
    }
    val name = "Call_${instance.calls.size}_Vehicle_${instance.vehicles.size}"
    val improvement = (costFunction(initSol.instance, initSol.arr).toDouble() - bestSol.cost) / costFunction(
        initSol.instance,
        initSol.arr
    )
    val averageRuntime = (sumtime / iterations) / 1000
    return Result(
        name,
        heuristic.name,
        (averageCost / iterations).toLong(),
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