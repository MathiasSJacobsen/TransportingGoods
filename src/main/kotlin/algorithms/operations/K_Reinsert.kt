package algorithms.operations

import Solution

class K_Reinsert : Operation(){
    override fun operation(solution: Solution): Solution  {
        val instance = solution.instance
        val K = 4
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val random = (0 until solutionWithIndependentRoutes.size).random()
        val chosen = solutionWithIndependentRoutes[random]

        if (chosen.size < K*2){
            return solution
        }

        val calls = mutableListOf<Int>()
        while (calls.size != K){
            val callToBeReInserted = chosen[0]
            chosen.removeAll { it == callToBeReInserted }
            calls.add(callToBeReInserted)
        }
        solutionWithIndependentRoutes[random] = chosen

        val possibleVehicleForCall = mutableListOf<Pair<Int, MutableList<Int>>>()
        for (call in calls) {
            val vehicles = mutableListOf<Int>()
            for (instanceVehicle in instance.vehicles){
                if (instanceVehicle?.possibleCalls?.contains(call)!!){
                    vehicles.add(instanceVehicle.index)
                }
            }
            possibleVehicleForCall.add(Pair(call, vehicles))
        }
        for (pair in possibleVehicleForCall){
            val vehicle = pair.second.random()
            solutionWithIndependentRoutes[vehicle].add(pair.first)
            solutionWithIndependentRoutes[vehicle].add(pair.first)

        }
        return Solution(instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }
}