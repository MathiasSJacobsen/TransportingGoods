import algorithms.BlindRandomSearch
import algorithms.IAlgorithm
import algorithms.LocalSearch
import algorithms.SimulatedAnnealing
import algorithms.SimulatedAnnealingBetter
import java.io.File
import utils.Instance
import utils.costFunction
import utils.parseInstance
import kotlin.system.measureTimeMillis
import utils.MarkdownMaker

val markdownMaker = MarkdownMaker()

fun main(args: Array<String>) {

    val CALL_007_VEHICLE_03 : Instance = parseInstance("src/main/kotlin/data/Call_7_Vehicle_3.txt")
    val CALL_018_VEHICLE_05 : Instance = parseInstance("src/main/kotlin/data/Call_18_Vehicle_5.txt")
    val CALL_035_VEHICLE_07 : Instance = parseInstance("src/main/kotlin/data/Call_035_Vehicle_07.txt")
    val CALL_080_VEHICLE_20 : Instance = parseInstance("src/main/kotlin/data/Call_080_Vehicle_20.txt")
    val CALL_130_VEHICLE_40 : Instance = parseInstance("src/main/kotlin/data/Call_130_Vehicle_40.txt")
    val ALGORITHMS =
        listOf<IAlgorithm>(BlindRandomSearch(), LocalSearch(), SimulatedAnnealing(), SimulatedAnnealingBetter())
    val INSTANCES = listOf<Instance>(
        CALL_007_VEHICLE_03,
        CALL_018_VEHICLE_05,
        CALL_035_VEHICLE_07,
        CALL_080_VEHICLE_20,
        CALL_130_VEHICLE_40
    )

    runAllInstancesWithAllAlgorithms(INSTANCES, ALGORITHMS)

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

fun runAlgo(instance: Instance, search: IAlgorithm): Result {
    val initSol = genInitialSolution(instance)
    var bestSol: Solution = initSol
    var t = initSol
    var averageCost = 0
    var sumtime = 0.0
    for (i in 0 until 10) {
        val time = measureTimeMillis {
            t = search.search(initSol)
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
    val averageRuntime = (sumtime / 10) / 1000
    return Result(
        name,
        search.name,
        (averageCost / 10).toLong(),
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