package algorithms.operations.escape

import Solution
import algorithms.operations.Operation

class MoveAllCallsInVehicleToDummy : Operation() {
    override fun operation(solution: Solution): Solution {
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val randomVehicle = (0 until solutionWithIndependentRoutes.lastIndex).random()
        val chosenVehicle = solutionWithIndependentRoutes[randomVehicle]

        if (chosenVehicle.isEmpty()) {
            return solution
        }
        val allCalls = solutionWithIndependentRoutes[randomVehicle]
        solutionWithIndependentRoutes[randomVehicle] = mutableListOf()
        solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].addAll(allCalls)

        return Solution(solution.instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }

}