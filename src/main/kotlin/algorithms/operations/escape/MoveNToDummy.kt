package algorithms.operations.escape

import Solution
import algorithms.operations.Operation

class MoveNToDummy : Operation() {

    override fun operation(solution: Solution): Solution {
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val randomVehicle = (0 until solutionWithIndependentRoutes.lastIndex).random()
        val chosenVehicle = solutionWithIndependentRoutes[randomVehicle]

        if (chosenVehicle.isEmpty()) {
            return solution
        }

        var n = (2..(chosenVehicle.size / 2)).random()

        while (n > 0) {
            val call = chosenVehicle[1]
            solutionWithIndependentRoutes[randomVehicle].remove(call)
            solutionWithIndependentRoutes[randomVehicle].remove(call)
            solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].addAll(arrayOf(call, call))
            n -= 2
        }
        return Solution(solution.instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }
}
