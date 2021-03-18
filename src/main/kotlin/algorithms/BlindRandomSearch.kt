package algorithms

import Solution
import utils.Instance
import utils.costFunction
import utils.isFeasible

class BlindRandomSearch : IAlgorithm {
    override val name: String
        get() = "Blind Random search"

    override fun search(initSolution: Solution): Solution {
        var bestSolution = initSolution
        for (i in 0 until 10000){
            val currentSolution = generateRandomValidSolution(bestSolution.instance)
            if (isFeasible(currentSolution.instance, currentSolution.arr).first &&
                costFunction(currentSolution.instance, currentSolution.arr) < costFunction(bestSolution.instance, bestSolution.arr)){
                bestSolution = currentSolution
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