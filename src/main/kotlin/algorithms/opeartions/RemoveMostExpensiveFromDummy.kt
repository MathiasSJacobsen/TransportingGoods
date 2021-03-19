package algorithms.opeartions

import Solution
import utils.Vehicle

class RemoveMostExpensiveFromDummy : Operation() {
    /*
    Takes the most expensive call from the dummy vehicle and places it on one vehicle that
    can take that call.
     */
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val dummy = solutionWithIndependentRoutes[instance.numberOfVehicles]

        // If the dummy is empty
        if (dummy.size == 0){
            return solution
        }
        var mostExpensiveCallCost = 0
        var mostExpensiveCall = 0
        var availableVehiclesForMostExpensiveFromDummy: List<Vehicle?>  = listOf()
        val done = mutableListOf<Int>()
        for (call in dummy){
            // There is no point checking the same call twice, since there is two of each in every vehicle.
            if (done.size == dummy.size/2){
                break
            }
            if (call in done){
                continue
            }
            done.add(call)
            val callCost = instance.calls[call]?.costOfNotTransporting!!
            if (mostExpensiveCallCost < callCost){
                mostExpensiveCallCost = callCost
                mostExpensiveCall = call
                availableVehiclesForMostExpensiveFromDummy =  instance.vehicles.filter { it?.possibleCalls?.contains(call)!! }
            }
        }
        //TODO: finne den bilen som burde få den
        dummy.remove(mostExpensiveCall)
        dummy.remove(mostExpensiveCall)

        val chosenVehicle = availableVehiclesForMostExpensiveFromDummy.random()!!

        solutionWithIndependentRoutes[chosenVehicle.index].add(mostExpensiveCall)
        solutionWithIndependentRoutes[chosenVehicle.index].add(mostExpensiveCall)

        return Solution(instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }
}