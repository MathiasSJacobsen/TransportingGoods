package algorithms.opeartions

import Solution

class OneReinsert(): Operation() {

    override fun operation(solution: Solution): Solution {
        val listOfVehicles = solutionWithIndependentRoutes(solution)
        val numberOfVehicle = solution.instance.numberOfVehicles
        val random = (0..numberOfVehicle).random()

        val chosenVehicle = listOfVehicles[random]

        if (chosenVehicle.size < 2){
            return solution
        }

        val chosen = chosenVehicle.random()
        val reinsertList = chosenVehicle.toList().filter { packet -> packet != chosen }
        listOfVehicles[random] = reinsertList.toMutableList()

        // TODO: Might wanna find a way to not reinsert to the same
        val random2 = (0..numberOfVehicle).random()
        val whereToReinsert = listOfVehicles[random2]
        val lengthOfWhereToReinsert = whereToReinsert.size
        val r = (0..lengthOfWhereToReinsert).random()
        val r2 = (0..lengthOfWhereToReinsert).random()


        whereToReinsert.add(r, chosen)
        whereToReinsert.add(r2, chosen)
        listOfVehicles[random2] = whereToReinsert

        val finalSolution = mutableListOf<Int>()
        for (e in listOfVehicles){
            finalSolution.addAll(e)
            finalSolution.add(-1)
        }
        finalSolution.removeAt(finalSolution.lastIndex)

        return Solution(solution.instance, finalSolution.toIntArray());
    }
}