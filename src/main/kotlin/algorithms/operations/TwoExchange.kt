package algorithms.operations

import Solution

class TwoExchange: Operation() {


    override fun operation(solution: Solution): Solution {
        val listOfVehicles = solutionWithIndependentRoutes(solution)
        val chosenVehicle = listOfVehicles.random()
        if (chosenVehicle.size < 4){
            return solution
        }
        var indexChosen11 = Int.MIN_VALUE
        var indexChosen12 = Int.MIN_VALUE
        var indexChosen21 = Int.MIN_VALUE
        var indexChosen22 = Int.MIN_VALUE

        val chosen1 = chosenVehicle.random()
        val chosen2 = chosenVehicle.random()

        solution.arr.forEachIndexed { index, element ->
            if(element == chosen1 && indexChosen11 == Int.MIN_VALUE){
                indexChosen11 = index
            } else if (element == chosen1){
                indexChosen12 = index
            }

            if (element == chosen2 && indexChosen21 == Int.MIN_VALUE){
                indexChosen21 = index
            } else if (element == chosen2) {
                indexChosen22 = index
            }
        }
        val temp = solution.arr.toMutableList()
        temp[indexChosen11] = chosen2
        temp[indexChosen12] = chosen2
        temp[indexChosen21] = chosen1
        temp[indexChosen22] = chosen1

        return Solution(solution.instance, temp)
    }
}