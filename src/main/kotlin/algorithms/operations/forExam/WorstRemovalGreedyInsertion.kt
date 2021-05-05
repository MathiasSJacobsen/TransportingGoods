package algorithms.operations.forExam

import Solution
import algorithms.operations.Operation
import kotlin.random.Random
import utils.costFunction

class WorstRemovalGreedyInsertion : Operation() {
    /**
     * Finds random number of calls [1, 5) that cost the most, and uses the best greedy insertion to replace them
     */
    override fun operation(solution: Solution): Solution {
        val costOfCalls = HashMap<Int, Int>()
        costOfCallsInRoute(solution, costOfCalls)
        val random = Random.nextInt(1,5)
        var tempArr = solution.arr.toMutableList()
        var tempSolution = solution

        val t = ArrayList<Map.Entry<Int, Int>>(random)
        val copyHashMap = costOfCalls.toMap().toMutableMap()
        for (i in 0 until random){
            val maxCostWithCall = copyHashMap.maxByOrNull { l -> l.value }!!
            t.add(maxCostWithCall)
            copyHashMap.remove(maxCostWithCall.key)
        }
        tempArr.removeAll(t.map { it.key })

        for (i in 0 until random) {
            val expensiveCall = t.maxByOrNull { it.value }!!
            t.remove(expensiveCall)
            tempSolution = Solution(solution.instance, tempArr.toMutableList())

            val quintuple = bestInsert(tempSolution, expensiveCall.key)
            val gittoppnavn = solutionWithIndependentRoutes(tempSolution)

            gittoppnavn[quintuple.routeIndx].add(quintuple.insert1, expensiveCall.key)
            gittoppnavn[quintuple.routeIndx].add(quintuple.insert2, expensiveCall.key)
            tempArr = fromIndependentRoutesToSolution(gittoppnavn).toMutableList()

        }
        tempSolution = Solution(solution.instance, tempArr.toMutableList())
        return tempSolution
    }


    fun costOfCallsInRoute(solution: Solution, hashMap: HashMap<Int, Int>) {
        val routes = solutionWithIndependentRoutes(solution)
        for ((i, vehicle) in routes.withIndex()) {
            val costOfVehicle = costFunction(solution, i)
            for (call in vehicle.distinct()) {
                if (i == routes.lastIndex) {
                    hashMap[call] = costOfVehicle
                }
                else {
                    val copyVehicle = vehicle.toMutableList()
                    copyVehicle.filter { it != call }
                    val tempRoute = solutionWithIndependentRoutes(solution).toMutableList()
                    tempRoute[i] = copyVehicle


                    val tempSolution = Solution(solution.instance, fromIndependentRoutesToSolution(tempRoute))
                    val costOFCall = costFunction(tempSolution, i) - costOfVehicle
                    hashMap[call] = costOFCall
                }
            }
        }
    }

}