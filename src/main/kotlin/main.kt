import algorithms.IAlgorithm
import algorithms.SimulatedAnnealing
import utils.Instance
import utils.costFunction
import utils.parseInstance
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    val CALL_007_VEHICLE_03 = parseInstance("src/main/kotlin/data/Call_7_Vehicle_3.txt")
    val CALL_018_VEHICLE_05 = parseInstance("src/main/kotlin/data/Call_18_Vehicle_5.txt")
    val CALL_035_VEHICLE_07 = parseInstance("src/main/kotlin/data/Call_035_Vehicle_07.txt")
    val CALL_080_VEHICLE_20 = parseInstance("src/main/kotlin/data/Call_080_Vehicle_20.txt")
    val CALL_130_VEHICLE_40 = parseInstance("src/main/kotlin/data/Call_130_Vehicle_40.txt")
    runAlgo(CALL_080_VEHICLE_20, SimulatedAnnealing())

    val e = mutableListOf<Int>(1,2,3,4,5,6,7)
    print(e)

}
fun genInitialSolution(instance: Instance): Solution {
    val sol = arrayListOf<Int>()

    for (i in 0 until instance.numberOfVehicles){
        sol.add(-1)
    }
    for (i in 0 until instance.numberOfCalls){
        sol.add(i)
        sol.add(i)
    }
    return Solution(instance, sol)

}
fun runAlgo(instance: Instance, search: IAlgorithm) {
    val initSol = genInitialSolution(instance)
    var bestSol : Solution = initSol
    var t = initSol
    var avrageCost = 0
    for (i in 0 until 10){
        val time = measureTimeMillis {
            t = search.search(initSol)
        }
        val costNewInstance = costFunction(t.instance, t.arr)
            if (costNewInstance < costFunction(bestSol.instance, bestSol.arr)){
                bestSol=t
        }
        avrageCost += costNewInstance
        println(time)
    }
    println(avrageCost/10)
    println((costFunction(initSol.instance, initSol.arr).toDouble()-bestSol.cost)/costFunction(initSol.instance, initSol.arr))
}