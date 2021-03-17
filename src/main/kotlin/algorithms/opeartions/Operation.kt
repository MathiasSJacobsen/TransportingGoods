package algorithms.opeartions

import Solution

abstract class Operation {

    abstract fun operation(solution: Solution): Solution

    /*
    Makes the a list with the independent routes
     */
    fun solutionWithIndependentRoutes(solution: Solution): MutableList<MutableList<Int>> {
        var vehicle = mutableListOf<Int>()
        val route = mutableListOf<MutableList<Int>>()
        for (e in solution.arr){
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

    fun fromIndependentRoutesToSolution(independentRoutes: MutableList<MutableList<Int>>): MutableList<Int> {
        val out = mutableListOf<Int>()
        for (route in independentRoutes){
            out.add(-1)
            for (call in route){
                out.add(call)
            }
        }
        return out
    }

}