package game

import android.support.v4.os.IResultReceiver._Parcel

fun main() {
    val game = Game()
    game.play()
}

class Game {
    private var bottomRange:Int = 0
    private var topRange:Int = 999
    var numInput: Array<Int?> = Array(10) { null }

    fun play() {
        do {
            resetInputArray()
            if (setRange()) {
                if (printRules()){
                    if (playRounds()) {
                        println("You won!!")
                    }
                    else {
                        println("Game over")
                    }
                }
                else {
                    println("game quit after rules print")
                }
            }
            else {
                println("game quit when range set")
            }
        } while (askToPlayAgain())
        println("Game ended")
    }

    fun resetInputArray() {
        //resets array when new game starts
        numInput.fill(null)
    }

    fun askToPlayAgain(): Boolean {
        println("Do you want to play again? (y/n)")
        val response = readLine()
        return response == "y" || response == "Y"
    }

    private fun playRounds():Boolean {
        var lost:Boolean = false
        while (!lost) {
            for (i in 0..9){
                val num = (bottomRange..topRange).random()
                if (!checkIfPlaceable(num)) {
                    //checks if random dice roll can even be placed, ends the game if it can't
                    printArray(10 - i, num)
                    println("Your next number is $num, which is not placeable")
                    return false
                }
                while (true) {
                    printArray(10-i, num)
                    println("Choose a ranking (1-10) or 'q' to quit:")
                    var input = readlnOrNull()
                    when {
                        input == null -> println("Invalid input")
                        input == "q" -> return false
                        input.isEmpty() -> println("Input cannot be empty")
                        else -> {
                            try {
                                val ranking = input.toInt()
                                if (ranking in 1..10) {
                                    if (!placeValue(ranking, num)) {
                                        return false
                                    }
                                    break
                                } else {
                                    println("Ranking must be between 1 an 10")
                                }
                            } catch (e: NumberFormatException) {
                                println("Invalid input")
                            }
                        }
                    }
                }
            }
            return true
        }
        return true
    }

