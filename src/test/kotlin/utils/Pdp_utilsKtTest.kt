package utils

import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

internal class Pdp_utilsKtTest {
    lateinit var CALL_007_VEHICLE_03:Instance
    @BeforeEach
    fun setUp() {
        CALL_007_VEHICLE_03 = parseInstance("src/main/kotlin/data/Call_7_Vehicle_3.txt")
    }

    @Test
    fun isFeasible0() {
        val feasibleSolution = intArrayOf(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7).map { it-1 }.toIntArray()

        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction0() {
        val feasibleSolution = intArrayOf(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(3286422, cost)
    }


    @Test
    fun isFeasible1() {
        val feasibleSolution = intArrayOf(0, 0, -1, 6, 6, 2, 2, -1, 4, 4, 5, 5, -1, 3, 3, 1, 1)

        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction1() {
        val feasibleSolution = intArrayOf(0, 0, -1, 6, 6, 2, 2, -1, 4, 4, 5, 5, -1, 3, 3, 1, 1)
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(1477429, cost)
    }
    @Test
    fun isFeasible2() {
        val feasibleSolution = intArrayOf(0, 3, 3, 0, 1, 1, 0, 5, 6, 2, 7, 7, 6, 4, 2, 4, 5).map { it-1 }.toIntArray()
        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction2() {
        val feasibleSolution = intArrayOf(0, 3, 3, 0, 1, 1, 0, 5, 6, 2, 7, 7, 6, 4, 2, 4, 5).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(2672316, cost)
    }

    @Test
    fun isFeasible3() {
        val feasibleSolution = intArrayOf(3, 3, 0, 0, 7, 7, 1, 1, 0, 5, 4, 6, 2, 5, 6, 4, 2).map { it-1 }.toIntArray()
        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction3() {
        val feasibleSolution = intArrayOf(3, 3, 0, 0, 7, 7, 1, 1, 0, 5, 4, 6, 2, 5, 6, 4, 2).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(2346070, cost)
    }

    @Test
    fun isFeasible4() {
        val feasibleSolution = intArrayOf(7, 7, 0, 1, 1, 0, 5, 5, 6, 6, 0, 3, 2, 3, 4, 2, 4).map { it-1 }.toIntArray()
        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction4() {
        val feasibleSolution = intArrayOf(7, 7, 0, 1, 1, 0, 5, 5, 6, 6, 0, 3, 2, 3, 4, 2, 4).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(1617415, cost)
    }
    @Test
    fun isFeasible5() {
        val feasibleSolution = intArrayOf(0, 7, 7, 3, 3, 0, 5, 5, 0, 1, 4, 1, 2, 6, 2, 6, 4).map { it-1 }.toIntArray()
        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction5() {
        val feasibleSolution = intArrayOf(0, 7, 7, 3, 3, 0, 5, 5, 0, 1, 4, 1, 2, 6, 2, 6, 4).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(2478319, cost)
    }
    @Test
    fun isFeasible6() {
        val feasibleSolution = intArrayOf(1, 1, 0, 7, 7, 0, 2, 2, 0, 3, 4, 5, 6, 4, 5, 3, 6).map { it-1 }.toIntArray()
        val feasibility = utils.isFeasible(CALL_007_VEHICLE_03, feasibleSolution).first
        kotlin.test.assertTrue { feasibility }
    }


    @Test
    fun costFunction6() {
        val feasibleSolution = intArrayOf(1, 1, 0, 7, 7, 0, 2, 2, 0, 3, 4, 5, 6, 4, 5, 3, 6).map { it-1 }.toIntArray()
        val cost = costFunction(CALL_007_VEHICLE_03, feasibleSolution)
        kotlin.test.assertEquals(2166916, cost)
    }
}
