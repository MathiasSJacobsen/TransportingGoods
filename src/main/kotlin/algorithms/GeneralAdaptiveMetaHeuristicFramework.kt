package algorithms

import Solution
import algorithms.operations.BruteForce
import algorithms.operations.K_Reinsert
import algorithms.operations.LeastWaitTime
import algorithms.operations.OneReinsert
import algorithms.operations.RemoveMostExpensiveFromDummy
import algorithms.operations.ThreeExchange
import algorithms.operations.escape.MoveAllCallsInVehicleToDummy
import java.time.LocalTime
import kotlin.math.exp
import kotlin.random.Random
import utils.costFunction
import utils.isFeasible

class GeneralAdaptiveMetaHeuristicFramework : IAlgorithm {

    override val name: String
        get() = "GAMHF"


    override fun search(initSolution: Solution, timeConstraint: Int): Solution {

        var temperature = 40.0
        val coolingFactor = 0.998

        val instance = initSolution.instance
        var iterationsSinceBest = 0

        var p1 = 0.25
        var p2 = 0.25
        var p3 = 0.25
        // p4 is 1 - p1 - p2 - p3

        var timesOperationOneCalled = 1.0
        var timesOperationTwoCalled = 1.0
        var timesOperationThreeCalled = 1.0
        var timesOperationFourCalled = 1.0

        var pointsForOperationOne = 1.0
        var pointsForOperationTwo = 1.0
        var pointsForOperationThree = 1.0
        var pointsForOperationFour = 1.0

        val OPERATIONONE = 1
        val OPERATIONTWO = 2
        val OPERATIONTHREE = 3
        val OPERATIONFOUR = 4


        var incumbent = initSolution
        var bestSolution = initSolution


        //
        var newSolution = initSolution
        var feasibilityOfNewSolution:Boolean //= isFeasible(instance, newSolution.arr)
        var costOfNewSolution :Int //= costFunction(instance, newSolution.arr)
        var deltaE : Int

        val min = timeConstraint / 60
        val sec = timeConstraint - min * 60
        val addon = LocalTime.of(0, min, sec)
        val end = LocalTime.now().plusMinutes(addon.minute.toLong()).plusSeconds(addon.second.toLong())
        println("This will end: $end ")

        while (LocalTime.now() < end) {
        //for (i in 0..1000 ){
            //TODO: escape condition
            if (iterationsSinceBest == 2500) {
                incumbent = MoveAllCallsInVehicleToDummy().operation(incumbent)
            }

            var random = Random.nextFloat()
            var chosen = -1
            newSolution = when {
                random < p1 -> {
                    timesOperationOneCalled += 1
                    chosen = OPERATIONONE
                    BruteForce().operation(incumbent)
                }
                random < p1 + p2 -> {
                    timesOperationTwoCalled += 1
                    chosen = OPERATIONTWO
                    ThreeExchange().operation(incumbent)
                }
                random < p1 + p2 + p3 -> {
                    timesOperationThreeCalled += 1
                    chosen = OPERATIONTHREE
                    OneReinsert().operation(incumbent)
                }
                else -> { // p4
                    chosen = OPERATIONFOUR
                    timesOperationFourCalled += 1
                    RemoveMostExpensiveFromDummy().operation(incumbent)
                }
            }


            feasibilityOfNewSolution = isFeasible(instance, newSolution.arr).first
            costOfNewSolution = costFunction(instance, newSolution.arr)
            if (feasibilityOfNewSolution && costOfNewSolution < costFunction(instance, bestSolution.arr)) {
                bestSolution = newSolution
                incumbent = newSolution
                iterationsSinceBest = 0
                when (chosen) {
                    OPERATIONONE -> pointsForOperationOne += 4
                    OPERATIONTWO -> pointsForOperationTwo += 4
                    OPERATIONTHREE -> pointsForOperationThree += 4
                    OPERATIONFOUR -> pointsForOperationFour += 4
                    else -> kotlin.error("Something is wrong; there was no operation called but a better solution was made")
                }
            } else if (feasibilityOfNewSolution && costOfNewSolution < costFunction(instance, incumbent.arr)) {
                when (chosen) {
                    OPERATIONONE -> pointsForOperationOne += 2
                    OPERATIONTWO -> pointsForOperationTwo += 2
                    OPERATIONTHREE -> pointsForOperationThree += 2
                    OPERATIONFOUR -> pointsForOperationFour += 2
                    else -> kotlin.error("Something is wrong; new solution better then incumbent, but no operation was called")
                }
            } // TODO: if not explored

            deltaE = costOfNewSolution - costFunction(instance, incumbent.arr)
            random = Random.nextFloat()
            if (feasibilityOfNewSolution && random < exp(-(deltaE).toFloat() / temperature)) {
                incumbent = newSolution
            }
            iterationsSinceBest += 1
            temperature *= coolingFactor

/*
            if (i % 10000 == 0) {
                val averageA = pointsForOperationFour.div(timesOperationOneCalled);
                val averageB = pointsForOperationTwo.div(timesOperationTwoCalled)
                val averageC = pointsForOperationThree.div(timesOperationThreeCalled)
                val averageD = pointsForOperationFour.div(timesOperationFourCalled)

                val avgSum = averageA + averageB + averageC + averageD

                p1 = averageA.div(avgSum)
                p2 = averageB.div(avgSum)
                p3 = averageC.div(avgSum)

                pointsForOperationOne = 1.0; pointsForOperationTwo = 1.0; pointsForOperationThree =
                    1.0; pointsForOperationFour = 1.0
                timesOperationOneCalled = 1.0;timesOperationTwoCalled = 1.0;timesOperationThreeCalled =
                    1.0; timesOperationFourCalled = 1.0
                println(String.format("%.3f \t%.3f \t%.3f \t%.3f", p1, p2, p3, (1-p1-p2-p3)))
            }
            if (i % 1000 ==  0) {
                println(i)
            }

 */
        }
        return bestSolution
    }


}