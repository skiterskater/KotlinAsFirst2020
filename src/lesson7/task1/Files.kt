@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import lesson3.task1.digitNumber
import lesson3.task1.revert
import kotlin.math.*
import java.io.File
import java.util.*
import ru.spbstu.wheels.stack
import java.io.File
import java.lang.IllegalArgumentException

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use { writer ->
        File(inputName).forEachLine { newLine ->
            if (!newLine.startsWith("_")) writer.write("$newLine\n")
        }
    }
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val text = File(inputName).readText().lowercase()
    val res = mutableMapOf<String, Int>()
    for (substring in substrings)
        res[substring] = countSubstringsWithCross(text, substring.lowercase())
    return res
}
//функция считающая вхождение строк с пересечением(если начало i-ой строки содержится к конце (i-1)-ой строки
fun countSubstringsWithCross(text: String, substring: String): Int {
    return if (text.contains(substring)) {
        var count = 0
        var i = text.indexOf(substring)
        while (i != -1) {
            i = text.indexOf(substring, i + 1)
            count++
        }
        count
    } else 0
}

/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val correct = mapOf('ю' to 'у', 'ы' to 'и', 'я' to 'а')
    val flagLetters = listOf('щ', 'ш', 'ч', 'ж')
    File(outputName).bufferedWriter().use { writer ->
        File(inputName).forEachLine { line ->
            val result = StringBuilder(line[0].toString())
            for (i in 1 until line.length) {
                val letter = line[i]
                val letterLow = line[i].lowercaseChar()
                if (letterLow in correct && line[i - 1].lowercaseChar() in flagLetters)
                    result.append(if (letter.isLowerCase()) correct[letterLow]!! else correct[letterLow]!!.uppercaseChar())
                else result.append(letter)
            }
            writer.write("$result\n")
        }
    }
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val lines = File(inputName).readLines().map { it.trim() }
    val length = if (lines.isNotEmpty()) lines.maxOf { it.length } else 0
    File(outputName).bufferedWriter().use { writer ->
        lines.forEach { line ->
            val indent = (length - line.length) / 2
            writer.write(" ".repeat(indent) + line + "\n")
        }
    }
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> = TODO()

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val dictionaryLow = dictionary.map { it.key.lowercaseChar() to it.value.lowercase() }.toMap()
    val writer = File(outputName).writer()
    val lines = File(inputName).readLines()
    for (line in lines) {
        var newLine = buildString { }
        for (i in line.indices) {
            newLine += if (line[i].lowercaseChar() in dictionaryLow.keys && !line[i].isUpperCase()) dictionaryLow[line[i].lowercaseChar()]
            else if (line[i].lowercaseChar() in dictionaryLow.keys && line[i].isUpperCase()) dictionaryLow[line[i].lowercaseChar()]?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
                // строчка выше получилась из newLine.capitalize
            }
            else line[i]
        }
        writer.write("$newLine\n")
    }
    writer.close()
}


/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    var maxLength = 0
    val result = mutableListOf<String>()
    for (line in File(inputName).readLines()) {
        if (line.lowercase().toSet().size == line.length) {
            if (line.length > maxLength) {
                maxLength = line.length
                result.clear()
            } else if (line.length < maxLength) continue
            result.add(line)
        }
    }
    File(outputName).writeText(result.joinToString(separator = ", "))
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val startText = File(inputName).readText()
    val toReturn = File(outputName).bufferedWriter()

    val replaceBold = Regex("\\*\\*([\\s\\S]*?)\\*\\*").replace(startText) { char ->
        "<b>" + char.value.replace("**", "") + "</b>"
    }
    val replaceItalics = Regex("\\*([\\s\\S]*?)\\*").replace(replaceBold) { char ->
        "<i>" + char.value.replace("*", "") + "</i>"
    }
    val replaceStrikethrough = Regex("~~([\\s\\S]*?)~~").replace(replaceItalics) { char ->
        "<s>" + char.value.replace("~~", "") + "</s>"
    }

    val textWithTags = replaceStrikethrough
    var linesCheckedInParagraph = 0
    val lines = textWithTags.split("\n").toMutableList()
    for (idx in lines.indices) {
        val singleLine = lines[idx]
        if (singleLine.trim().isEmpty()) {
            if (linesCheckedInParagraph >= 1) {
                if (idx + 1 < lines.size && lines[idx + 1].trim().isNotEmpty()) {
                    lines[idx] = "</p><p>"
                    linesCheckedInParagraph = 0
                }
            }
        } else {
            linesCheckedInParagraph += 1
        }
    }

    toReturn.write("<html><body><p>${lines.joinToString(separator = "")}</p></body></html>")
    toReturn.close()
}


/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val toReturn = File(outputName).bufferedWriter()
    val builder = StringBuilder("<html><body><p>")

    val digits = ".0123456789"
    val currentStack = mutableListOf<String>()

