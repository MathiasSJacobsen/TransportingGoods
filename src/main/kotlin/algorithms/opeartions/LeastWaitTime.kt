package algorithms.opeartions

import Solution
import java.util.Collections
import utils.isFeasible

class LeastWaitTime : Operation() {
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        var copyOfSolutionWithIndependentRoutes = solutionWithIndependentRoutes

        // More then 10 takes forever to calculate
        val vehiclesWithMoreThenOneCall = solutionWithIndependentRoutes.filter { it.size in 3..9 }
        if (vehiclesWithMoreThenOneCall.isEmpty()){
            return solution
        }
        val chosen = vehiclesWithMoreThenOneCall.random()
        val vehicleOfChosen = solutionWithIndependentRoutes.indexOf(chosen)
        if (vehicleOfChosen == instance.vehicles.size) return solution
        // Find all perms and only care about the feasible of them
        val permutationsOfChosen = chosen.permutations().toMutableList().filter {
            copyOfSolutionWithIndependentRoutes[vehicleOfChosen] = it
            isFeasible(instance, fromIndependentRoutesToSolution(copyOfSolutionWithIndependentRoutes)).first
        }.toMutableList()

        if (permutationsOfChosen.size < 1) return solution

        val t = mutableListOf<Pair<Int, Int>>()
        for(index in (permutationsOfChosen.indices)){
            var sum = instance.vehicles[vehicleOfChosen]?.startingTime!!
            var currentPos = instance.vehicles[vehicleOfChosen]?.homeNode!!
            val nodeTime = instance.nodeTimeAndCost[vehicleOfChosen]!!
            val travelTimeForVehicle = instance.travelTimeAndCost[vehicleOfChosen]!!
            val done = hashSetOf<Int>()
            for(call in permutationsOfChosen[index]){
                val newPos = instance.calls[call]?.originNode!!
                sum += travelTimeForVehicle[currentPos]?.get(newPos)?.time!!
                currentPos = newPos
                if (call in done){
                    sum += nodeTime[call]?.originTime!!
                } else {
                    sum += nodeTime[call]?.destinationTime!!
                    done.add(call)
                }
            }
            t.add(Pair(index, sum))
        }
        val minWaitTime = t.minByOrNull { it.second }!!
        solutionWithIndependentRoutes[vehicleOfChosen] = permutationsOfChosen[minWaitTime.first]
        return Solution(instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
    }

    private fun <V> MutableList<V>.permutations(): HashSet<MutableList<V>> {
        val retVal: HashSet<MutableList<V>> = hashSetOf()

        fun generate(k: Int, list: List<V>) {
            // If only 1 element, just output the array
            if (k == 1) {
                retVal.add(list.toMutableList())
            } else {
                for (i in 0 until k) {

                    generate(k - 1, list)
                    if (k % 2 == 0) {
                        Collections.swap(list, i, k - 1)
                    } else {
                        Collections.swap(list, 0, k - 1)
                    }
                }
            }
        }

        generate(this.count(), this.toList())
        return retVal
    }
}