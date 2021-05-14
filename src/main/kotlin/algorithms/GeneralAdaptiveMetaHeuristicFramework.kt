package algorithms

import Solution
import algorithms.operations.RemoveMostExpensiveFromDummy
import algorithms.operations.escape.EscapeThisHell
import algorithms.operations.escape.MoveAllCallsInVehicleToDummy
import algorithms.operations.escape.MoveNToDummy
import algorithms.operations.forExam.RandomRemovalGreedyInsertion
import algorithms.operations.forExam.RelationRemovalInsertion
import algorithms.operations.forExam.WorstRemovalGreedyInsertion
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random
import utils.costFunction
import utils.isFeasible

class GeneralAdaptiveMetaHeuristicFramework : IAlgorithm {
    override val name: String
        get() = "For EXAM"

    override fun search(initSolution: Solution, timeConstraint: Double): Solution {
        var temperature = 40.0
        var alpha = 0.998

        val instance = initSolution.instance
        var iterationsSinceBest = 0

        var p1 = 0.5
        var p2 = 0.0
        var p3 = 0.0
        // p4 is 1 - p1 - p2 - p3

        val uniqueSols = hashSetOf<MutableList<Int>>()

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

        var newSolution = initSolution
        var feasibilityOfNewSolution: Boolean //= isFeasible(instance, newSolution.arr)
        var costOfNewSolution: Int //= costFunction(instance, newSolution.arr)
        var deltaE: Int

        val timeForLocalSearch = 0.01
        val endTime = System.currentTimeMillis() + (timeConstraint * 1000L * (1 - timeForLocalSearch))


        var i = 0
        var acceptionProbability = 0.8
        val firstHundredDeltas = ArrayList<Int>(100)
        var `average of first 100 deltas`: Int
        val temperatureFinal = 0.01
        var startTime = System.currentTimeMillis()

        println("Starting ${instance.numberOfCalls} calls ${instance.numberOfVehicles}")

        while (startTime < endTime) {
            var bestSoulThisRound = false
            if (iterationsSinceBest % 200 == 0) {
                incumbent = MoveAllCallsInVehicleToDummy().operation(incumbent) //EscapeThisHell().operation(incumbent)
            }

            var random = Random.nextFloat()
            var chosen = -1
            newSolution = when {
                random < p1 -> {
                    timesOperationOneCalled += 1
                    chosen = OPERATIONONE
                    RandomRemovalGreedyInsertion().operation(incumbent)
                }
/*
                random < p1 + p2 -> {
                    timesOperationTwoCalled += 1
                    chosen = OPERATIONTWO
                    RemoveMostExpensiveFromDummy().operation(incumbent)
                }

                random < p1 + p2 + p3 -> {
                    timesOperationThreeCalled += 1
                    chosen = OPERATIONTHREE
                    OneReinsert().operation(incumbent)
                }
                 */
                else -> { // p4
                    chosen = OPERATIONFOUR
                    timesOperationFourCalled += 1
                    WorstRemovalGreedyInsertion().operation(incumbent)
                }
            }


            feasibilityOfNewSolution = isFeasible(instance, newSolution.arr).first
            costOfNewSolution = costFunction(instance, newSolution.arr)
            if (feasibilityOfNewSolution && costOfNewSolution < costFunction(instance, bestSolution.arr)) {
                bestSoulThisRound = true
                bestSolution = newSolution
                incumbent = newSolution
                iterationsSinceBest = 0
                when (chosen) {
                    OPERATIONONE -> pointsForOperationOne += 4
                    //OPERATIONTWO -> pointsForOperationTwo += 4
                    //OPERATIONTHREE -> pointsForOperationThree += 4
                    OPERATIONFOUR -> pointsForOperationFour += 4
                    else -> kotlin.error("Something is wrong; there was no operation called but a better solution was made")
                }
            } else if (feasibilityOfNewSolution && costOfNewSolution < costFunction(instance, incumbent.arr)) {
                when (chosen) {
                    OPERATIONONE -> pointsForOperationOne += 2
                    //OPERATIONTWO -> pointsForOperationTwo += 2
                    //OPERATIONTHREE -> pointsForOperationThree += 2
                    OPERATIONFOUR -> pointsForOperationFour += 2
                    else -> kotlin.error("Something is wrong; new solution better then incumbent, but no operation was called")
                }
            } else if (feasibilityOfNewSolution && !uniqueSols.contains(newSolution.arr)){
                uniqueSols.add(newSolution.arr)
                when (chosen) {
                    OPERATIONONE -> pointsForOperationOne += 1
                    //OPERATIONTWO -> pointsForOperationTwo += 1
                    //OPERATIONTHREE -> pointsForOperationThree += 1
                    OPERATIONFOUR -> pointsForOperationFour += 1
                    else -> kotlin.error("Something is wrong;")
                }
            }


            if (i == 99) {
                `average of first 100 deltas` = firstHundredDeltas.sum().div(firstHundredDeltas.size)
                val time = System.currentTimeMillis()
                val timePerIterator = (time - startTime).div(100)
                val totalIterations = (endTime - time).div(timePerIterator)
                temperature = findInitTemp(`average of first 100 deltas`)
                alpha = getAlpha(temperatureFinal, temperature, totalIterations.toInt())
            }
            else if (i < 100 && !bestSoulThisRound) {
                firstHundredDeltas.add(costOfNewSolution - costFunction(instance, incumbent.arr))
            }
            deltaE = costOfNewSolution - costFunction(instance, incumbent.arr)
            random = Random.nextFloat()
            if (feasibilityOfNewSolution && random < exp(-(deltaE).toFloat() / temperature)) {
                incumbent = newSolution
            }
            iterationsSinceBest += 1
            temperature *= alpha

            if (i % 200 == 0) {
                val averageA = pointsForOperationFour.div(timesOperationOneCalled);
                //val averageB = pointsForOperationTwo.div(timesOperationTwoCalled)
                //val averageC = pointsForOperationThree.div(timesOperationThreeCalled)
                val averageD = pointsForOperationFour.div(timesOperationFourCalled)

                val avgSum = averageA + averageD

                p1 = averageA.div(avgSum)
                //p2 = averageB.div(avgSum)
                //p3 = averageC.div(avgSum)

                pointsForOperationOne = 1.0;

                pointsForOperationFour = 1.0
                timesOperationOneCalled = 1.0;
                timesOperationFourCalled = 1.0
                // println(String.format("%.3f \t%.3f \t%.3f \t%.3f", p1, p2, p3, (1 - p1 - p2 - p3)))
            }
            i++
            startTime = System.currentTimeMillis()
        }
        bestSolution = LocalSearch().search(bestSolution, timeConstraint*timeForLocalSearch)
        return bestSolution
    }

    private fun  findInitTemp(delta: Int): Double {
        return -delta / ln(0.8)
    }

    private fun getAlpha(T_F: Double, T_0: Double, n: Int): Double {
        return (T_F / T_0).pow(1.0 / n)
    }
}