// Для первой строчки
    var idx = 0
    if (lines.isNotEmpty()) {
        if (lines[0][idx] == '*') {
            currentStack.add("ul")
        } else currentStack.add("ol")

        while (lines[0][idx] == ' ' || lines[0][idx] == '*' || lines[0][idx] in digits) idx += 1
        builder.append("<${currentStack.last()}><li>${lines[0].substring(idx)}")

// для последующих строчек
        for (j in 1 until lines.size) {
            val currentLine = lines[j]
            if (currentLine.trim().isEmpty()) continue
            var c = 0
            while (currentLine[c] == ' ') c += 1
            if (c > 4 * (currentStack.size - 1)) {
                if (currentLine[c] in digits) {
                    builder.append("<ol>")
                    currentStack.add("ol")
                } else if (currentLine[c] == '*') {
                    builder.append("<ul>")
                    currentStack.add("ul")
                } else throw IllegalArgumentException()
            } else if (c < 4 * (currentStack.size - 1)) {
                builder.append("</li></${currentStack.removeLast()}></li>")
            } else {
                builder.append("</li>")
            }

            while (currentLine[c] == ' ' || currentLine[c] == '*' || currentLine[c] == '.' || currentLine[c] in digits) c += 1
            builder.append("<li>${currentLine.substring(c)}")
        }

        while (currentStack.size > 0) builder.append("</li></${currentStack.removeLast()}>")
    }

    builder.append("</p></body></html>")
    toReturn.write("$builder")
    toReturn.close()
}


/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).writer()
    val count = max(digitNumber(lhv) + digitNumber(rhv), digitNumber(rhv * lhv) + 1)
    writer.write(
        "${newString(count - digitNumber(lhv), ' ')}$lhv\n*${
            newString(
                count - digitNumber(rhv) - 1,
                ' '
            )
        }$rhv\n"
    )
    writer.write(
        "${newString(count, '-')}\n${
            newString(
                count - digitNumber((rhv % 10) * lhv),
                ' '
            )
        }${(rhv % 10) * lhv}\n"
    )
    var second = rhv / 10
    var countT = 2 // учитывает знак + и переход
    while (second != 0) {
        writer.write("+${newString(count - digitNumber((second % 10) * lhv) - countT, ' ')}${(second % 10) * lhv}\n")
        second /= 10
        countT++
    }
    writer.write("${newString(count, '-')}\n${newString(count - digitNumber(lhv * rhv), ' ')}${lhv * rhv}")
    writer.close()
}
// функция делающая строку из n одинаковых символов
fun newString(n: Int, symbol: Char): String {
    var newLine = ""
    var count = n
    while (count > 0) {
        newLine += symbol
        count--
    }
    return newLine
}

/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).writer()
    val result = lhv / rhv
    var revResult = result.toString().reversed().toInt()
    var count = digitNumber(lhv) + 4
    var countT = digitNumber(rhv * (revResult % 10)) + 1
    var right = countT
    var left = 0
    var n = digitNumber(result) - 1
    var main = needDigit(left, right, lhv)
    var minus: Int
    if (lhv < rhv && digitNumber(lhv) != 1) {
        writer.write("$lhv | $rhv\n")
        writer.write("${newString(digitNumber(lhv) - 2, ' ')}-0   0\n")
        writer.write("${newString(max(digitNumber(lhv), 2), '-')}\n$lhv")
        writer.close()
    } else {
        if (rhv * (revResult % 10) > needDigit(left, right - 1, lhv)) {
            writer.write("$lhv | $rhv\n")
            count--
            main = needDigit(left, right, lhv)
            right--
            minus = main - rhv * (revResult % 10)
            writer.write(
                "-${rhv * (revResult % 10)}${
                    newString(
                        count - digitNumber(rhv * (revResult % 10)) - 1,
                        ' '
                    )
                }$result\n"
            )
        } else {
            minus = main - rhv * (revResult % 10) * 10
            writer.write(" $lhv | $rhv\n")
            writer.write(
                "-${rhv * (revResult % 10)}${
                    newString(
                        count - digitNumber(rhv * (revResult % 10)) - 1,
                        ' '
                    )
                }$result\n"
            )
        }
        writer.write("${newString(countT, '-')}\n")
        if (digitNumber(result) == 1) {
            writer.write("${newString(countT - digitNumber(lhv - result * rhv), ' ')}${lhv - result * rhv}")
            writer.close()
        } else {
            while (n > 0) {
                revResult /= 10
                if (minus < revResult % 10 * rhv) {
                    minus = minus * 10 + needDigit(right, right + 1, lhv)
                }
                var countX = max((digitNumber(rhv * (revResult % 10)) + 1), digitNumber(minus))
                if (digitNumber(minus) == 1) {
                    writer.write(
                        "${
                            newString(
                                countT - digitNumber(minus),
                                ' '
                            )
                        }0$minus\n"
                    )
                    countX = max(digitNumber(rhv * (revResult % 10)), digitNumber(minus) + 1)
                } else writer.write("${newString(countT - digitNumber(minus) + 1, ' ')}$minus\n")
                writer.write(
                    "${
                        newString(
                            countT - digitNumber(rhv * (revResult % 10)),
                            ' '
                        )
                    }-${rhv * (revResult % 10)}\n"
                )
                writer.write("${newString(right + 1 - countX, ' ')}${newString(countX, '-')}\n")
                left = right
                right++
                if (n == 1) {
                    writer.write(
                        "${
                            newString(
                                count - digitNumber(minus - rhv * (revResult % 10)) - 3,
                                ' '
                            )
                        }${minus - rhv * (revResult % 10)}"
                    )
                }
                minus = needDigit(left, right, lhv) + (minus - rhv * (revResult % 10)) * 10
                countT++
                n--
            }
            writer.close()
        }
    }
}
// функция считающая нужное значение цифр числа в заданном диапазоне цифр
fun needDigit(left: Int, right: Int, n: Int): Int {
    val res = n.toString()
    return if (right > res.length) res.last().toInt() else res.substring(left, right).toInt()
}
