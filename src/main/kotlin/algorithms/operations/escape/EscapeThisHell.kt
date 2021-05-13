package algorithms.operations.escape

import Solution
import algorithms.operations.Operation
import algorithms.operations.TwoExchange
import utils.Instance
import utils.isFeasible

class EscapeThisHell : Operation() {
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        var t = solutionWithIndependentRoutes(solution)
        var i = 0
        var newSol = solution
        var bestSolution = newSol
        while (i < 20){
            newSol = generateRandomValidSolution(instance)
            if (isFeasible(instance, solution.arr).first){
                i++

                bestSolution = newSol
            }
        }
        return bestSolution
    }

    private fun generateRandomValidSolution(instance: Instance): Solution {
        val t = arrayListOf<Int>()
        for (i in 0 until instance.numberOfCalls){
            t.add(i)
        }
        for (i in 0 until instance.numberOfVehicles){
            t.add(-1)
        }
        t.shuffle()

        val sol = arrayListOf<Int>()
        var currentVehicle = arrayListOf<Int>()

        for (e in t){
            if (e == -1){
                currentVehicle.addAll(currentVehicle)
                currentVehicle.shuffle()
                sol.addAll(currentVehicle)
                sol.add(-1)
                currentVehicle = arrayListOf<Int>()
            } else {
                currentVehicle.add(e)
            }
        }
        currentVehicle.addAll(currentVehicle)
        currentVehicle.shuffle()
        sol.addAll(currentVehicle)
        return Solution(instance, sol)
    }

}