package utils

import Solution
import java.io.File
import java.util.Arrays
import java.util.stream.IntStream


fun parseInstance(filepath: String): Instance {
    val INDEXINGOFFSET = 1
    val file = File(filepath).readText().replace("\r", "").split("\n")

    val numberOfNodes = file[1].toInt()
    val numberOfVehicles = file[3].toInt()
    val numberOfCalls = file[numberOfVehicles + 5 + 1].toInt();


    // Parsing the vehicles
    val vehicles = arrayOfNulls<Vehicle>(numberOfVehicles);
    var offset1 = 5;
    var offset2 = 8 + numberOfVehicles;

    for (i in 0 until numberOfVehicles) {
        val vehicleInfo = file[offset1].split(",")
        val index = vehicleInfo[0].toInt() - INDEXINGOFFSET
        vehicles[index] = Vehicle(
            index,
            vehicleInfo[1].toInt() - INDEXINGOFFSET,
            vehicleInfo[2].toInt(),
            vehicleInfo[3].toInt()
        )

        var callsForThisVehicle = file[offset2]
            .split(",")
            .map { it.toInt() - INDEXINGOFFSET }
        callsForThisVehicle = callsForThisVehicle.subList(1, callsForThisVehicle.size)

        vehicles[index]?.possibleCalls = callsForThisVehicle

        offset1++
        offset2++
    }

    // Parse the calls
    val calls = arrayOfNulls<Call>(numberOfCalls);
    var offset = 9 + (2 * numberOfVehicles);
    for (i in 0 until numberOfCalls) {
        val givenCall = file[offset].split(",").map { it.toInt() }
        val index = givenCall[0] - INDEXINGOFFSET;
        calls[index] = Call(
            index,
            givenCall[1] - INDEXINGOFFSET,
            givenCall[2] - INDEXINGOFFSET,
            givenCall[3],
            givenCall[4],
            givenCall[5],
            givenCall[6],
            givenCall[7],
            givenCall[8]
        )
        offset++
    }

    // Parse travel times and cost
    val travelTimeAndCost = arrayOfNulls<Array<Array<TravelTimeAndCost?>?>>(numberOfVehicles)
    for (i in 0 until numberOfVehicles) {
        travelTimeAndCost[i] = arrayOfNulls(numberOfNodes)
        for (j in 0 until numberOfNodes) {
            travelTimeAndCost[i]?.set(j, arrayOfNulls(numberOfNodes));
        }
    }
    offset = 10 + (numberOfVehicles * 2) + numberOfCalls;

    for (i in 0 until (numberOfVehicles * numberOfNodes * numberOfNodes)) {
        val line = file[offset].split(",").map { it.toInt() }
        val vehicle = line[0] - INDEXINGOFFSET;
        val origin = line[1] - INDEXINGOFFSET;
        val destination = line[2] - INDEXINGOFFSET;
        travelTimeAndCost[vehicle]?.get(origin)?.set(
            destination, TravelTimeAndCost(
                line[3],
                line[4]
            )
        )
        offset++;
    }

    // Parse node times and cost
    val nodeTimeAndCost = arrayOfNulls<Array<NodeTimeAndCost?>>(numberOfVehicles);
    for (i in 0 until numberOfVehicles) {
        nodeTimeAndCost[i] = arrayOfNulls(numberOfCalls)
    }
    offset++;

    for (i in 0 until (numberOfVehicles * numberOfCalls)) {
        val line = file[offset].split(",").map { it.toInt() };
        val vehicle = line[0] - INDEXINGOFFSET
        val call = line[1] - INDEXINGOFFSET
        nodeTimeAndCost[vehicle]?.set(
            call, NodeTimeAndCost(
                line[2],
                line[3],
                line[4],
                line[5]
            )
        )
        offset++
    }

    return Instance(
        numberOfNodes,
        numberOfVehicles,
        numberOfCalls,
        vehicles,
        calls,
        travelTimeAndCost,
        nodeTimeAndCost
    )
}