    //checks if the random number generated is placeable in the array
    private fun checkIfPlaceable(currNum: Int): Boolean {
        if (numInput.all { it == null }) {
            return true
        }
        for (i in numInput.indices){
            if (numInput[i] != null) {
                if (currNum == numInput[i]) {
                    val index = numInput.indexOf(currNum)
                    for (j in index .. 9) {
                        if (numInput[j] == null) {
                            return true
                        }
                        if (numInput[j] != currNum) {
                            break
                        }
                    }
                    for (j in index downTo 0) {
                        if (j == 0) {
                            return false
                        }
                        if (numInput[j-1] == null) {
                            return true
                        }
                    }
                    return false
                }
                if (currNum < numInput[i]!!) {
                    if (i == 0) {
                        return false
                    }
                    else if (numInput[i-1] == null) {
                        return true
                    }
                    else {
                        for (j in i-1 downTo 0) {
                            if (numInput[j]!! < currNum) {
                                return false
                            }
                        }
                    }
                }
                if (numInput[9] != null) {
                    if (currNum > numInput[9]!!) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun checkForLoss():Boolean {
        var prev:Int? = null
        var ascending = true
        for (element in numInput){
            if (element != null) {
                if (prev == null) {
                    prev = element
                }
                else if (element < prev) {
                    ascending = false
                    break
                }
                prev = element
            }
        }
        return !ascending
    }

    private fun placeValue(index:Int, currNum: Int):Boolean {
        var ind = index
        while (true) {
            if (numInput[ind - 1] != null) {
                println("Spot $ind is taken, enter a different spot (or 'q' to quit)")
                var input = readlnOrNull()
                when {
                    input == null -> println("Invalid input")
                    input == "q" -> return false
                    input.isEmpty() -> println("Input cannot be empty")
                    else -> {
                        try {
                            val newRanking = input.toInt()
                            if (newRanking in 1..10) {
                                ind = newRanking
                            } else {
                                println("Ranking must be between 1 an 10")
                            }
                        } catch (e: NumberFormatException) {
                            println("Invalid input")
                        }
                    }
                }
            }
            else {
                numInput[ind-1] = currNum
                if (checkForLoss()) {
                    println("Numbers weren't in ascending order try again")
                    numInput[ind-1] = null
                    do {
                        println("Choose a ranking (1-10):")
                        var input = readlnOrNull()
                        when {
                            input == null -> println("Invalid input")
                            input == "q" -> return false
                            input.isEmpty() -> println("Input cannot be empty")
                            else -> {
                                try {
                                    val ranking = input.toInt()
                                    if (ranking in 1..10) {
                                        if (!placeValue(ranking, currNum)) {
                                            return false
                                        }
                                        break
                                    } else {
                                        println("Ranking must be between 1 an 10")
                                    }
                                } catch (e: NumberFormatException) {
                                    println("Invalid input")
                                }
                            }
                        }
                    } while (!(placeValue(index, currNum)))
                }
                return true
            }
        }
    }

    private fun printArray(remaining:Int, currNum:Int) {
        println("Remaining placements: $remaining")
        println("Generated number: $currNum")
        for (i in numInput.indices) {
            if (numInput[i] != null) {
                print("Rank ")
                print(i+1)
                println(": " + numInput[i])
            }
            else {
                print("Rank ")
                print(i+1)
                println(": -")
            }
        }
    }

    private fun printRules(): Boolean {
        println("Rules: 10 random numbers between $bottomRange and $topRange will be generated. Your goal is to place the randomly generated numbers in ascending rank order with rank 1 being the lowest and rank 10 being the highest number.")
        println()
        println("You must rank each number as it is generated and numbers cannot change rank after being placed.")
        println()
        println("Numbers can only be placed in an unranked spot and between numbers above/below its value in ascending rank order.")
        println()
        println("In the example below, the randomly generated number (576) MUST be placed in rank 3:")
        println()
        println("Remaining placements: 9")
        println("Generated number: 576")
        println("Rank 1: -")
        println("Rank 2: 500")
        println("Rank 3: -")
        println()
        println("The game is over when either a) 10 randomly generated numbers have been correctly placed or b) there are no more valid ranks to place the last randomly generated number.")
        println()
        println("Good luck!")
        println("Enter any key to accept the rules or 'q' to quit")
        val trash = readLine()
        if (trash == "q") {
            return false
        }
        else {
            return true
        }
    }

    private fun setRange(): Boolean {
        println("Welcome to the random number rank game!")
        val message = "If you would like to play with the default number range (0 to 999), press enter. If not, enter the bottom number of the range you would like "
        val bRange = getBottomRange(message)
        if (bRange == null) {
            return false
        }
        bottomRange = bRange

        val message1 = "If you would like to play with the default number range (0 to 999), press enter. If not, enter the top number of the range you would like "
        val tRange = getTopRange(message1)
        if (tRange == null) {
            return false
        }
        topRange = tRange
        return true
    }

    private fun getBottomRange(prompt: String): Int? {
        while (true) {
            println("$prompt (or enter 'q' to quit)")
            val input = readLine()
            if (input != null) {
                when {
                    input == "q" -> return null
                    input.isEmpty() -> return 0
                    else -> {
                        try {
                            return input.toInt()
                        } catch (e: NumberFormatException) {
                            println("Invalid entry. Please enter a valid integer or 'q' to quit.")
                        }
                    }
                }
            }
        }
    }
    private fun getTopRange(prompt: String): Int? {
        while (true) {
            println("$prompt (or enter 'q' to quit)")
            val input = readLine()
            if (input != null) {
                when {
                    input == "q" -> return null
                    input.isEmpty() -> return 999
                    else -> {
                        try {
                            return input.toInt()
                        } catch (e: NumberFormatException) {
                            println("Invalid entry. Please enter a valid integer or 'q' to quit.")
                        }
                    }
                }
            }
        }
    }
}