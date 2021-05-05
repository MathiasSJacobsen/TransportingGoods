package algorithms.operations

import Solution
import kotlin.math.min
import kotlin.random.Random
import utils.Quintuple
import utils.costFunction
import utils.isFeasible

abstract class Operation {

    abstract fun operation(solution: Solution): Solution

    /*
    Makes the a list with the independent routes
     */
    protected fun solutionWithIndependentRoutes(solution: Solution): MutableList<MutableList<Int>> {
        var vehicle = mutableListOf<Int>()
        val route = mutableListOf<MutableList<Int>>()
        for (e in solution.arr) {
            if (e == -1) {
                route.add(vehicle)
                vehicle = mutableListOf()
            } else {
                vehicle.add(e)
            }
        }
        route.add(vehicle)
        return route
    }

    protected fun fromIndependentRoutesToSolution(independentRoutes: MutableList<MutableList<Int>>): MutableList<Int> {
        val out = ArrayList<Int>(independentRoutes.sumOf { it.size + 1 }) //mutableListOf<Int>()
        var first = true
        for (route in independentRoutes) {
            if (!first) {
                out.add(-1)
            } else {
                first = false
            }
            for (call in route) {
                out.add(call)
            }
        }
        return out
    }


    protected fun removeCalls(solution: Solution): MutableList<Int>? {
        val vehicle = solutionWithIndependentRoutes(solution).random().toMutableList()

        if (vehicle.size == 0){
            return null
        }
        val random = Random.nextInt(0, min(vehicle.size/2, 4))

        val callsRemoved = mutableListOf<Int>()
        for (i in 0..random){
            callsRemoved.add(vehicle.random())
            vehicle.removeAll { callsRemoved[callsRemoved.lastIndex] == it }
        }
        return callsRemoved
    }

    protected fun bestInsert(solution: Solution, call: Int): Quintuple {
        var minCost : Int = Int.MAX_VALUE
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        var cheapestVehicle = solutionWithIndependentRoutes.lastIndex

        var insert1 = 0
        var insert2 = 1

        for ((v, route) in solutionWithIndependentRoutes.subList(0, solutionWithIndependentRoutes.lastIndex).withIndex()){
            val vehicleCost = costFunction(solution, v)
            for (i in 0..route.size){
                for (j in (i+1) until (route.size+2)){
                    val copy = route.toMutableList()
                    copy.add(i, call)
                    copy.add(j, call)
                    if (isFeasible(copy, solution.instance, v).first){
                        val temp = solutionWithIndependentRoutes.toMutableList()
                        temp[v] = copy
                        val test = fromIndependentRoutesToSolution(temp)
                        val gittOppNavn = Solution(solution.instance, test)
                        val cost = costFunction(gittOppNavn, v) - vehicleCost
                        if (cost < minCost){
                            minCost = cost
                            cheapestVehicle = v
                            insert1 = i
                            insert2 = j
                        }
                    }
                }
            }
        }
        return Quintuple(call, cheapestVehicle, insert1, insert2, minCost)
    }

}