fun isFeasible(instance: Instance, solution: MutableList<Int>): Pair<Boolean, String> {
    var startIndex = 0;
    val zeroIndices = getVehicleIndices(solution);
    for (i in 0 until instance.numberOfVehicles) {
        val vehicle = instance.vehicles[i]!!

        val route = solution.subList(startIndex, zeroIndices[i]) //TODO: remove solution.sliceArray(startIndex until zeroIndices[i])

        // 1. Check that the route is compatible with the vehicle
        for (call in route) {
            if (!vehicle.possibleCalls.contains(call)) {
                return Pair(false, "Incompatible vehicle and call")
            }
        }
        // 2. Check vehicle capacity
        var currentLoad = 0;
        var currentCalls = hashSetOf<Call>();
        for (node in route) {
            val call = instance.calls[node]!!
            if (call in currentCalls) {
                currentLoad -= call.size;
                continue;
            }
            currentCalls.add(call)
            currentLoad += call.size;

            if (currentLoad > vehicle.capacity) {
                return Pair(false, "utils.Vehicle capacity exceeded")
            }
        }

        // 3. Check time windows
        var currentNode = vehicle.homeNode;
        var currentTime = vehicle.startingTime;
        currentCalls = hashSetOf();
        for (c in route) {
            val call = instance.calls[c]!!
            if (call in currentCalls) {
                currentCalls.remove(call); // TODO: REMOVE?
                currentTime += instance.travelTimeAndCost[i]?.get(currentNode)?.get(call.destinationNode)?.time!!
                currentNode = call.destinationNode;

                if (currentTime > call.upperTimeDelivery) {
                    return Pair(false, "Delivery time exceeded")
                }
                if (currentTime < call.lowerTimeDelivery) {
                    currentTime = call.lowerTimeDelivery
                }
                currentTime += instance.nodeTimeAndCost[i]?.get(c)?.destinationTime!!
            } else {
                currentCalls.add(call)
                currentTime += instance.travelTimeAndCost[i]?.get(currentNode)?.get(call.originNode)?.time!!
                currentNode = call.originNode

                if (currentTime > call.upperTimePickup) {
                    return Pair(false, "Pickup time exceeded")
                }
                if (currentTime < call.lowerTimePickup) {
                    currentTime = call.lowerTimePickup
                }

                currentTime += instance.nodeTimeAndCost[i]?.get(c)?.originTime!!
            }
        }
        startIndex = zeroIndices[i] + 1
    }
    return Pair(true, "Feasible")
}

fun costFunction(instance: Instance, solution: MutableList<Int>): Int {
    val zeroIndices = getVehicleIndices(solution)
    val outSourced = solution.subList(zeroIndices[zeroIndices.size - 1] + 1, solution.size)
    val packetsOnVehicle = solution.subList(0, zeroIndices[zeroIndices.size - 1])

    var cost = 0;
    var startIndex = 0;

    for (v in 0 until instance.numberOfVehicles) {
        val route = packetsOnVehicle.slice(startIndex until zeroIndices[v])
        val startedCalls = arrayListOf<Int>();
        var previousNode = instance.vehicles[v]?.homeNode!!

        for (c in route) {
            val call = instance.calls[c]!!
            if (c in startedCalls) {
                cost += instance.nodeTimeAndCost[v]?.get(c)?.destinationCost!!
                cost += instance.travelTimeAndCost[v]?.get(previousNode)?.get(call.destinationNode)?.cost!!
                previousNode = call.destinationNode;
            } else {
                cost += instance.nodeTimeAndCost[v]?.get(c)?.originCost!!;
                cost += instance.travelTimeAndCost[v]?.get(previousNode)?.get(call.originNode)?.cost!!
                previousNode = call.originNode;
                startedCalls.add(c)
            }
        }
        startIndex = zeroIndices[v] + 1
    }
    // Cost of not delivering
    val notDelivered = hashSetOf<Int>()
    for (node in outSourced) {
        if (node !in notDelivered) {
            cost += instance.calls[node]?.costOfNotTransporting!!
            notDelivered.add(node)
        }
    }
    return cost
}

