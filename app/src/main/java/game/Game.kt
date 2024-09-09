package game

class Game {
    private var bottomRange:Int = 0
    private var topRange:Int = 999
    var numInput: Array<Int?> = Array(10) { null }

    fun play() {
        setRange()
        printRules()
        if (playRounds()) {
            println("game won")
        }
        else {
            println("game lost")
        }
    }

    private fun playRounds():Boolean {
        var lost:Boolean = false
        while (!lost) {
            for (i in 0..9){
                val num = (bottomRange..topRange).random()
                if (checkForLoss()) {
                    println("numbers weren't in ascending order")
                    printArray(10 - i, num)
                    return false
                }
                if (!checkIfPlaceable(num)) {
                    printArray(10 - i, num)
                    println("Your next number is $num, which is not placeable")
                    return false
                }
                while (true) {
                    printArray(10-i, num)
                    println("Choose a ranking (1-10) for 'q' to quit:")
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

    private fun checkIfPlaceable(currNum: Int): Boolean {
        for (i in 0 until 10) {
            val curr = numInput[i]
            if (curr != null) {
                if (currNum > curr){
                    for (j in i+1 .. 9) {
                        if (numInput[j] == null) {
                            return true
                        }
                        else if (numInput[j]!! > currNum){
                            return false
                        }
                    }
                }
                else {
                    if (i == 0) {
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

    private fun printRules() {
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
        println("Rank 1: 500")
        println("Rank 1: -")
        println()
        println("The game is over when either a) 10 randomly generated numbers have been correctly placed or b) there are no more valid ranks to place the last randomly generated number.")
        println()
        println("Good luck!")
        println("Enter any key to accept the rules")
        val trash = readLine()
    }

    private fun setRange() {
        println("Welcome to the random number rank game!")
        val message = "If you would like to play with the default number range (0 to 999), press enter. If not, enter the bottom number of the range you would like "
        val bRange = getBottomRange(message)
        if (bRange == null) {
            println("Game ended")
            return
        }
        bottomRange = bRange

        val message1 = "If you would like to play with the default number range (0 to 999), press enter. If not, enter the top number of the range you would like "
        val tRange = getTopRange(message1)
        if (tRange == null) {
            println("Game ended")
            return
        }
        topRange = tRange
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