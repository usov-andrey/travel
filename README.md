# 2014-2015 год
  Я планировал отдохнуть на Филиппинах, но за 3 недели мне хотелось бы посетить 3 места и между каждыми из них нужно делать перелет.
  
  В зависимости от дат, сроков нахождения в этих местах, последовательности посещения мест и авиакомпаний для перелета,
  стоимость такого путешествия могла различаться вплоть до 20 тыс рублей.
  И я решил написать программу, которая сделает за меня все эти вычисления.
  
  Оказалось, что это NP сложная задача, а значит простым перебором все возможных вариантов ее не решить.
  В результате загрузив данные о городах, аэропортах, всех возможных перелетов из aviasales, booking, rome2rio
  и данные о билетах с airaisa, cebupacific, tiger airlines у меня получилось построить из этого всего граф для обхода.
  С помощью разных эвристик даже получилось добиться скорости вычисления меньше минуты.
  
  Но Филлипины я посетил еще до того как все у меня было готово и только при посещении Бали и Индонезии я смог воспользоваться
  результатом работы и сэкономить десяток тысяч рублей.
  
  К сожалению, последняя версия куда-то потерялась(в текущей точно нет парсинга индонезийских авиалиний) и насколько все это
  сейчас в рабочем состоянии совсем непонятно. Но с основным алгоритмом можно разобраться.
  
  Начинать нужно с org.humanhelper.travel.route.routesearch.RouteService
