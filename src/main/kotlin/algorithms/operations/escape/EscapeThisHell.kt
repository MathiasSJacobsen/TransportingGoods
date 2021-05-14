package algorithms.operations.escape

import Solution
import algorithms.operations.Operation
import algorithms.operations.TwoExchange
import utils.Instance
import utils.isFeasible

class EscapeThisHell : Operation() {
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        var solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        var randomVehicle = (0 until solutionWithIndependentRoutes.lastIndex).random()
        var chosenVehicle = solutionWithIndependentRoutes[randomVehicle].toMutableList()
        if (solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].size == instance.numberOfCalls*2){
            return solution
        }
        while (chosenVehicle.size == 0) {
            randomVehicle = (0 until solutionWithIndependentRoutes.lastIndex).random()
            chosenVehicle = solutionWithIndependentRoutes[randomVehicle]
        }
        val numberOfCallsToTakeOut = (0..chosenVehicle.size step 2)
        val r = (1..(numberOfCallsToTakeOut.last / 2)).random()

        for (i in 0 until r) {
            val callToRemove = chosenVehicle[0]
            chosenVehicle.remove(callToRemove)
            chosenVehicle.remove(callToRemove)
            solutionWithIndependentRoutes[randomVehicle] = chosenVehicle
            val compatibleVehicle = findCompatibleVehicleForCall(callToRemove, randomVehicle, instance)
            if (compatibleVehicle.size == 0) {
                solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].add(callToRemove)
                solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].add(callToRemove)
            } else {
                var foundPlaceForCall = false
                for (vehicle in compatibleVehicle){
                    foundPlaceForCall = false
                    val copyOfTestVehicle = solutionWithIndependentRoutes[vehicle].toMutableList()
                    val copyOfTestRoute = solutionWithIndependentRoutes.toMutableList()
                    copyOfTestVehicle.add(callToRemove)
                    copyOfTestVehicle.add(callToRemove)
                    copyOfTestRoute[vehicle] = copyOfTestVehicle
                    if(isFeasible(fromIndependentRoutesToSolution(copyOfTestRoute), instance, vehicle).first){
                        foundPlaceForCall = true
                        solutionWithIndependentRoutes = copyOfTestRoute
                        break
                    }
                }
                if (!foundPlaceForCall) {
                    solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].add(callToRemove)
                    solutionWithIndependentRoutes[solutionWithIndependentRoutes.lastIndex].add(callToRemove)

                }
            }
        }
        return Solution(instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }

    private fun findCompatibleVehicleForCall(
        call: Int,
        vehicleToNotInclude: Int,
        instance: Instance
    ): MutableList<Int> {
        val compatibleVehicle = mutableListOf<Int>()
        for (vehicle in instance.vehicles) {
            if (vehicle?.possibleCalls?.contains(call)!! && vehicle.index != vehicleToNotInclude) {
                compatibleVehicle.add(vehicle.index)
            }
        }
        return compatibleVehicle
    }


}