fun getVehicleIndices(array: MutableList<Int>): MutableList<Int> {
    val indices = arrayListOf<Int>();
    array.forEachIndexed { index, element ->
        if (element == -1) {
            indices.add(index);
        }
    }
    return indices;
}

fun costFunction(solution: Solution, v: Int): Int {
    val zeroIndices = getVehicleIndices(solution.arr)
    val packetsOnVehicle = solution.arr.subList(0, zeroIndices[zeroIndices.size - 1])
    val outSourced = solution.arr.subList(zeroIndices[zeroIndices.size - 1] + 1, solution.arr.size)

    val instance = solution.instance
    var cost = 0


    if (v == zeroIndices.size){
        val notDelivered = hashSetOf<Int>()
        for (node in outSourced) {
            if (node !in notDelivered) {
                cost += instance.calls[node]?.costOfNotTransporting!!
                notDelivered.add(node)
            }
        }
        return cost
    }

    val startIndex = if (v == 0) {
        0
    } else {
        zeroIndices[v-1] + 1
    }


    val route = packetsOnVehicle.slice(startIndex until zeroIndices[v])
    val startedCalls = arrayListOf<Int>()
    var previousNode = instance.vehicles[v]?.homeNode!!

    for (c in route) {
        val call = instance.calls[c]!!
        if (c in startedCalls) {
            cost += instance.nodeTimeAndCost[v]?.get(c)?.destinationCost!!
            cost += instance.travelTimeAndCost[v]?.get(previousNode)?.get(call.destinationNode)?.cost!!
            previousNode = call.destinationNode
        } else {
            cost += instance.nodeTimeAndCost[v]?.get(c)?.originCost!!
            cost += instance.travelTimeAndCost[v]?.get(previousNode)?.get(call.originNode)?.cost!!
            previousNode = call.originNode
            startedCalls.add(c)
        }
    }
    return cost
}

fun isFeasible(route: MutableList<Int>, instance: Instance, i: Int ) : Pair<Boolean, String>{
    val vehicle = instance.vehicles[i]!!

    // 1. Check that the route is compatible with the vehicle
    for (call in route) {
        if (!vehicle.possibleCalls.contains(call)) {
            return Pair(false, "Incompatible vehicle and call")
        }
    }
    // 2. Check vehicle capacity
    var currentLoad = 0;
    var currentCalls = hashSetOf<Call>();
    for (node in route) {
        val call = instance.calls[node]!!
        if (call in currentCalls) {
            currentLoad -= call.size;
            continue;
        }
        currentCalls.add(call)
        currentLoad += call.size;

        if (currentLoad > vehicle.capacity) {
            return Pair(false, "utils.Vehicle capacity exceeded")
        }
    }

    // 3. Check time windows
    var currentNode = vehicle.homeNode;
    var currentTime = vehicle.startingTime;
    currentCalls = hashSetOf();
    for (c in route) {
        val call = instance.calls[c]!!
        if (call in currentCalls) {
            currentCalls.remove(call); // TODO: REMOVE?
            currentTime += instance.travelTimeAndCost[i]?.get(currentNode)?.get(call.destinationNode)?.time!!
            currentNode = call.destinationNode;

            if (currentTime > call.upperTimeDelivery) {
                return Pair(false, "Delivery time exceeded")
            }
            if (currentTime < call.lowerTimeDelivery) {
                currentTime = call.lowerTimeDelivery
            }
            currentTime += instance.nodeTimeAndCost[i]?.get(c)?.destinationTime!!
        } else {
            currentCalls.add(call)
            currentTime += instance.travelTimeAndCost[i]?.get(currentNode)?.get(call.originNode)?.time!!
            currentNode = call.originNode

            if (currentTime > call.upperTimePickup) {
                return Pair(false, "Pickup time exceeded")
            }
            if (currentTime < call.lowerTimePickup) {
                currentTime = call.lowerTimePickup
            }

            currentTime += instance.nodeTimeAndCost[i]?.get(c)?.originTime!!
        }
    }
    return Pair(true, "Feasible")
}


