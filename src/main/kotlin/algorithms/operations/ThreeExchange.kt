package algorithms.operations

import Solution

class ThreeExchange: Operation() {
    override fun operation(solution: Solution): Solution {
        val listOfVehicles = solutionWithIndependentRoutes(solution)
        val chosenVehicle = listOfVehicles.random()

        if (chosenVehicle.size < 6){
            return solution
        }
        val chosen1 = chosenVehicle.random()
        val chosen2 = chosenVehicle.random()
        val chosen3 = chosenVehicle.random()

        if (chosen1 == chosen2 ||chosen1 == chosen3 || chosen2 == chosen3){
            return solution
        }

        var indexChosen11 = Int.MIN_VALUE
        var indexChosen12 = Int.MIN_VALUE
        var indexChosen21 = Int.MIN_VALUE
        var indexChosen22 = Int.MIN_VALUE
        var indexChosen31 = Int.MIN_VALUE
        var indexChosen32 = Int.MIN_VALUE

        solution.arr.forEachIndexed { index, element ->
            if(element == chosen1 && indexChosen11 == Int.MIN_VALUE){
                indexChosen11 = index
            } else if (element == chosen1){
                indexChosen12 = index
            }
            if (element == chosen3 && indexChosen31 == Int.MIN_VALUE){
                indexChosen31 = index
            } else if(element == chosen3) {
                indexChosen32 = index
            }

            if (element == chosen2 && indexChosen21 == Int.MIN_VALUE){
                indexChosen21 = index
            } else if (element == chosen2) {
                indexChosen22 = index
            }

        }
        val temp = solution.arr.toMutableList()
        temp[indexChosen11] = chosen3
        temp[indexChosen12] = chosen3
        temp[indexChosen21] = chosen1
        temp[indexChosen22] = chosen1
        temp[indexChosen31] = chosen2
        temp[indexChosen32] = chosen2

        return Solution(solution.instance, temp)
    }
}