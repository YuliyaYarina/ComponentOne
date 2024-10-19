# Компонент 1. "Виртуальный регулятор температуры".
_ 
Компонент необходимо реализовать как отдельную библиотеку (regulator.jar), 
которая будет в дальнейшем подключена как зависимость для проекта – сервер. 
Для взаимодействия с "регулятором температуры" в библиотеке должен быть 
объявлен интерфейс ( public interface Regulator ... ), вызовом методов которого 
будет выполняться основная работа. Для начала и завершения работы с объектом 
- реализацией интерфейса, необходимы статические методы. Пользователь 
(серверное приложение) библиотеки не должен иметь возможность создать 
реализации интерфейса, иным способом кроме вызова статических методов. При 
повторном вызове метода для создания объекта - должен быть возвращён ранее 
созданный объект. При вызове метода завершения работы - соответственно 
ранее созданный объект удаляется.
При задании температуры добавляются несколько (например 3 + random (6)) 
значений. С линейной интерполяцией температуры от последнего значения к 
заданному.
### Операции, которые должен выполнять Регулятор:
1. Задать температуру.
2. Получить значения температуры.
3. Удалить значения температуры (очистить список).
   
### Особые требования
Это усложнения, которые будут использованы для проверки умений: всё 
взаимодействие с регулятором должно выполняться только 1 методом:
int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut)
operation - специально байтовое поле. (проверка умений работать с битами байта)
В первых 3х битах которого задаётся операция, в биты с 4 по 7й - сколько данных 
необходимо прочесть, ну и в добавок 8й бит "зарезервирован" и всегда должен 
быть 1.
1й бит - очищать или нет значения температуры
2й бит - задавать или нет температуру
3й бит - получать или нет температуру обратно.
4й - 7й биты - число от 0 до ... (скольки - это будет один из вопросов) - количество 
данных, которые нужно прочесть.
в поле inData – задаётся, до которой должен «нагреться» или «остыть» регулятор 
температуры.
в список outData - записываются предыдущие значения, в offsetOut - задаётся 
сдвиг относительно индекса последнего значения.
результат работы функции - 0 - успех, 1,2,3... - коды ошибок.
Пример вызова: regul.adjustTemp(0b11100101, -300, outData, 5) return 3
необходимо: одновременно удалить старые значения, добавить новые, прочесть 
последние 3 значения и выдать ошибку из-за превышения минимльной 
температуры.
